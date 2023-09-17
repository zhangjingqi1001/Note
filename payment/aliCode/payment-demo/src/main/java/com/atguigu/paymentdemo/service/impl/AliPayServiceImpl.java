package com.atguigu.paymentdemo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.atguigu.paymentdemo.entity.OrderInfo;
import com.atguigu.paymentdemo.service.AliPayService;
import com.atguigu.paymentdemo.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Slf4j
@Service
public class AliPayServiceImpl implements AliPayService {

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private Environment config;

    @Transactional
    @Override
    public String tradeCreate(Long productId) {
        AlipayTradePagePayResponse response = null;
        try {
//          1.生成订单
            OrderInfo orderInfo = orderInfoService.createOrderByProductId(productId);
//          2.调用支付宝接口
//          请求的支付宝开放平台的接口名alipay.trade.page.pay，所以对象叫做AlipayTradePagePayRequest
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
//          异步接收地址，仅支持http/https，公网可访问
//          request.setNotifyUrl("");
//          同步跳转地址，仅支持http/https
            request.setReturnUrl(config.getProperty("alipay.return-url"));//http://localhost:8080/#/success

            /******必传参数******/
            JSONObject bizContent = new JSONObject();
//          商户订单号，商家自定义，保持唯一性，64个字符以内，仅支持字母、数字、下划线且需保证在商户端不重复
            bizContent.put("out_trade_no", orderInfo.getOrderNo());

//          订单总金额，最小值0.01元，取值范围为 [0.01,100000000]。金额不能为0
            BigDecimal totalFee = new BigDecimal(orderInfo.getTotalFee().toString()).divide(new BigDecimal(100));
            bizContent.put("total_amount", totalFee);

//          订单标题，不可使用特殊符号，如 /，=，& 等
            bizContent.put("subject", orderInfo.getTitle());

//          电脑网站支付场景固定传值FAST_INSTANT_TRADE_PAY
            bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");

//          此时是完整的Request对象
            request.setBizContent(bizContent.toString());

//          响应结果的名字
            response = alipayClient.pageExecute(request);

            if (response.isSuccess()) {
                log.info("调用成功 - "+response);
                log.info(response.getBody());
            } else {
                log.info("调用失败 - "+response);
                log.info("返回描述 - "+response.getMsg());
                log.info("返回状态码 - "+response.getCode());
                throw  new RuntimeException("创建支付交易失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw  new RuntimeException("创建支付交易失败");
        }

        return response.getBody();
    }

}










