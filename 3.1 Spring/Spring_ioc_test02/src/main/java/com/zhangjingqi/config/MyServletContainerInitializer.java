package com.zhangjingqi.config;



import com.zhangjingqi.service.QuickService;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.Set;
@HandlesTypes({QuickService.class})
public class MyServletContainerInitializer implements ServletContainerInitializer {


    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        System.out.println("MyServletContainerInitializer running ......");
    }
}