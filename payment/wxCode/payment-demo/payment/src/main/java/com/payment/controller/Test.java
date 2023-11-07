package com.payment.controller;

public class Test {
    public static void main(String[] args) {
        String httpPostUrl = String.format("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/%s/close","123456" );
        System.out.println(httpPostUrl);
    }

}

