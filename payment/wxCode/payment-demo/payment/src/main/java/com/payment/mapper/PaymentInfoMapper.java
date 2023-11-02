package com.payment.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.payment.entity.PaymentInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentInfoMapper extends BaseMapper<PaymentInfo> {
}
