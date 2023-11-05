package com.payment.service;

import com.alibaba.fastjson2.JSONObject;

import java.security.GeneralSecurityException;
import java.util.Map;

public interface WxPayService {
    Map<String, Object> nativePay(Long productId) throws Exception;

    void processOrder(JSONObject bodyJson) throws GeneralSecurityException;
}
