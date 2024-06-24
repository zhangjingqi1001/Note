package com.payment.controller;

import com.payment.entity.OrderInfo;
import com.payment.enums.OrderStatus;
import com.payment.service.OrderInfoService;
import com.payment.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin //开放前端的跨域访问
@Api(tags = "商品订单管理")
@RestController
@RequestMapping("/api/order-info")
public class OrderInfoController {

    @Resource
    private OrderInfoService orderInfoService;

    @ApiOperation("订单列表")
    @GetMapping("/list")
    public R list(){

        List<OrderInfo> list = orderInfoService.listOrderByCreateTimeDesc();
        return R.ok().data("list", list);
    }

    /**
     * 查询本地订单状态
     * @param orderNo
     * @return
     */
    @ApiOperation("查询本地订单状态")
    @GetMapping("/query-order-status/{orderNo}")
    public R queryOrderStatus(@PathVariable String orderNo){

        String orderStatus = orderInfoService.getOrderStatus(orderNo);
        if(OrderStatus.SUCCESS.getType().equals(orderStatus)){
            return R.ok().setMessage("支付成功"); //支付成功
        }
//      支付中的代码是101，前端会对101状态码进行判断。
//      如果前端获取到的状态码是101的话，就会执行定时查单的任务，一直到状态码等于0为止，就可以做页面的跳转了
        return R.ok().setCode(101).setMessage("支付中......");
    }



}