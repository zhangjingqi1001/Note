package com.payment.service;

import com.alibaba.fastjson2.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public interface WxPayService {
    Map<String, Object> nativePay(Long productId) throws Exception;

    void processOrder(JSONObject bodyJson) throws GeneralSecurityException;

    void cancelOrder(String orderNo) throws IOException;

    String queryOrder(String orderNo) throws IOException;

    void checkOrderStatus(String orderNo) throws IOException;

    void refund(String orderNo, String reason) throws Exception;

    public String queryRefund(String refundNo) throws Exception;

    public void processRefund(Map<String, Object> bodyMap) throws Exception;

    String queryBill(String billDate, String type) throws Exception;

    String downloadBill(String billDate, String type) throws Exception;
}
