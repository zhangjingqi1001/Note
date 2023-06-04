package com.zhangjingqi.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.zhangjingqi.dao.impl.UserDaoImpl;
import com.zhangjingqi.service.UserService;
import com.zhangjingqi.service.impl.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.applet.AppletContext;

public class ApplicationContextTest {
    public static void main(String[] args) {


        //参数是一个xml配置文件
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        Object dataSource = applicationContext.getBean("dataSource");
        System.out.println(dataSource);


    }
}
