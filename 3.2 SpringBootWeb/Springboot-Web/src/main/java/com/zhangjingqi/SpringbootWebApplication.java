package com.zhangjingqi;

import com.example.EnableHeaderConfig;
import com.example.HeaderConfig;
import com.example.MyImportSelector;
import com.example.TokenParser;
import org.dom4j.io.SAXReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

//Filter是javaweb三大组件之一，不是Spring提供的，如果想要使用三大组件，需要添加这个注解
@ServletComponentScan
//@ComponentScan({"com.example","com.zhangjingqi"})

// 说明： TokenParser加不加@Component注解无所谓，都会注入到IOC容器中
//@Import({HeaderConfig.class}) // 参数是一个数组
//@Import({MyImportSelector.class})
@EnableHeaderConfig
@SpringBootApplication
public class SpringbootWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWebApplication.class, args);
    }
    //声明第三方bean
    @Bean //将当前方法的返回值对象交给IOC容器管理, 成为IOC容器bean
    public SAXReader saxReader(){
        return new SAXReader();
    }
}
