package com.atguigu.paymentdemo.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.atguigu.paymentdemo.entity.OrderInfo;
import com.atguigu.paymentdemo.service.AliPayService;
import com.atguigu.paymentdemo.service.OrderInfoService;
import com.atguigu.paymentdemo.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @Resource
    private OrderInfoService orderInfoService;

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

//      1.异步通知的验签
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
            return result;//failure
        }
        log.info("支付成功 - 异步验签通知成功");
//      验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure

//      2.业务内容二次校验
//      2.1 商家需要验证该通知数据中的 out_trade_no 是否为商家系统中创建的订单号。
//      将此订单去数据库中查询，如果查询到是存在的，那就是我们商户系统中的订单；如果查询不到的话，说明不是我们商户系统中的订单
        String outTradeNo = params.get("out_trade_no");
        OrderInfo order = orderInfoService.getOrderByOrderNo(outTradeNo);
        if (order == null) {
            log.info("订单不存在");
            return result;//failure
        }
//      2.2 判断 total_amount 是否确实为该订单的实际金额（即商家订单创建时的金额）
//      这个地方注意，商户系统中是以分为单位，但是支付宝中是以元为单位
        String totalAmount = params.get("total_amount");
//      将支付宝给我们的金额乘100转换成分，与商户系统中的金额进行对比
        int totalAmountInt = new BigDecimal(totalAmount).multiply(new BigDecimal("100")).intValue();//乘100
        int totalFeeInt = order.getTotalFee();
        if (totalAmountInt != totalFeeInt) {
            log.error("金额校验失败");
            return result;//failure
        }
//      2.3 校验通知中的 seller_id（或者 seller_email) 是否为 out_trade_no 这笔单据的对应的操作方（有的时候，一个商家可能有多个 seller_id/seller_email）
//      这个地方不一致的话，说明你付给别人了
        String sellerId = params.get("seller_id");//商户PID，我们的是2088721011169741
        String sellerIdProperty = config.getProperty("alipay.seller-id");
        if (!sellerId.equals(sellerIdProperty)) {
            log.error("商家PID校验失败");
            return result;//failure
        }
//      2.4 验证 app_id 是否为该商家本身
        String appId = params.get("app_id");
        String appIdProperty = config.getProperty("alipay.app-id");
        if (!appId.equals(appIdProperty)) {
            log.error("AppID校验失败");
            return result;//failure
        }
//      2.5在支付宝的业务通知中，只有交易通知状态为 TRADE_SUCCESS 或 TRADE_FINISHED 时，支付宝才会认定为买家付款成功
//      在这个地方我们只判断TRADE_SUCCESS即可，因为TRADE_FINISHED是不能退款的，但是我们以后是可以退款的
        String tradeStatus = params.get("trade_status");
        if (!"TRADE_SUCCESS".equals(tradeStatus)) {
            log.error("支付未成功");
            return result;//failure
        }

//      3.处理业务、修改订单状态、记录支付日志
//      校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
        aliPayService.processOrder(params);//将所有的回调参数传入


//      向支付宝返回的成功的结果（通知结果）
        return result;
    }


    /***
     * 用户取消订单
     */
    @ApiOperation("用户取消订单")
    @PostMapping("/trade/close/{orderNo}")
    public R cancel(@PathVariable String orderNo) {
        log.info("用户取消订单");
        aliPayService.cancelOrder(orderNo);
        return R.ok().setMessage("订单已取消");
    }

    /**
     * @param orderNo
     * @return
     */
    @ApiOperation("查询订单")
    @GetMapping("/trade/query/{orderNo}")
    public R queryOrder(@PathVariable String orderNo) {
        log.info("查询订单");
        String result =  aliPayService.queryOrder(orderNo);
        return R.ok().setMessage("查询成功").data("result",result);
    }


    /**
     * 用户申请退款
     * @param orderNo  商户订单号
     * @param reason   退款原因
     * @return
     */
    @ApiOperation("申请退款")
    @PostMapping("/trade/refund/{orderNo}/{reason}")
    public R refunds(@PathVariable String orderNo,@PathVariable String reason){
        log.info("申请退款");

        aliPayService.refund(orderNo,reason);

        return R.ok();
    }

    /**
     * 查询退款
     * @param orderNo
     * @return
     * @throws Exception
     */
    @ApiOperation("查询退款：测试用")
    @GetMapping("/trade/fastpay/refund/{orderNo}")
    public R queryRefund(@PathVariable String orderNo) throws Exception {

        log.info("查询退款");

        String result = aliPayService.queryRefund(orderNo);
        return R.ok().setMessage("查询成功").data("result", result);
    }


    /**
     * 获取账单
     * @param billDate 账单的日期
     * @param type 账单的类型
     * @return
     */
    @ApiOperation("获取账单url")
    @GetMapping("/bill/downloadurl/query/{billDate}/{type}")
    public R queryTradeBill(@PathVariable String billDate,@PathVariable String type){
        log.info("获取账单url");
        String downloadUrl = aliPayService.queryBill(billDate,type);

        return R.ok().setMessage("获取账单url成功").data("downloadUrl", downloadUrl);
    }
}
























