package com.atguigu.paymentdemo.service;

public interface AliPayService {
    //  最终会返回一个表单字符串
    String tradeCreate(Long productId);
}
