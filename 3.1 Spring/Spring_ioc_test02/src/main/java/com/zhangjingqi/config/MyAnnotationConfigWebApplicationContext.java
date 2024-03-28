package com.zhangjingqi.config;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class MyAnnotationConfigWebApplicationContext extends AnnotationConfigWebApplicationContext {
    public MyAnnotationConfigWebApplicationContext() {
//      注册核心配置类
        super.register(SpringMVCConfig.class);
    }
}