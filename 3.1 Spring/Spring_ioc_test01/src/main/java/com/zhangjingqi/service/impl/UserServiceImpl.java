package com.zhangjingqi.service.impl;

import com.zhangjingqi.dao.UserDao;

import com.zhangjingqi.service.UserService;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

@Service("userService")
public class UserServiceImpl implements UserService{

    @Override
    public void show1() {
        System.out.println("show1......");
    }

    @Override
    public void show2() {
        System.out.println("show2.......");
    }
}
