package com.zhangjingqi.dao.impl;

import com.zhangjingqi.dao.UserDao;
import org.springframework.beans.factory.InitializingBean;

public class UserDaoImpl implements UserDao, InitializingBean {
    public UserDaoImpl() {
        System.out.println("userDao实例化");
    }

    public void init() {
        System.out.println("init 初始化方法开始执行");
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }
}
