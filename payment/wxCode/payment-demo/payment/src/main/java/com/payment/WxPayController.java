package com.payment;

import com.payment.service.WxPayService;
import com.payment.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@CrossOrigin //跨域注解
@RestController
@RequestMapping("/api/wx-pay")
@Api(tags = "微信支付")
public class WxPayController {

    @Autowired
    private WxPayService wxPayService;

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

}
