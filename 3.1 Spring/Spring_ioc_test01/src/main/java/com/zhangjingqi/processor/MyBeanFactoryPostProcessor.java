package com.zhangjingqi.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    //方法对应的参数就是BeanFactory
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        System.out.println("beanDefinitionMap填充完毕后回调该方法...");
//        //修改某一个beanDefinition
//        // 说明：为了安全起见，并没有给我们提供getBeanDefinitionMap方法
//        //      但是允许我们根据名字取获取某一个beanDefinition
//        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");
//        //userService全限定名是com.zhangjingqi.service.impl.UserServiceImpl
//        //我们现在要改成com.zhangjingqi.dao.impl.UserDaoImpl
//        beanDefinition.setBeanClassName("com.zhangjingqi.dao.impl.UserDaoImpl");
//
//    }


    //方法对应的参数就是BeanFactory
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("beanDefinitionMap填充完毕后回调该方法...");
//      注册BeanDefinition
        BeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClassName("com.zhangjingqi.dao.impl.PersonDaoImpl");

//      ConfigurableListableBeanFactory内部没有注册BeanDefinition
//      beanFactory.registerSingleton();此方法是其放入到单例池当中
//      需要把ConfigurableListableBeanFactory类型强转为子类DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)beanFactory;
        defaultListableBeanFactory.registerBeanDefinition("personDao",beanDefinition);
    }
}
