package com.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.payment.entity.OrderInfo;

import java.util.List;

public interface OrderInfoService extends IService<OrderInfo> {

    OrderInfo createOrderByProductId(Long productId);

    public void saveCodeUrl(String orderNo, String codeUrl);

    public List<OrderInfo> listOrderByCreateTimeDesc();

    String getOrderStatus(String orderNo);
}
