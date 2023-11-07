package com.payment.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.mysql.cj.util.StringUtils;
import com.payment.config.WxPayConfig;
import com.payment.entity.OrderInfo;
import com.payment.enums.OrderStatus;
import com.payment.enums.wxpay.WxNotifyType;
import com.payment.enums.wxpay.WxTradeState;
import com.payment.service.OrderInfoService;
import com.payment.service.PaymentInfoService;
import com.payment.service.WxPayService;
import com.payment.util.OrderNoUtils;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class WxPayServiceImpl implements WxPayService {

    @Autowired
    private WxPayConfig wxPayConfig;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private PaymentInfoService paymentInfoService;

    /**
     * 创建订单，调用Native支付接口
     *
     * @param productId 商品ID
     * @return 支付二维码url
     */
    @Override
    public Map<String, Object> nativePay(Long productId) throws Exception {
//      TODO 生成订单
        OrderInfo orderInfo = orderInfoService.createOrderByProductId(productId);

        if (orderInfo != null && !StringUtils.isNullOrEmpty(orderInfo.getCodeUrl())) {
            log.info("二维码订单已经存在-" + orderInfo.getCodeUrl());
//          之后再支付这个商品的时候，直接通过url生成二维码扫描即可
            HashMap<String, Object> returnMap = new HashMap<>();
            returnMap.put("codeUrl", orderInfo.getCodeUrl());
            return returnMap;
        }

//      TODO 调用统一下单API
        //请求URL
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/native");
//        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");

        // 请求body参数(这个地方封装成一个对象再转JSON也是没问题的)
        //Gson gson = new Gson();
        Map paramsMap = new HashMap<>();
        //公众号ID
        paramsMap.put("appid", wxPayConfig.getAppid());
        //直连商户号
        paramsMap.put("mchid", wxPayConfig.getMchId());
        //商品描述
        paramsMap.put("description", orderInfo.getTitle());
        //商户订单号
        paramsMap.put("out_trade_no", orderInfo.getOrderNo());
        //通知地址（整个支付流程完成后，微信平台要通知我们的商户平台），这个地址其实是我们内网穿透的地址
        // 这个地方拼接完成就是https://500c-219-143-130-12.ngrok.io//api/wx-pay/native/notify，这个接口是我们自己定义的
        paramsMap.put("notify_url", wxPayConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));
        //订单金额
        Map amountMap = new HashMap<>();
        //订单总金额
        amountMap.put("total", orderInfo.getTotalFee());
        //货币类型 人民币
        amountMap.put("currency", "CNY");

        paramsMap.put("amount", amountMap);

        log.info("Native统一支付下单请求数据 - " + JSONObject.toJSONString(paramsMap));//{"amount":{"total":1,"currency":"CNY"},"mchid":"1558950191","out_trade_no":"ORDER_20231105014402447","appid":"wx74862e0dfcf69954","description":"test","notify_url":"https://06ca-240e-444-10-1f3f-513c-1cc6-c851-ccba.ngrok-free.app/api/wx-pay/native/notify"}

        //请求体
        StringEntity entity = new StringEntity(JSONObject.toJSONString(paramsMap), "utf-8");
        //JSON类型的请求数据
        entity.setContentType("application/json");
        //说明请求的请求体
        httpPost.setEntity(entity);
        //设置请求头Accept，意思是希望接收的请求数据也是JSON
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        //CloseableHttpClient对象自带签名和验签，所以我们直接执行execute方法就好了
        CloseableHttpResponse response = httpClient.execute(httpPost);
        log.info("Native统一支付下单响应数据 - " + response); //HttpResponseProxy{HTTP/1.1 200 OK [Server: nginx, Date: Fri, 03 Nov 2023 07:11:23 GMT, Content-Type: application/json; charset=utf-8, Content-Length: 52, Connection: keep-alive, Keep-Alive: timeout=8, Cache-Control: no-cache, must-revalidate, X-Content-Type-Options: nosniff, Request-ID: 089BBA92AA0610E50318F7C58C5820F79B0D28B25F-0, Content-Language: zh-CN, Wechatpay-Nonce: e71434e9c3a974376ceb078ddfd17271, Wechatpay-Signature: FXlbxm0OvGeUF08kxIdCPLH1fFP/lc9RwoJ2iLK4iYimRKFNUbs1xp7Z/7nYmI5G1+hCZcYGWAWWmwPQ8p7z1tE8C3Y78fbStOBKzt/fVhQnEG/cR7HiFrVN60F7zhOecx8bmDYqhBA2k+7BU1bhJcXEwSR+6PMv0BVERL6crk60ngCxZge3+fSz3lNxk42IDQKjM9rI/YHK/uyCi1YNuOQKs8EZEgArBJ7yjek884OT0RMWwPnRf9KVoiJoHeS1dMf3jJqDBXL/0ap+pXgSwh0XK/YKNg788kZwSjENzLRi8NR7QjjfF2pWaUHN2OIabYzvYuWhIEZeECw7kdDGsQ==, Wechatpay-Timestamp: 1698995483, Wechatpay-Serial: 70806CE61990E2AAF559EC92EBD2058E6FA733EB, Wechatpay-Signature-Type: WECHATPAY2-SHA256-RSA2048] org.apache.http.entity.BufferedHttpEntity@1b094186}

        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {//处理成功
                log.info("success,return body = " + EntityUtils.toString(response.getEntity()));//{"code_url":"weixin://wxpay/bizpayurl?pr=IcXgoEAzz"}
            } else if (statusCode == 204) {//处理成功，但是没有返回值
                log.info("success");
            } else {
                log.info("Native下单失败 响应码：" + statusCode + ",响应体 = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            //关闭
            response.close();
//            httpClient.close();
        }

        //将相应结果转换成JSON
        JSONObject responseJson = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));

        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put("codeUrl", responseJson.get("code_url"));
        //也要返回订单号，前端之后会向后端传输此参数，判断用户是否下单
        returnMap.put("orderNo", orderInfo.getOrderNo());
//      将二维码的地址存储起来，因为有效期为两个小时，两个小时如果没下单还是可以扫的
        orderInfoService.saveCodeUrl(orderInfo.getOrderNo(), responseJson.get("code_url").toString());

        return returnMap;
    }


    //  可重入锁
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void processOrder(JSONObject bodyJson) throws GeneralSecurityException {
        log.info("处理订单");

        //解密数据,得到明文
        String plainText = decryptFromResource(bodyJson);

        //将明文转换成map,下面这个JSON数据其实就是bodyJson中的resource
        JSONObject plainTextJSON = JSONObject.parseObject(plainText);
        String outTradeNo = plainTextJSON.getString("out_trade_no");

        //在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱
        //尝试获取锁，立即获取到锁就是true，获取失败则立即返回false，不会一直等待锁的释放
        //这个锁与synchronized的区别就是synchronized获取不到锁会一直等待，ReentrantLock获取不到锁就返回false
        if (lock.tryLock()) {
            try {
                //处理重复通知
                String orderStatus = orderInfoService.getOrderStatus(outTradeNo);
                if (!OrderStatus.NOTPAY.getType().equals(orderStatus)) {
                    //说明是已支付，我们直接返回订单状态即可
                    return;
                }
                //更新订单状态
                orderInfoService.updateStatusByOrderNo(outTradeNo, OrderStatus.SUCCESS);
                //记录支付日志
                paymentInfoService.createPaymentInfo(plainText);
            } finally {
                //主动释放锁
                lock.unlock();
            }
        }


    }

    /**
     * 对称解密
     */
    private String decryptFromResource(JSONObject bodyJson) throws GeneralSecurityException {
        log.info("密文解密");

        JSONObject resource = bodyJson.getJSONObject("resource");
//      额外数据
        String associatedData = resource.getString("associated_data");
//      密文
        String ciphertext = resource.getString("ciphertext");
        log.info("密文 - " + ciphertext);
//      随机串
        String nonce = resource.getString("nonce");

//      参数需要一个byte形式的对称加密的密钥
//      wxpay.api-v3-key=UDuLFDcmy5Eb6o0nTNZdu6ek4DDh4K8B
        AesUtil aesUtil = new AesUtil(wxPayConfig.getApiV3Key().getBytes());
//      第一个参数：associated_data附加数据，第二个参数：随机串，第三个参数：密文
//      得到明文
        String plainText = aesUtil.decryptToString(associatedData.getBytes(), nonce.getBytes(), ciphertext);
        log.info("明文：plainText - " + plainText);
        return plainText;
    }


    /**
     * 用户取消订单
     *
     * @param orderNo 订单号
     */
    @Override
    public void cancelOrder(String orderNo) throws IOException {
        //调用微信支付的关单接口
        this.closeOrder(orderNo);
        //更新商户端的订单状态
        orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CANCEL);// CANCEL("用户已取消")
    }


    private void closeOrder(String orderNo) throws IOException {
        log.info("关单接口的调用 - 订单号 - " + orderNo);
        String httpPostUrl = String.format("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/%s/close", orderNo);
        log.info("请求地址-" + httpPostUrl);
        //TODO 创建HttpPost对象
        HttpPost httpPost = new HttpPost(httpPostUrl);

        //TODO 请求body参数(这个地方封装成一个对象再转JSON也是没问题的)
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mchid", wxPayConfig.getMchId());
//        paramsMap.put("out_trade_no", orderNo);

        //TODO 将请求参数设置到请求对象中
        StringEntity entity = new StringEntity(JSONObject.toJSONString(paramsMap), "utf-8");
        //JSON类型的请求数据
        entity.setContentType("application/json");
        //说明请求的请求体
        httpPost.setEntity(entity);
        //设置请求头Accept，意思是希望接收的请求数据也是JSON
        httpPost.setHeader("Accept", "application/json");

        //TODO 完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {//处理成功
                log.info("200 - success,return body = " + EntityUtils.toString(response.getEntity()));//{"code_url":"weixin://wxpay/bizpayurl?pr=IcXgoEAzz"}
            } else if (statusCode == 204) {//处理成功，但是没有返回值
                log.info("204 - success");
            } else {
                log.info("Native取消订单失败 响应码：" + statusCode + ",响应体 = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            //关闭
            response.close();
        }
        //因为没有响应数据，所以不需要处理响应数据
    }

    /**
     * 根据商户订单号向微信支付平台发送请求查询订单
     */
    @Override
    public String queryOrder(String orderNo) throws IOException {
        //拼接url
        String httpGetUrl = String.format("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/%s", orderNo);
        //拼接参数
        String httpGetUrlParam = httpGetUrl.concat("?mchid=").concat(wxPayConfig.getMchId());
        log.info("请求地址-" + httpGetUrlParam);

        //TODO 创建HttpGet对象
        HttpGet httpGet = new HttpGet(httpGetUrlParam);
        httpGet.setHeader("Accept", "application/json");

        //TODO 完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {//处理成功
                log.info("success,return body = " + EntityUtils.toString(response.getEntity()));//{"amount":{"currency":"CNY","payer_currency":"CNY","payer_total":1,"total":1},"appid":"wx74862e0dfcf69954","attach":"","bank_type":"OTHERS","mchid":"1558950191","out_trade_no":"ORDER_20231106165639372","payer":{"openid":"oHwsHuOAS1JWiRAnLKwq5qTg4UkQ"},"promotion_detail":[],"success_time":"2023-11-06T16:56:51+08:00","trade_state":"SUCCESS","trade_state_desc":"支付成功","trade_type":"NATIVE","transaction_id":"4200002050202311069075905809"}
            } else if (statusCode == 204) {
                log.info("success");
            } else {
                log.info("Native根据商户号查询订单失败 响应码：" + statusCode + ",响应体 = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            //关闭
            response.close();
//            httpClient.close();
        }

        //将相应结果转换成JSON
        JSONObject responseJson = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));


        return responseJson.toString();
    }

    /**
     * 根据订单号查询微信支付查单接口，核实订单状态
     * 如果订单已支付，则更新商户端订单状态为已支付
     * 如果订单未支付，则调用关单接口关闭订单，并封信商户端订单状态
     *
     * @param orderNo 商户订单号
     */
    @Override
    public void checkOrderStatus(String orderNo) throws IOException {
        log.info("根据订单号查询微信支付查单接口，核实订单状态 - " + orderNo);

        //向微信平台发起查单接口
        String result = this.queryOrder(orderNo);
        JSONObject resultJSON = JSONObject.parseObject(result);

        //获取微信支付端的订单状态
        String tradeState = resultJSON.getString("trade_state");
        log.info("订单状态 - " + tradeState);

        //判断订单状态
        if (WxTradeState.SUCCESS.getType().equals(tradeState)) {
            log.info("核实订单已支付 - " + orderNo);
            //更新本地订单状态
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);
            //记录支付日志
            paymentInfoService.createPaymentInfo(result);
        } else if (WxTradeState.NOTPAY.getType().equals(tradeState)) {
            log.info("核实订单未支付 - " + orderNo);
            //调用微信关单接口
            this.closeOrder(orderNo);
            //更新本地订单状态,超时已关闭
            orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
        }


    }
}
