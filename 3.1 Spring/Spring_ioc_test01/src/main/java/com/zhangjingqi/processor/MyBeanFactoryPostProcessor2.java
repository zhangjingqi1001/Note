package com.zhangjingqi.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class MyBeanFactoryPostProcessor2 implements BeanDefinitionRegistryPostProcessor {

//  之前接口的方法
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println("MyBeanFactoryPostProcessor2的postProcessBeanFactory方法");
    }

//   BeanDefinitionRegistryPostProcessor接口的方法，专门用于注册BeanDefinition操作
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry)
            throws BeansException {
        System.out.println("MyBeanFactoryPostProcessor2的postProcessBeanDefinitionRegistry");

        BeanDefinition beanDefinition = new RootBeanDefinition();

        beanDefinition.setBeanClassName("com.zhangjingqi.dao.impl.UserDaoImpl");

        beanDefinitionRegistry.registerBeanDefinition("userDao2", beanDefinition);
    }
}