package com.atguigu.paymentdemo.task;

import com.atguigu.paymentdemo.entity.OrderInfo;
import com.atguigu.paymentdemo.enums.PayType;
import com.atguigu.paymentdemo.service.AliPayService;
import com.atguigu.paymentdemo.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component//定时任务要作为一个组件在Spring容器当中创建
public class AliPayTask {

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private AliPayService aliPayService;

    /**
     * 从第0秒开始，每间隔30秒执行1次，查询创建超过5分钟并且未支付的订单
     * 秒 分 时 日 月 周
     * 以秒为例
     * *：每秒都执行
     * 1-3：从第1秒开始执行，到第3秒结束执行
     * 0/3：从第0秒开始，每隔3秒执行1次
     * 1,2,3：在指定的第1、2、3秒执行
     * ?：不指定
     * 日和周不能同时制定，指定其中之一，则另一个设置为?
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void  orderConfirm() throws Exception{
        log.info("aliPay orderConfirm 被执行");
//      获取超过5分钟没有支付的订单
        List<OrderInfo> orderInfoList =  orderInfoService.getNoPayOrderByDuration(5, PayType.ALIPAY.getType());

        for (OrderInfo orderInfo : orderInfoList) {
            String orderNo = orderInfo.getOrderNo();
            log.warn("超时订单 ===> {}", orderNo);

            // 核实订单状态：并调用支付宝查单接口
            aliPayService.checkOrderStatus(orderNo);
        }
    }


}
