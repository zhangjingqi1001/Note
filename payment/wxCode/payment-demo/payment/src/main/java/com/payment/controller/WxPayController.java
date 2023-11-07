package com.payment.controller;

import com.alibaba.fastjson2.JSONObject;
import com.payment.service.WxPayService;
import com.payment.util.HttpUtils;
import com.payment.util.WechatPay2ValidatorForRequest;
import com.payment.vo.R;
import com.wechat.pay.contrib.apache.httpclient.auth.ScheduledUpdateCertificatesVerifier;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin //跨域注解
@RestController
@RequestMapping("/api/wx-pay")
@Api(tags = "微信支付")
public class WxPayController {

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private ScheduledUpdateCertificatesVerifier verifier;

    @ApiOperation("调用统一下单API，生成支付二维码")
    @PostMapping("native/{productId}")
    public R nativePay(@PathVariable Long productId) throws Exception {
        log.info("调用统一下单API - productId - " + productId);

        //Map集合要返回支付二维码连接和订单号
        Map<String, Object> map = wxPayService.nativePay(productId);
        //我们在R类上添加了@Accessors(chain = true)注解，表示可以链式操作
        //链式操作有什么好处/作用？ 可以链式操作 R.ok().setData(map).setCode(); 并且这样后Set方法也会给我们返回一个R对象，更方便
        return R.ok().setData(map);
    }

    /**
     * @param request  微信中的请求是在HttpServletRequest里面
     * @param response 要给微信的服务器返回响应
     * @return 因为通知的接口要求响应的是JSON字符串的格式
     */
    @PostMapping("/native/notify")
    public String nativeNotify(HttpServletRequest request, HttpServletResponse response) throws IOException, GeneralSecurityException {

        //TODO 处理通知参数
        //自己封装的工具类处理通知参数,最后会得到一个字符串形式的通知参数
        String body = HttpUtils.readData(request);
        //转换成JSON类型
        JSONObject bodyJson = JSONObject.parseObject(body);
        log.info("通知唯一id - " + bodyJson.getString("id"));//通知唯一id - cb0f0048-ddc2-5f1d-a516-ec307a16c40c
//      ciphertext里面是加密的数据，我们验签之后再进行解密
        log.info("通知的完整数据 - " + body);//通知的完整数据 - {"id":"cb0f0048-ddc2-5f1d-a516-ec307a16c40c","create_time":"2023-11-05T17:35:22+08:00","resource_type":"encrypt-resource","event_type":"TRANSACTION.SUCCESS","summary":"支付成功","resource":{"original_type":"transaction","algorithm":"AEAD_AES_256_GCM","ciphertext":"/VgIvB6fU1CJK8GLRCv/hVfaV2T3/nTTTdApaNu4HAidhB+KG9z0Zb9l1utkAJK6GAXfiXTvvcSvTX6/4vyyt8ob4PtArElMN5wHbgPJvZecvA8UFqvLdK84sCaCrPdZognImMEu3pnIBA4tQZWNUQOoFVAGWJ6vetrK6+KT1L5lqgr4Pvpgwa6GSsmS44Fxw1L31Sj2EQYmRWWf4FZSCmo1t+mmWYluB/Gk0CFXLyk1orAMSEat7+TXxg/3AQGIEco4nASl8Ox0xK8LV9x6lEbd90XsdMpGPS4TFqwxLHwip/aLsteYN0BuMsbmFOoU9zPTAa4sRa5/+CMPQEQiD57YCeeWiJagQPASOjYmrFCQy5j8IL8WAP+NVDGDphvtu8S3ZrRRMNQNju6vBAnGzlr46P6meLBqlDg/zJJixwKrmli1ZZdJmJJlcn0U85GiXqjFa98Y1NY+9uqs3CqLkct8O+ilrsF8JmHkKvqr/UG04XtyX6RmsK/MRR5ksYOw4lC53roXUK29gPOLsFMBkdcNNPMaqzqF3REbHwbrA1MVQLFADx3r+jmjciWub9oMl+CnRqhvSQ==","associated_data":"transaction","nonce":"kO0hZuBY9B1H"}}


        //TODO 验签
        WechatPay2ValidatorForRequest wechatPay2ValidatorForRequest = new WechatPay2ValidatorForRequest(verifier, bodyJson.getString("id"), body);
        if (!wechatPay2ValidatorForRequest.validate(request)) {
//          验签不通过，返回一个失败的应答
            log.info("通知验签失败");
            Map<String, String> map = new HashMap<>();
            map.put("code", "FAIL");
            map.put("message", "失败");
            return JSONObject.toJSONString(map);
        }
        log.info("通知验签成功");
        //TODO 处理订单
        // bodyJson是微信平台给我们的参数
        wxPayService.processOrder(bodyJson);


        //TODO 向微信支付平台应答
        //接收成功： HTTP应答状态码需返回200或204，无需返回应答报文
        response.setStatus(200);
        //接收失败： HTTP应答状态码需返回5XX或4XX，同时需返回应答报文
//        Map<String,String> map  = new HashMap<>();
//        map.put("code","FAIL");
//        map.put("message","失败");

//      成功的时候什么也不返回，只要保证状态码是200或204
        return null;
    }

    /**
     * 用户取消订单
     *
     * @param orderNo 要取消的订单的订单号
     * @return
     */
    @PostMapping("/cancel/{orderNo}")
    public R cancel(@PathVariable String orderNo) throws IOException {
        log.info("取消订单");
        wxPayService.cancelOrder(orderNo);

        return R.ok();
    }


    /**
     * 测试微信查单功能
     */
    @GetMapping("/query/{orderNo}")
    public R queryOrder(@PathVariable String orderNo) throws IOException {
        log.info("查询订单");
        String result = wxPayService.queryOrder(orderNo);
        return R.ok().setMessage("查询成功").data("result",result);
    }

}
