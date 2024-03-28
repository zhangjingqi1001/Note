package com.zhangjingqi.imports;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

//public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
//    @Override
//    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
////      向容器当中注入BeanDefinition
//        BeanDefinition beanDefinition = new RootBeanDefinition();
//        beanDefinition.setBeanClassName(OtherBean2.class.getName());
//        registry.registerBeanDefinition("otherBean2",beanDefinition);
//    }
//}
