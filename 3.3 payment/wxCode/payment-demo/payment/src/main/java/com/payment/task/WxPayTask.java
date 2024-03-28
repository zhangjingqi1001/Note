package com.payment.task;

import com.payment.entity.OrderInfo;
import com.payment.service.OrderInfoService;
import com.payment.service.WxPayService;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

//希望程序启动的时候能自动初始化出来
@Component
@Slf4j
public class WxPayTask {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private WxPayService wxPayService;

    /**
     * 从第0秒开始，每隔30秒执行查单一次，查询创建超过5分钟并且未支付的订单
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void orderConfirm() throws IOException {
        log.info("微信支付定时查单");
        //查找超过五分钟未支付的订单
        List<OrderInfo> orderInfoList = orderInfoService.getNoPayOrderByDuration(5);

        //获取商户系统数据库中未支付的超时订单，向微信支付平台发送请求确认是否是未支付
        for (OrderInfo orderInfo : orderInfoList) {
            String orderNo = orderInfo.getOrderNo();
            log.warn("超时订单 - {}",orderNo);

            //核实订单状态，调用微信支付查单接口
            wxPayService.checkOrderStatus(orderNo);
        }
    }

    /**
     * cron表达式由6部分组成，分别是秒、分、时、日、月、周。
     * 其中日和周是互斥的，不能同时指定，指定其中一个则另一个设置为“？”即可。
     * 假如“1-3”在秒位置，则表示从第1秒开始执行，到第3秒结束执行
     * 假如“0/3”在秒位置，则表示第0秒开始，每隔3秒执行一次
     * 假如“1,2,3”在秒位置，则表示在第1秒、第2秒，第3秒开始执行
     * “？”表示不指定参数
     * “*”表示每秒/分/时....，如果在秒位置就是每秒都执行，如果是在分位置就是每分钟都执行
     * 比如 cron = "* * * * * ?" 表示每个月每日没时每分没秒都要执行这个定时任务
     */
//    @Scheduled(cron = "* * * * * ?")
    public void task1() {
        log.info("task1 被执行");

    }

}
