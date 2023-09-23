package com.atguigu.paymentdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.atguigu.paymentdemo.entity.OrderInfo;
import com.atguigu.paymentdemo.entity.RefundInfo;
import com.atguigu.paymentdemo.enums.OrderStatus;
import com.atguigu.paymentdemo.enums.PayType;
import com.atguigu.paymentdemo.enums.alipay.AliPayTradeState;
import com.atguigu.paymentdemo.service.AliPayService;
import com.atguigu.paymentdemo.service.OrderInfoService;
import com.atguigu.paymentdemo.service.PaymentInfoService;
import com.atguigu.paymentdemo.service.RefundInfoService;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
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

    @Resource
    private RefundInfoService refundsInfoService;

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
     *
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

    /**
     * 根据商户订单号查询支付宝查单接口，核实订单状态
     * <p>
     * 1.如果订单未创建，则直接更新商户端的订单状态即可
     * 2.如果订单未支付，则调用关单接口关闭订单，并更新商户端订单状态
     * 3.如果已经支付，则更新商户端订单状态，并记录支付日志
     *
     * @param orderNo 商户订单号
     */
    @Override
    public void checkOrderStatus(String orderNo) {
        log.info("根据订单号核实订单状态 ===》{}", orderNo);
        String result = this.queryOrder(orderNo);//如果返回的是null，表示支付宝端不存在此订单

        if (result == null) {
//          TODO 如果订单未创建，则直接更新商户端的订单状态即可
            log.info("核实订单未创建 ===》{}", orderNo);
//          更新本地订单状态,将订单关闭
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
            return;
        }

        JSONObject resultJson = JSONObject.parseObject(result);
        log.info("resultJson - " + resultJson);
        String tradeStatus = resultJson.getJSONObject("alipay_trade_query_response").getString("trade_status");
        log.info("tradeStatus - " + tradeStatus);

//      TODO 如果订单未支付，则调用关单接口关闭订单，并更新商户端订单状态
        if (AliPayTradeState.NOTPAY.getType().equals(tradeStatus)) {
            log.info("核实订单未支付 ===》{}", orderNo);
//          调用关单接口关闭订单(统一收单交易关闭接口)
            this.closeOrder(orderNo);
//          并更新商户端订单状态(超时关闭)
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
            return;
        }

//      TODO 如果已经支付，则更新商户端订单状态，并记录支付日志
//      正常情况下，用户付款后，商户系统会收到支付宝发送的一个异步通知，在这个通知中我们会接收到支付宝处理的结果，然后我们去修改订单的状态
//      但是在定时查单的过程中发现，明明已经支付的订单但是在商户系统中显示没有支付，这是怎么发生的呢？
//      原因是商户系统由于某些原因没有接收到异步通知 或者说 支付宝发送异步通知失败
//      所以这个时候我们可以主动调用支付宝端的查单接口来向支付宝确认支付结果
        if (AliPayTradeState.SUCCESS.getType().equals(tradeStatus)) {
            log.info("核实订单已支付 ===》{}", orderNo);
//          修改订单的状态
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);
            Map<String, String> params = JSON.parseObject(resultJson.getJSONObject("alipay_trade_query_response").toJSONString(), new TypeReference<Map<String, String>>() {
            });
//          记录支付日志
            paymentInfoService.createPaymentInfoForAliPay(params);
        }
    }

    /**
     * 退款
     * <p>
     * 支付宝退款和微信退款是有一些不同的
     * 支付宝中的退款是一个同步返回结果
     * 而微信是一个异步返回结果，所以微信需要通过退款的异步通知来获取退款的状态
     * 而支付宝中的退款，只要是非银行卡类的退款，我们就能得到退款是否成功
     * 比如下面的代码中我们使用response.isSuccess()即可判断
     *
     * @param orderNo
     * @param reason
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refund(String orderNo, String reason) {

        try {
            log.info("调用退款API");

            //在商户系统中创建退款单
            RefundInfo refundInfo = refundsInfoService.createRefundByOrderNoForAliPay(orderNo, reason);

            //调用支付宝统一收单交易退款接口
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

            //组装当前业务方法的请求参数
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);//商户订单编号 （trade_no是支付宝生成的订单号）
            BigDecimal refund = new BigDecimal(refundInfo.getRefund().toString()).divide(new BigDecimal("100"));
            //BigDecimal refund = new BigDecimal("2").divide(new BigDecimal("100"));
            bizContent.put("refund_amount", refund);//退款金额：不能大于支付金额
            bizContent.put("refund_reason", reason);//退款原因(可选)

            request.setBizContent(bizContent.toString());

            //执行请求，调用支付宝接口
            AlipayTradeRefundResponse response = alipayClient.execute(request);

            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());

                //更新订单状态
                orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_SUCCESS);

                //更新退款单
                refundsInfoService.updateRefundForAliPay(
                        refundInfo.getRefundNo(),
                        response.getBody(),
                        AliPayTradeState.REFUND_SUCCESS.getType()); //退款成功

            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());

                //更新订单状态
                orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_ABNORMAL);

                //更新退款单
                refundsInfoService.updateRefundForAliPay(
                        refundInfo.getRefundNo(),
                        response.getBody(),
                        AliPayTradeState.REFUND_ERROR.getType()); //退款失败
            }


        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("创建退款申请失败");
        }
    }

    /**
     * 查询退款
     *
     * @param orderNo
     * @return
     */
    @Override
    public String queryRefund(String orderNo) {

        try {
            log.info("查询退款接口调用 ===> {}", orderNo);

            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);//第三方订单号
            bizContent.put("out_request_no", orderNo);//退款请求号，请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的商户订单号
            request.setBizContent(bizContent.toString()); //

            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());
                return response.getBody();
            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
                //throw new RuntimeException("查单接口的调用失败");
                return null;//订单不存在
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("查单接口的调用失败");
        }
    }

    @Override
    public String queryBill(String billDate, String type) {
        try {

            AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("bill_type", type);
            bizContent.put("bill_date", billDate);
            request.setBizContent(bizContent.toString());
            AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);

            if(response.isSuccess()){
                log.info("调用成功，返回结果 ===> " + response.getBody());

                //获取账单下载地址
                Gson gson = new Gson();
                HashMap<String, LinkedTreeMap> resultMap = gson.fromJson(response.getBody(), HashMap.class);
                LinkedTreeMap billDownloadurlResponse = resultMap.get("alipay_data_dataservice_bill_downloadurl_query_response");



                String billDownloadUrl = (String)billDownloadurlResponse.get("bill_download_url");

                return billDownloadUrl;
            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
                throw new RuntimeException("申请账单失败");
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("申请账单失败");
        }
    }

}











