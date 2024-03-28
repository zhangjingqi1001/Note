package com.zhangjingqi.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.zhangjingqi.config.SpringConfig;
import com.zhangjingqi.dao.UserDao;
import com.zhangjingqi.dao.impl.UserDaoImpl;

import com.zhangjingqi.service.UserService;
import com.zhangjingqi.service.impl.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.applet.AppletContext;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplicationContextTest {
    public static void main(String[] args) {
        //参数是一个xml配置文件
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans2.xml");
        UserService bean = applicationContext.getBean(UserService.class);
        bean.show1();
//        注解方式加载Spring核心配置
//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
//        Object otherBean2 = applicationContext.getBean("otherBean2");
//        System.out.println(otherBean2);
//        Object springConfig = applicationContext.getBean("springConfig");
//        Object userDao = applicationContext.getBean("dataSource");
//        System.out.println(springConfig);
//        System.out.println(userDao);
//        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");

    }
}
