package com.atguigu.paymentdemo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.paymentdemo.entity.PaymentInfo;
import com.atguigu.paymentdemo.enums.PayType;
import com.atguigu.paymentdemo.mapper.PaymentInfoMapper;
import com.atguigu.paymentdemo.service.PaymentInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

    /**
     * 记录支付日志
     * @param plainText
     */
    @Override
    public void createPaymentInfo(String plainText) {

        log.info("记录支付日志");

        Gson gson = new Gson();
        HashMap plainTextMap = gson.fromJson(plainText, HashMap.class);

        //订单号
        String orderNo = (String)plainTextMap.get("out_trade_no");
        //业务编号
        String transactionId = (String)plainTextMap.get("transaction_id");
        //支付类型
        String tradeType = (String)plainTextMap.get("trade_type");
        //交易状态
        String tradeState = (String)plainTextMap.get("trade_state");
        //用户实际支付金额
        Map<String, Object> amount = (Map)plainTextMap.get("amount");
        Integer payerTotal = ((Double) amount.get("payer_total")).intValue();

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderNo(orderNo);
        paymentInfo.setPaymentType(PayType.WXPAY.getType());
        paymentInfo.setTransactionId(transactionId);
        paymentInfo.setTradeType(tradeType);
        paymentInfo.setTradeState(tradeState);
        paymentInfo.setPayerTotal(payerTotal);
        paymentInfo.setContent(plainText);

        baseMapper.insert(paymentInfo);
    }

    /**
     * 记录支付宝支付日志
     */
    @Transactional
    @Override
    public void createPaymentInfoForAliPay(Map<String, String> params) {
        log.info("记录支付日志");
//      获取订单号
        String orderNo = params.get("out_trade_no");
//      支付系统交易编号(支付宝交易凭证号)
        String transactionId = params.get("trade_no");
//      交易状态
        String tradeStatus = params.get("trade_status");
//      交易总金额
        String totalAmount = params.get("total_amount");
        int totalAmountInt = new BigDecimal(totalAmount).multiply(new BigDecimal("100")).intValue();

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderNo(orderNo);
        paymentInfo.setPaymentType(PayType.ALIPAY.getType());
        paymentInfo.setTransactionId(transactionId);
        paymentInfo.setTradeType("电脑网站支付");
        paymentInfo.setTradeState(tradeStatus);
        paymentInfo.setPayerTotal(totalAmountInt);

        String paymentInfoJSON = JSONObject.toJSONString(params);
        paymentInfo.setContent(paymentInfoJSON);//通知参数
//      插入数据库
        baseMapper.insert(paymentInfo);
    }
}
