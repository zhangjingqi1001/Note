package com.payment.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.payment.entity.OrderInfo;
import com.payment.entity.RefundInfo;
import com.payment.enums.wxpay.WxRefundStatus;
import com.payment.mapper.RefundInfoMapper;
import com.payment.service.OrderInfoService;
import com.payment.service.RefundInfoService;
import com.payment.util.OrderNoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@Service
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 根据订单号创建退款订单
     * @param orderNo
     * @return
     */
    @Override
    public RefundInfo createRefundByOrderNo(String orderNo, String reason) {

        //根据订单号获取订单信息
        OrderInfo orderInfo = orderInfoService.getOrderByOrderNo(orderNo);

        //根据订单号生成退款订单
        RefundInfo refundInfo = new RefundInfo();
        refundInfo.setOrderNo(orderNo);//订单编号
        refundInfo.setRefundNo(OrderNoUtils.getRefundNo());//退款单编号
        refundInfo.setTotalFee(orderInfo.getTotalFee());//原订单金额(分)
        refundInfo.setRefund(orderInfo.getTotalFee());//退款金额(分)
        refundInfo.setReason(reason);//退款原因

        //保存退款订单
        baseMapper.insert(refundInfo);

        return refundInfo;
    }

    /**
     * 记录退款记录
     * @param content
     */
    @Override
    public void updateRefund(String content) {

        //将json字符串转换成Map
        HashMap resultMap = JSONObject.parseObject(content, HashMap.class);

        //根据退款单编号修改退款单
        QueryWrapper<RefundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("refund_no", resultMap.get("out_refund_no"));

        //设置要修改的字段
        RefundInfo refundInfo = new RefundInfo();

        refundInfo.setRefundId( resultMap.get("refund_id").toString());//微信支付退款单号

        //查询退款和申请退款中的返回参数
        if(resultMap.get("status") != null){
            refundInfo.setRefundStatus( resultMap.get("status").toString());//退款状态
            refundInfo.setContentReturn(content);//将全部响应结果存入数据库的content字段
        }
        //退款回调中的回调参数
        if(resultMap.get("refund_status") != null){
            refundInfo.setRefundStatus(resultMap.get("refund_status").toString());//退款状态
            refundInfo.setContentNotify(content);//将全部响应结果存入数据库的content字段
        }

        //更新退款单
        baseMapper.update(refundInfo, queryWrapper);
    }

    /**
     * 找出申请退款超过minutes分钟并且未成功的退款单
     * @param minutes
     * @return
     */
    @Override
    public List<RefundInfo> getNoRefundOrderByDuration(int minutes) {

        //minutes分钟之前的时间
        Instant instant = Instant.now().minus(Duration.ofMinutes(minutes));

        QueryWrapper<RefundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("refund_status", WxRefundStatus.PROCESSING.getType());
        queryWrapper.le("create_time", instant);
        List<RefundInfo> refundInfoList = baseMapper.selectList(queryWrapper);
        return refundInfoList;
    }

    @Override
    public RefundInfo createRefundByOrderNoForAliPay(String orderNo, String reason) {
        //根据订单号获取订单信息
        OrderInfo orderInfo = orderInfoService.getOrderByOrderNo(orderNo);

        //根据订单号生成退款订单
        RefundInfo refundInfo = new RefundInfo();
        refundInfo.setOrderNo(orderNo);//商户订单编号
        refundInfo.setRefundNo(OrderNoUtils.getRefundNo());//商户退款单编号

//      针对退款，我们的退款金额是可以小于原订单金额的，就是退一部分。也就是说一笔订单可以发起多笔退款
//      只要多笔退款的总金额不要多于原订单金额即可，在支付宝当中都是允许的
//      全额退款再支付宝trade_status中是TRADE_CLOSED（未付款交易超时关闭或支付完成后全额退款）
        refundInfo.setTotalFee(orderInfo.getTotalFee());//原订单金额(分)
        refundInfo.setRefund(orderInfo.getTotalFee());//退款金额(分)
        refundInfo.setReason(reason);//退款原因

        //保存退款订单
        baseMapper.insert(refundInfo);

        return refundInfo;
    }

    /**
     * 更新退款记录
     * @param refundNo
     * @param content
     * @param refundStatus
     */
    @Override
    public void updateRefundForAliPay(String refundNo, String content, String refundStatus) {

        //根据退款单编号修改退款单
        QueryWrapper<RefundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("refund_no", refundNo);

        //设置要修改的字段
        RefundInfo refundInfo = new RefundInfo();
        refundInfo.setRefundStatus(refundStatus);//退款状态
        refundInfo.setContentReturn(content);//将全部响应结果存入数据库的content字段

        //更新退款单
        baseMapper.update(refundInfo, queryWrapper);

    }
}