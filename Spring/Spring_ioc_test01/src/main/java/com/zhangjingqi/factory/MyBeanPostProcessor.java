package com.zhangjingqi.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor {
//  先执行
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(bean+":postProcessBeforeInitialization:"+beanName);
        return bean;
    }

//  后执行
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(bean+":postProcessAfterInitialization:"+beanName);
        return bean;
    }
}
