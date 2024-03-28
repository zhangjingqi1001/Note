package com.zhangjingqi.config;

import com.zhangjingqi.Interceptor.MyInterceptor01;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// 覆盖我们分析的两个方法
@Component
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//     开启DefaultServlet，可以处理静态资源了
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//      创建拦截器对象，进行注册
//      Interceptor的执行顺序也取决于添加顺序
        registry.addInterceptor(new MyInterceptor01())
                .addPathPatterns("/*");
    }
}