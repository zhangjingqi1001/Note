package com.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.payment.entity.RefundInfo;

import java.util.List;

public interface RefundInfoService extends IService<RefundInfo> {
    RefundInfo createRefundByOrderNo(String orderNo, String reason);

    void updateRefund(String content);

    List<RefundInfo> getNoRefundOrderByDuration(int minutes);

    RefundInfo createRefundByOrderNoForAliPay(String orderNo, String reason);

    void updateRefundForAliPay(String refundNo, String content, String refundStatus);
}
