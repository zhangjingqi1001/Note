package com.zhangjingqi.service.impl;

import com.zhangjingqi.dao.UserDao;
import com.zhangjingqi.dao.impl.UserDaoImpl;
import com.zhangjingqi.service.UserService;
import org.springframework.beans.factory.InitializingBean;

public class UserServiceImpl implements UserService  {

    private UserDao userDao;
    // BeanFactory去调用该方法，从容器中获得userDao设置到此处
    public void setUserDao(UserDao userDao) {
        this.userDao =userDao;
    }

    public UserServiceImpl(){
//        System.out.println("UserServiceImpl实例化 - 无参构造注入");
    }

    public UserServiceImpl(String name){
//        System.out.println("UserServiceImpl实例化 - 有参构造注入，name="+name);
    }
    public UserServiceImpl(String name,int age){
//        System.out.println("UserServiceImpl实例化 - 有参构造注入，name="+name+",age="+age);
    }

}
