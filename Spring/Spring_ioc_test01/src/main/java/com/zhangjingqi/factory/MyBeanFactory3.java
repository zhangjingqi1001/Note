package com.zhangjingqi.factory;

import com.zhangjingqi.dao.UserDao;
import com.zhangjingqi.dao.impl.UserDaoImpl;
import org.springframework.beans.factory.FactoryBean;

public class MyBeanFactory3 implements FactoryBean<UserDao> {

//  返回的Bean是谁
    public UserDao getObject() throws Exception {
        return new UserDaoImpl();
    }

//  返回Bean的类型是什么
    public Class<?> getObjectType() {
        return UserDao.class;
    }
}
