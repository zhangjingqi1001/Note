package com.hmdp.service.impl;

import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.UserHolder;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Override
    public Result seckillVoucher(Long voucherId) {
//      1.查询优惠券
        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
//      2.判断秒杀是否开始
        if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
            return Result.fail("秒杀活动尚未开始");
        }
//      3.判断秒杀是否结束
        if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
            return Result.fail("秒杀活动已经结束");
        }
//      4.判断库存是否充足
        if (voucher.getStock() < 1) {
            return Result.fail("库存不足");
        }
        UserDTO user = UserHolder.getUser();
        synchronized (user.getId().toString().intern()) {
//          获取当前对象的代理对象 强转
            VoucherOrderServiceImpl proxy = (VoucherOrderServiceImpl) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        }
    }

    //  如果在方法上添加synchronized,说明同步锁是this，当前对象
//  不建议 把synchronized放在方法上，锁住此对象后，不管任何一个用户来了，都是这把锁，也就意味着整个方法被串行化了
//  所谓“一人一单”，只需要对同一个用户加锁即可，如果不是同一个用户，无需加锁
    @Transactional
    public Result createVoucherOrder(Long voucherId) {
//      TODO 新增一人一单的判断
        UserDTO user = UserHolder.getUser();
//      user.getId().toString()转换成字符串也无法保证线程安全，因为每次的String都不一样
//      我们可以加一个intern，是一个字符串对象规范表示，回去字符串常量池中找一找和此字符串的值一样的字符串地址并返回

//      TODO 查询订单
            int count = query().eq("user_id", user.getId())
                    .eq("voucher_id", voucherId).count();
//      TODO 判断是否存在
            if (count > 0) {
//      TODO 用户至少下过一单，不能再下了
                return Result.fail("一人一单，不可重复下单");
            }
//      TODO 说明没买过，继续执行代码
//      5.扣减库存
            boolean success = seckillVoucherService.update()
                    .setSql("stock = stock-1") //set stock = stock-1
                    .eq("voucher_id", voucherId) //where  voucher_id= voucherId
                    .gt("stock", 0)//where  stock>0
                    .update();
            if (!success) {
                return Result.fail("扣减失败，可能库存不足");
            }

//      6.创建订单
//      我们只管订单id，代金券id，下单用户id
            VoucherOrder voucherOrder = new VoucherOrder();
//      6.1 订单id
//      使用自定义id生成器生成id
            long orderID = redisIdWorker.nextId("order");
            voucherOrder.setId(orderID);
//      6.2 用户id
//      我们之前编写的登录拦截器
            voucherOrder.setUserId(user.getId());
//      6.3 代金券id
            voucherOrder.setVoucherId(voucherId);

            save(voucherOrder);

//      7.返回订单id
            return Result.ok(orderID);

    }
}
