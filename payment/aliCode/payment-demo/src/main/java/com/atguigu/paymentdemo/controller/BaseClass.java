package com.atguigu.paymentdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseClass {

    private  Class clazz;

    public static  Logger log;

    public BaseClass() {
//      初始化当前类对象
        clazz = this.getClass();
//      初始化log对象
        log = LoggerFactory.getLogger(BookController.class);
    }

}
