package com.atguigu.paymentdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:alipay-sandbox.properties")
public class AliPayClientConfig {

}
