package com.payment.service;

import com.alibaba.fastjson2.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

public interface WxPayService {
    Map<String, Object> nativePay(Long productId) throws Exception;

    void processOrder(JSONObject bodyJson) throws GeneralSecurityException;

    void cancelOrder(String orderNo) throws IOException;

    String queryOrder(String orderNo) throws IOException;

    void checkOrderStatus(String orderNo) throws IOException;
}
