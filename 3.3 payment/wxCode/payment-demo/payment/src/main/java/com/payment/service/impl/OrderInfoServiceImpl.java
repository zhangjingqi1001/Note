package com.payment.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.payment.entity.OrderInfo;
import com.payment.entity.Product;
import com.payment.enums.OrderStatus;
import com.payment.mapper.OrderInfoMapper;
import com.payment.mapper.ProductMapper;
import com.payment.service.OrderInfoService;
import com.payment.util.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {


    @Autowired
    private ProductMapper productMapper;

    /**
     * 创建订单
     * 我们并不是每次请求都需要创建订单的
     */
    @Override
    public OrderInfo createOrderByProductId(Long productId) {
//      TODO 查找已经存在但是未支付的订单
        OrderInfo orderInfo = this.getNoPayOrderByProductId(productId);
        if (orderInfo != null) {
            return orderInfo;
        }


        Product product = productMapper.selectById(productId);
        orderInfo = new OrderInfo();
        orderInfo.setTitle(product.getTitle());
        orderInfo.setProductId(productId);
//      订单金额（单位是分）
        orderInfo.setTotalFee(product.getPrice());
//      调用工具类生成订单号
        orderInfo.setOrderNo(OrderNoUtils.getOrderNo());
//      订单金额
        orderInfo.setOrderStatus(OrderStatus.NOTPAY.getType());

//      TODO 订单信息存入数据库
//      baseMapper指的就是当前OrderInfo的Mapper
        baseMapper.insert(orderInfo);

        return orderInfo;
    }

    /**
     * 查找已经存在但是未支付的订单
     */
    private OrderInfo getNoPayOrderByProductId(Long productId) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        queryWrapper.eq("order_status", OrderStatus.NOTPAY.getType());
//        queryWrapper.eq("user_id", userId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 存储订单二维码
     */
    @Override
    public void saveCodeUrl(String orderNo, String codeUrl) {

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCodeUrl(codeUrl);

        baseMapper.update(orderInfo, queryWrapper);
    }


    /**
     * 查询订单列表，并倒序查询
     *
     * @return
     */
    @Override
    public List<OrderInfo> listOrderByCreateTimeDesc() {

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<OrderInfo>().orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public String getOrderStatus(String orderNo) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        OrderInfo orderInfo = baseMapper.selectOne(queryWrapper);
        if (orderInfo == null) {
            return null;
        }
        return orderInfo.getOrderStatus();
    }

    /**
     * 根据订单号更新订单状态
     */
    @Override
    public void updateStatusByOrderNo(String outTradeNo, OrderStatus orderStatus) {
        log.info("更新订单状态 ===> {}", orderStatus.getType());

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", outTradeNo);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderStatus(orderStatus.getType());

        baseMapper.update(orderInfo, queryWrapper);
    }

    @Override
    public List<OrderInfo> getNoPayOrderByDuration(int minutes) {
        //五分钟之前
        Instant instant = Instant.now().minus(Duration.ofMinutes(5));


        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        //订单未支付
        queryWrapper.eq("order_status",OrderStatus.NOTPAY.getType());
        //创建的订单要早于5分钟之前
        queryWrapper.le("create_time",instant);

        return baseMapper.selectList(queryWrapper);
    }
    /**
     * 根据订单号获取订单
     *
     * @param orderNo
     * @return
     */
    @Override
    public OrderInfo getOrderByOrderNo(String orderNo) {

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        OrderInfo orderInfo = baseMapper.selectOne(queryWrapper);

        return orderInfo;
    }


    /**
     * 根据商品id查询未支付订单
     * 防止重复创建订单对象
     *
     * @param productId
     * @return
     */
    private OrderInfo getNoPayOrderByProductId(Long productId, String paymentType) {

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        queryWrapper.eq("order_status", OrderStatus.NOTPAY.getType());
        queryWrapper.eq("payment_type", paymentType);
//        queryWrapper.eq("user_id", userId);
        OrderInfo orderInfo = baseMapper.selectOne(queryWrapper);
        return orderInfo;
    }

}
