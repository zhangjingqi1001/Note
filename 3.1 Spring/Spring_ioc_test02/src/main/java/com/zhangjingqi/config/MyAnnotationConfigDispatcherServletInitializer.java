package com.zhangjingqi.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MyAnnotationConfigDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    //返回的带有@Configuration注解的类用来配置ContextLoaderListener
    //提供Spring容器的核心配置类
    protected Class<?>[] getRootConfigClasses() {
        System.out.println("加载核心配置类创建ContextLoaderListener");
        return new Class[]{SpringConfig.class};
    }

    //返回的带有@Configuration注解的类用来配置DispatcherServlet
    //提供SpringMVC容器的核心配置类
    protected Class<?>[] getServletConfigClasses() {
        System.out.println("加载核心配置类创建DispatcherServlet");
        return new Class[]{SpringMVCConfig.class};
    }

    //将一个或多个路径映射到DispatcherServlet上
    //提供前端控制器的映射路径
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}