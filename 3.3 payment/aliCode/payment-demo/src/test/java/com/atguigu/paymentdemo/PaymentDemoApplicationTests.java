package com.atguigu.paymentdemo;

import com.atguigu.paymentdemo.config.WxPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.security.PrivateKey;

@Slf4j
@SpringBootTest
class PaymentDemoApplicationTests {

    @Resource
    private WxPayConfig wxPayConfig;

    @Resource
    private CloseableHttpClient wxPayClient;

    /**
     * 获取商户的私钥
     */
    @Test
    void testGetPrivateKey() {

        //获取私钥路径
        String privateKeyPath = wxPayConfig.getPrivateKeyPath();

        //获取私钥
        PrivateKey privateKey = wxPayConfig.getPrivateKey(privateKeyPath);

        System.out.println(privateKey);

    }

    @Resource
    private Environment config;

    @Test
    void getAliProperties(){
        log.info(config.getProperty("alipay.app-id"));
    }

}
