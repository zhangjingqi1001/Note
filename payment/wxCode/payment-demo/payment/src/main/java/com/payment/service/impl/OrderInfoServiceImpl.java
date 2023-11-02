package com.payment.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.payment.entity.OrderInfo;
import com.payment.mapper.OrderInfoMapper;
import com.payment.service.OrderInfoService;
import org.springframework.stereotype.Service;

@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {


}
