package com.atguigu.paymentdemo.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.atguigu.paymentdemo.service.AliPayService;
import com.atguigu.paymentdemo.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@CrossOrigin//跨域
@RestController
@RequestMapping("/api/ali-pay")
@Api(tags = "网站支付宝支付")
public class AliPayController {

    @Resource
    private AliPayService aliPayService;

    @Resource
    private Environment config;

    //  传入商品的id进行下单
    @ApiOperation("统一收单下单并支付接口")
    @PostMapping("/trade/page/pay/{productId}")
    public R tradePagePay(@PathVariable("productId") Long productId) {
        log.info("统一收单下单并支付接口");
//      支付宝开放平台接受Request请求对象后，会生成一个html形式的from表单，包含自动提交的脚本
        String formStr = aliPayService.tradeCreate(productId);

        return R.ok().data("formStr", formStr);
    }

    /**
     * @param params 将支付宝的请求参数转换成Map集合
     * @return
     */
    @ApiOperation("支付通知")
    @PostMapping("/trade/notify")//注意要和配置文件中alipay.notify-url一致
    public String tradeNotify(@RequestParam Map<String, String> params) throws AlipayApiException {
        log.info("支付通知");
        log.info("通知参数 - " + params);
        String result = "failure";

//      TODO 1.异步通知的验签
        boolean signVerified = AlipaySignature.rsaCheckV1(params,
//              支付宝公钥
                config.getProperty("alipay.alipay-public-key"),
//              编码方式
                AlipayConstants.CHARSET_UTF8,
//               签名类型
                AlipayConstants.SIGN_TYPE_RSA2); //调用SDK验证签名
        if (!signVerified) {
            //验签失败则记录异常日志，并在response中返回failure.
            log.error("支付成功 - 异步通知验签失败");
            return "failure";
        }
        log.info("支付成功 - 异步验签通知成功");
//      验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure

//      TODO 2.业务内容二次校验

//      TODO 向支付宝返回的成功的结果（通知结果）
        return result;
    }

}
























