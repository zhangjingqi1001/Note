package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.SimpleRedisLock;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
@Slf4j
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    //TODO 获取脚本
//  RedisScript是一个接口，我们使用一个实现类，泛型就是返回值的类型
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    //静态代码块做初始化
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
//      借助Spring提供的方法区resource下找
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
//      配置一下返回值类型
        SECKILL_SCRIPT.setResultType(Long.class);
    }


    //创建一个线程池
    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();

    @PostConstruct//当前类初始化完毕后执行这个方法
    private void init() {
        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
    }

    private class VoucherOrderHandler implements Runnable {
        String queueName = "stream.orders";
        @Override
        public void run() {

            while (true) {
                try {
//                  TODO 1.获取消息队列中的订单信息,XREADGROUP GROUP g1 c1 COUNT 1 BLOCK 2000 STREAMS stream.orders >
//                  为什么返回一个List？因为count不是1的话，可能会有多条消息
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
//                          组是属于消费者的一部分信息，在这里统一叫做Consumer(一定是Spring包下的)
                            Consumer.from("g1", "c1"),
//                          StreamReadOptions.empty()创建一个空的，count(1)每次读取一条消息
//                          block(Duration.ofMillis(2000))，只能传入一个Duration参数
                            StreamReadOptions.empty().count(1).block(Duration.ofMillis(2000)),
//                          queueName消息队列名字,ReadOffset.lastConsumed()最近一个未消费消息
                            StreamOffset.create(queueName, ReadOffset.lastConsumed())
                    );
//                  TODO 2.判断消息获取是否成功
                    if(list ==null || list.isEmpty() ){
//                      TODO 2.1 如果获取失败，说明没有消息，继续下一次循环
//                      如果获取失败，说明没有消息，继续下一次循环
                        continue;
                    }

//                  TODO 3.解析消息中的订单信息
//                  明确知道count是1，所以直接获取0就行
                    MapRecord<String, Object, Object> record = list.get(0);
//                  消息id
                    RecordId id = record.getId();
//                  将Map转成VoucherOrder
                    Map<Object, Object> values = record.getValue();
//                  true表示出现错误忽略
                    VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(values, new VoucherOrder(), true);
//                  TODO 3.如果获取成功，可以下单
                    handleVoucherOrder(voucherOrder);
//                  TODO 4.ACK确认,XACK stream.orders g1 id
                    stringRedisTemplate.opsForStream().acknowledge(queueName,"g1",id);
                } catch (Exception e) {
                    log.error("处理订单异常", e);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
//                  TODO 处理时消息时报异常了，消息没有被确认，会进入pending-list，我们在这里需要去pendingList取出来再去做处理
                    handlePendingList();
                }
            }
        }
        private void handlePendingList() {
            while (true) {
                try {
//                  TODO 1.获取pending-list中的订单信息,XREADGROUP GROUP g1 c1 COUNT 1  STREAMS stream.orders 0
//                  为什么返回一个List？因为count不是1的话，可能会有多条消息
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
//                          组是属于消费者的一部分信息，在这里统一叫做Consumer(一定是Spring包下的)
                            Consumer.from("c1", "g1"),
//                          StreamReadOptions.empty()创建一个空的，count(1)每次读取一条消息
                            StreamReadOptions.empty().count(1),
//                          queueName消息队列名字,ReadOffset.from("0"),读取pending-list第一条消息
                            StreamOffset.create(queueName, ReadOffset.from("0"))
                    );
//                  TODO 2.判断消息获取是否成功
                    if(list ==null || list.isEmpty() ){
//                      TODO 2.1 如果获取失败，说明pending-list没有异常消息，结束循环
//                      如果获取失败，说明没有消息，继续下一次循环
                        continue;
                    }

//                  TODO 3.解析消息中的订单信息
//                  明确知道count是1，所以直接获取0就行
                    MapRecord<String, Object, Object> record = list.get(0);
//                  消息id
                    RecordId id = record.getId();
//                  将Map转成VoucherOrder
                    Map<Object, Object> values = record.getValue();
//                  true表示出现错误忽略
                    VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(values, new VoucherOrder(), true);
//                  TODO 3.如果获取成功，可以下单
                    handleVoucherOrder(voucherOrder);
//                  TODO 4.ACK确认,XACK stream.orders g1 id
                    stringRedisTemplate.opsForStream().acknowledge(queueName,"g1",id);
                } catch (Exception e) {
                    log.error("处理订单异常", e);
//                  这个地方不用递归，直接下一次循环处理就行
                }
            }

        }
    }

    /**
     * 订单处理方法
     * 我们在Redisson脚本中加了一个次锁了，在这里为什么还要再加一次锁呢？
     * 做一个兜底方案，万一Redis出现问题，有个保证
     *
     * @param voucherOrder
     */
    private void handleVoucherOrder(VoucherOrder voucherOrder) {
//      1.获取用户
        Long userId = voucherOrder.getUserId();
//      2.创建锁对象
        RLock lock = redissonClient.getLock("lock:order:" + userId);
//      3.获取锁
        boolean isLock = lock.tryLock();
//      4.判断是否获取锁成功
        if (!isLock) {
//          获取锁失败
            log.error("不允许重复下单");
            return;
        }
        try {
//            之前这种获取代理对象的方式是获取不到的,因为不在同一个线程下面了
//            VoucherOrderServiceImpl proxy = (VoucherOrderServiceImpl) AopContext.currentProxy();
//            return proxy.createVoucherOrder(voucherId);
//            在主线程获取，这个地方使用
            proxy.createVoucherOrder(voucherOrder);
        } finally {
//          出现异常做锁的释放
            lock.unlock();
        }

    }

    private VoucherOrderServiceImpl proxy;

    @Override
    public Result seckillVoucher(Long voucherId) {
//      获取用户id
        Long userId = UserHolder.getUser().getId();
//      获取订单id，这不操作移到上面
        long orderId = redisIdWorker.nextId("order");
//      1.执行lua脚本
//      我们没有传入key，所以传入一个空集合即可
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(), userId.toString(), String.valueOf(orderId)
        );
//      2.判断结果是否为0（0有购买资格，不为0没有购买资格）
        int r = result.intValue();
        if (r != 0) {
//      2.1 不为0没有购买资格
            return Result.fail(r == 1 ? "库存不足" : "不能重复下单");
        }
//      3.获取代理对象
        proxy = (VoucherOrderServiceImpl) AopContext.currentProxy();

//      4. 返回订单id
        return Result.ok(orderId);
    }


    //  如果在方法上添加synchronized,说明同步锁是this，当前对象
//  不建议 把synchronized放在方法上，锁住此对象后，不管任何一个用户来了，都是这把锁，也就意味着整个方法被串行化了
//  所谓“一人一单”，只需要对同一个用户加锁即可，如果不是同一个用户，无需加锁
    @Transactional
    public void createVoucherOrder(VoucherOrder voucherOrder) {
//      新增一人一单的判断
        Long userId = voucherOrder.getUserId();
//      user.getId().toString()转换成字符串也无法保证线程安全，因为每次的String都不一样
//      我们可以加一个intern，是一个字符串对象规范表示，回去字符串常量池中找一找和此字符串的值一样的字符串地址并返回

//      查询订单
        int count = query().eq("user_id", userId)
                .eq("voucher_id", voucherOrder.getVoucherId()).count();
//      判断是否存在
        if (count > 0) {
//      用户至少下过一单，不能再下了
            log.error("用户至少下过一单，不能再下了");
            return;
        }
//      说明没买过，继续执行代码
//      5.扣减库存
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock-1") //set stock = stock-1
                .eq("voucher_id", voucherOrder.getVoucherId()) //where  voucher_id= voucherId
                .gt("stock", 0)//where  stock>0
                .update();
        if (!success) {
            log.error("扣减失败，可能库存不足");
            return;
        }
//      创建订单
        save(voucherOrder);
    }
}


//    @Override
//    public Result seckillVoucher(Long voucherId) {
////      1.查询优惠券
//        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
////      2.判断秒杀是否开始
//        if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
//            return Result.fail("秒杀活动尚未开始");
//        }
////      3.判断秒杀是否结束
//        if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
//            return Result.fail("秒杀活动已经结束");
//        }
////      4.判断库存是否充足
//        if (voucher.getStock() < 1) {
//            return Result.fail("库存不足");
//        }
//        UserDTO user = UserHolder.getUser();
////      创建锁对象
////      锁定的范围与之前一样。切记不能把order锁住，范围太大了，以后有关order的都被锁住了
////      之前的方式：SimpleRedisLock lock = new SimpleRedisLock("order:" + user.getId(), stringRedisTemplate);
////      TODO 使用Redisson客户端获取锁对象
//        RLock lock = redissonClient.getLock("lock:order:" + user.getId());
////      获取锁
////      订单大概是500ms，我们这里可以设定为秒
//        boolean isLock = lock.tryLock();
////      判断是否获取锁成功
//        if (!isLock) {
////      不成功
////      我们要避免一个用户重复下单,既然获取锁失败，说明在并发执行，我们要避免并发执行
//            return Result.fail("不允许重复下单");
//        }
////      成功
////      createVoucherOrder方法执行过程中可能会有异常，我们放到try...catch中
//        try {
////          获取当前对象的代理对象 强转
//            VoucherOrderServiceImpl proxy = (VoucherOrderServiceImpl) AopContext.currentProxy();
//            return proxy.createVoucherOrder(voucherId);
//        }finally {
////          出现异常做锁的释放
//            lock.unlock();
//        }
//
//    }

//*******************************************************************************
//   下面这段使用的阻塞队列
//   创建堵塞队列
//  有一个特点：当一个线程尝试从这个队列里获取元素时，如果没有元素，这个线程就会堵塞，知道队列中有元素他才会被唤醒
//  1024*1024表示队列的长度
//private BlockingQueue<VoucherOrder> orderTasks = new ArrayBlockingQueue<>(1024 * 1024);
//    private VoucherOrderServiceImpl proxy;
//
//  TODO 创建一个线程任务
//  什么时候执行这个任务呢？用户秒杀抢购之前，因为一旦用户开始秒杀，阻塞队列中就会有新的订单，这个任务就应该去取出订单相关信息
//private class VoucherOrderHandler implements Runnable {
//    @Override
//    public void run() {
////          不断的从堵塞队列orderTasks取出信息，然后执行
//        while (true) {
////              take就是一个阻塞方法，获取队列中的头部,如果没有就等到有
//            try {
////                  TODO 1.获取队列中的订单信息
//                VoucherOrder voucherOrder = orderTasks.take();
////                  TODO 2.创建订单
//                handleVoucherOrder(voucherOrder);
//            } catch (Exception e) {
////                    e.printStackTrace();
//                log.error("处理订单异常", e);
//            }
//        }
//    }
//}
//    @PostConstruct//当前类初始化完毕后执行这个方法
//    private void init() {
//        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
//    }
//    @Override
//    public Result seckillVoucher(Long voucherId) {
//        Long userId = UserHolder.getUser().getId();
////      1.执行lua脚本
////      我们没有传入key，所以传入一个空集合即可
//        Long result = stringRedisTemplate.execute(
//                SECKILL_SCRIPT,
//                Collections.emptyList(),
//                voucherId.toString(), userId.toString()
//        );
////      2.判断结果是否为0（0有购买资格，不为0没有购买资格）
//        int r = result.intValue();
//        if (r != 0) {
////      2.1 不为0没有购买资格
//            return Result.fail(r == 1 ? "库存不足" : "不能重复下单");
//        }
////      TODO 2.2 为0有购买资格,把下单的信息保存到堵塞队列,后续可以异步完成下单业务
////      TODO 阻塞队列，将用户id，订单id，优惠券id，保存到阻塞队列中
//        VoucherOrder voucherOrder = new VoucherOrder();
////      TODO 2.3 生成订单id
//        long orderId = redisIdWorker.nextId("order");
//        voucherOrder.setId(orderId);
////      TODO 2.4 用户id
//        voucherOrder.setUserId(userId);
////      TODO 2.5 代金券id
//        voucherOrder.setVoucherId(voucherId);
////      TODO 2.6 放入阻塞队列
//        orderTasks.add(voucherOrder);
////      TODO 3.获取代理对象
//        proxy = (VoucherOrderServiceImpl) AopContext.currentProxy();
//
////      3. 返回订单id
//        return Result.ok(orderId);
//    }
//        /**
//     * 订单处理方法
//     * 我们在Redisson脚本中加了一个次锁了，在这里为什么还要再加一次锁呢？
//     * 做一个兜底方案，万一Redis出现问题，有个保证
//     *
//     * @param voucherOrder
//     */
//    private void handleVoucherOrder(VoucherOrder voucherOrder) {
////      TODO 1.获取用户
//        Long userId = voucherOrder.getUserId();
////      TODO 2.创建锁对象
//        RLock lock = redissonClient.getLock("lock:order:" + userId);
////      TODO 3.获取锁
//        boolean isLock = lock.tryLock();
////      TODO 4.判断是否获取锁成功
//        if (!isLock) {
////          获取锁失败
//            log.error("不允许重复下单");
//            return;
//        }
////      TODO
//        try {
////            之前这种获取代理对象的方式是获取不到的,因为不在同一个线程下面了
////            VoucherOrderServiceImpl proxy = (VoucherOrderServiceImpl) AopContext.currentProxy();
////            return proxy.createVoucherOrder(voucherId);
////            TODO 在主线程获取，这个地方使用
//            proxy.createVoucherOrder(voucherOrder);
//        } finally {
////          出现异常做锁的释放
//            lock.unlock();
//        }
//
//    }

//*********************************************


