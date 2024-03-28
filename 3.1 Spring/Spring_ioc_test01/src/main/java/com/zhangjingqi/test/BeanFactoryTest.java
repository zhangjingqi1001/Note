package com.zhangjingqi.test;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

public class BeanFactoryTest {
    public static void main(String[] args) {
//      创建一个工厂对象
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//      创建一个读取器（读取xml文件）
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
//      读取器读取配置文件,并给工厂
        reader.loadBeanDefinitions("beans.xml");
//      根据id获取Bean实例对象
        Object userService = beanFactory.getBean("userService");
//        System.out.println(userService);
//        Object userDao = beanFactory.getBean("userDao");
//        System.out.println(userDao);
    }

}
