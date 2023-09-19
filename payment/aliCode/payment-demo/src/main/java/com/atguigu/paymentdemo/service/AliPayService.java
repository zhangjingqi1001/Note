package com.atguigu.paymentdemo.service;

import java.util.Map;

public interface AliPayService {
    //  最终会返回一个表单字符串
    String tradeCreate(Long productId);

    void processOrder(Map<String, String> params);

    void cancelOrder(String orderNo);

    String queryOrder(String orderNo);
}
