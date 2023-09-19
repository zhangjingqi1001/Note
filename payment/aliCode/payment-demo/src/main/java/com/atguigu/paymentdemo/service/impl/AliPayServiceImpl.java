package com.atguigu.paymentdemo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.atguigu.paymentdemo.entity.OrderInfo;
import com.atguigu.paymentdemo.enums.OrderStatus;
import com.atguigu.paymentdemo.enums.PayType;
import com.atguigu.paymentdemo.service.AliPayService;
import com.atguigu.paymentdemo.service.OrderInfoService;
import com.atguigu.paymentdemo.service.PaymentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class AliPayServiceImpl implements AliPayService {

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private Environment config;

    @Resource
    private PaymentInfoService paymentInfoService;

    @Transactional
    @Override
    public String tradeCreate(Long productId) {
        AlipayTradePagePayResponse response = null;
        try {
//          1.生成订单
            OrderInfo orderInfo = orderInfoService.createOrderByProductId(productId, PayType.ALIPAY.getType());
//          2.调用支付宝接口
//          请求的支付宝开放平台的接口名alipay.trade.page.pay，所以对象叫做AlipayTradePagePayRequest
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
//          异步接收地址，仅支持http/https，公网可访问
            request.setNotifyUrl(config.getProperty("alipay.notify-url"));
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
                log.info("调用成功 - " + response);
                log.info(response.getBody());
            } else {
                log.info("调用失败 - " + response);
                log.info("返回描述 - " + response.getMsg());
                log.info("返回状态码 - " + response.getCode());
                throw new RuntimeException("创建支付交易失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("创建支付交易失败");
        }

        return response.getBody();
    }


    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 处理订单
     *
     * @param params
     */
    @Override
    public void processOrder(Map<String, String> params) {
        log.info("处理订单");
//      获取订单号
        String orderNo = params.get("out_trade_no");
//      尝试获取锁：成功获取则立即返回true，获取失败则立即返回false，不必一直等待锁的释放
        if (lock.tryLock()) {
            try {
//              处理重复通知
//              接口调用的幂等性：无论接口被调用多少次，以下业务执行一次
                String orderStatus = orderInfoService.getOrderStatus(orderNo);
                if (!OrderStatus.NOTPAY.getType().equals(orderStatus)) {
//                  只要不是未支付，就执行下面这个地方
                    return;
                }
//              更新订单状态
                orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);//OrderStatus.SUCCESS 支付成功
//              记录支付日志
                paymentInfoService.createPaymentInfoForAliPay(params);
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 用户取消订单
     *
     * @param orderNo 订单号
     */
    @Override
    public void cancelOrder(String orderNo) {
//      调用支付宝提供的统一收单交易关闭接口
        this.closeOrder(orderNo);
//      更新用户的订单状态
        orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CANCEL);//OrderStatus.CANCEL 用户已取消


    }

    /**
     * 统一收单交易关闭接口
     *
     * @param orderNo 商户订单号
     */
    @Transactional
    public void closeOrder(String orderNo) {
        log.info("统一收单交易关闭接口:" + orderNo);

        try {
            AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();

            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo); // 商户订单号
            request.setBizContent(bizContent.toString());

            AlipayTradeCloseResponse response = alipayClient.execute(request);

            if (response.isSuccess()) {
                log.info("调用成功 - " + response);
                log.info(response.getBody());
            } else {
                log.info("调用失败 - " + response);
                log.info("返回描述 - " + response.getMsg());
                log.info("返回状态码 - " + response.getCode());
//              这个地方我们就不抛出异常了，让程序正常执行
//              在此else中出现的情况可能是支付宝并没有收到订单，进而没有收到out_trade_no，所以我们在这里使用out_trade_no查询不到
//              在cancelOrder方法中closeOrder方法执行完成之后，就会执行updateStatusByOrderNo，将订单状态修改为 用户已取消
//                throw new RuntimeException("统一收单交易关闭接口失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("统一收单交易关闭接口失败");
        }

    }

    /**
     * 查询订单
     * @param orderNo 商户订单号
     */
    @Override
    public String queryOrder(String orderNo) {

        try {
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);

            request.setBizContent(bizContent.toString());
            AlipayTradeQueryResponse response = alipayClient.execute(request);

            if (response.isSuccess()) {
                log.info("调用成功 - " + response);
                log.info(response.getBody());
                return response.getBody();
            } else {
                log.info("调用失败 - " + response);
                log.info("返回描述 - " + response.getMsg());
                log.info("返回状态码 - " + response.getCode());
//                throw new RuntimeException("统一收单线下交易查询调用失败");
                return null;
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("统一收单线下交易查询调用失败");
        }


    }

}











