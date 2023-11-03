package com.payment.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.payment.config.WxPayConfig;
import com.payment.entity.OrderInfo;
import com.payment.enums.OrderStatus;
import com.payment.enums.wxpay.WxNotifyType;
import com.payment.service.OrderInfoService;
import com.payment.service.WxPayService;
import com.payment.util.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WxPayServiceImpl implements WxPayService {

    @Autowired
    private WxPayConfig wxPayConfig;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private CloseableHttpClient httpClient;

    /**
     * 创建订单，调用Native支付接口
     * @param productId 商品ID
     * @return 支付二维码url
     */
    @Override
    public Map<String, Object> nativePay(Long productId) throws Exception {
//      TODO 生成订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setTitle("test");
        orderInfo.setProductId(productId);
//      订单金额（单位是分）
        orderInfo.setTotalFee(1);
//      调用工具类生成订单号
        orderInfo.setOrderNo(OrderNoUtils.getOrderNo());
//      订单金额
        orderInfo.setOrderStatus(OrderStatus.NOTPAY.getType());

//      TODO 将订单信息存入数据库


//      TODO 调用统一下单API
        //请求URL
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/native");

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

        log.info("Native统一支付下单请求数据 - " + JSONObject.toJSONString(paramsMap));

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
            httpClient.close();
        }

        //将相应结果转换成JSON
        JSONObject responseJson = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));

        HashMap<String, Object> returnMap = new HashMap<>();
        returnMap.put("codeUrl",responseJson.get("code_url"));
        return returnMap;
    }
}
