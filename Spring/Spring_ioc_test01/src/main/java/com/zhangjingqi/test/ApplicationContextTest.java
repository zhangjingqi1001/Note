package com.zhangjingqi.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.applet.AppletContext;

public class ApplicationContextTest {
    public static void main(String[] args) {
        //参数是一个xml配置文件
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        Object userDao2 = applicationContext.getBean("userDao2");
        System.out.println("userDao2 = "+userDao2);
    }
}
