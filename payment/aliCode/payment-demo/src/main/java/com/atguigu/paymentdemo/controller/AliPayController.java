package com.atguigu.paymentdemo.controller;

import com.atguigu.paymentdemo.service.AliPayService;
import com.atguigu.paymentdemo.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@CrossOrigin//跨域
@RestController
@RequestMapping("/api/ali-pay")
@Api(tags = "网站支付宝支付")
public class AliPayController {

    @Resource
    private AliPayService aliPayService;

    //  传入商品的id进行下单
    @PostMapping("/trade/page/pay/{productId}")
    @ApiOperation("统一收单下单并支付接口")
    public R tradePagePay(@PathVariable("productId") Long productId) {
        log.info("统一收单下单并支付接口");
//      支付宝开放平台接受Request请求对象后，会生成一个html形式的from表单，包含自动提交的脚本
        String formStr = aliPayService.tradeCreate(productId);

        return R.ok().data("formStr",formStr);
    }
}
























