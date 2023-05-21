package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeaderConfig {

    //  方式一：name 指定全类名
//  方式名：value 指定Class文件
//  会判断是否存在io.jsonwebtoken.Jwts类，如果存在则会将Bean注入IOC容器
//    @ConditionalOnClass(name = "io.jsonwebtoken.Jwts")
    // @ConditionalOnMissingBean若不指定参数代表 当前环境没有该类型（该类型在这里指的是HeaderParser）的Bean就创建一个
//    @ConditionalOnMissingBean
//  name指定配置文件中配置项的名称，value指定配置项的值
//  会判断配置文件中是否存在指定属性与值，如果都存在才会将Bean加载到IOC容器
    @ConditionalOnProperty(name = "name",havingValue = "zhangjingqi")
    @Bean
    public HeaderParser headerParser(){
        return new HeaderParser();
    }

    @Bean
    public HeaderGenerator headerGenerator(){
        return new HeaderGenerator();
    }
}
