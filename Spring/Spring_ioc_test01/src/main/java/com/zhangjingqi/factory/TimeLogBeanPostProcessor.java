package com.zhangjingqi.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;

//public class TimeLogBeanPostProcessor implements BeanPostProcessor {
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        //     使用动态代理对目标Bean进行增强，返回proxy对象，进而存储到单例池当中
//        Object proxyBean = Proxy.newProxyInstance(bean.getClass().getClassLoader(),
//                bean.getClass().getInterfaces(),
//                (Object proxy, Method method, Object[] args) -> {
//                    long start = System.currentTimeMillis();
//                    System.out.println("开始时间：" + new Date(start));
//                    //执行目标方法
//                    Object result = method.invoke(bean, args);
//                    long end = System.currentTimeMillis();
//                    System.out.println("结束时间：" + new Date(end));
//                    return result;
//                });
//        //返回代理对象
//        return proxyBean;
//
//    }
//}
