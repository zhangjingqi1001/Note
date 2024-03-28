package com.aliyun.oss;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//底层封装饿了@Import注解
//@EnableConfigurationProperties(读取配置文件的类名.class,假设为AliOSSProperties.class ) //使用properties/yaml配置文件的形式读取配置信息可以直接这么创建
// 自动配置类
@Configuration
public class AliOSSAutoConfiguration {
    @Bean
    public AliOSSUtils aliOSSUtils(){
//      因为这里我没有使用properties/yaml配置文件的形式读取配置信息可以直接这么创建
        return  new AliOSSUtils();

    }

//  因为使用了@EnableConfigurationProperties(AliOSSProperties.class )AliOSSProperties类已经成为IOC的Bean了

//   下面方法的参数可以直接使用AliOSSProperties,因为它会自动根据类型进行装配

//  使用配置文件注入的
//    @Bean
//    public AliOSSUtils aliOSSUtilsProperties(AliOSSProperties aliOSSProperties){
//      如果使用了配置文件之后，也是不可以自动注入的，我们这里就要改成
//     AliOSSUtils aliOSSUtils = new AliOSSUtils();
//     aliOSSUtils.setAliOSSProperties(aliOSSProperties);
//      return aliOSSUtils;
//    }
}
