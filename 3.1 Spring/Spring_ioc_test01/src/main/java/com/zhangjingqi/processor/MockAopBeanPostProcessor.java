package com.zhangjingqi.processor;

import com.zhangjingqi.advice.MyAdvice;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


//BeanPostProcessor：Bean后处理器，一般在Bean实例化之后，填充到单例池singletonObjects之前执行
public class MockAopBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//      目的： 对UserServiceImpl中的show1和show2方法进行增强，增强方法存在于MyAdvice中
//      问题： 1.筛选出UserServiceImpl 或者是service.impl包下的所有类的所有方法都可以增强
//              如果这个地方不筛选的话，所有的类的方法都会增强，这显然不是我们的目的
//            解决方案： 使用if...else 判断一下就可以了
//            2. MyAdvice怎么获取？
//            解决方案： 可以将MyAdvice存入容器
//        bean.getClass().getPackage().equals("com.zhangjingqi.service.impl")  这样是错误的getPackage方法返回的是一个对象
        if (bean.getClass().getPackage().getName().equals("com.zhangjingqi.service.impl")) {
//          TODO 生成Bean的Proxy对象
//          参数一： 类加载器
//          参数二：它实现的接口
//          参数三： new InvocationHandler()
            Object beanProxy = Proxy.newProxyInstance(bean.getClass().getClassLoader(),
                    bean.getClass().getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                           TODO 增强对象的before方法
                            MyAdvice myAdvice = applicationContext.getBean(MyAdvice.class);
                            myAdvice.beforeAdvice();
                            //TODO 执行目标对象的目标方法
                            //  参数一： 我们要执行的是哪个对象
                            //  参数二： 参数
                            //  result是method.invoke(bean, args)执行完成的返回值
                            Object result = method.invoke(bean, args);
//                          TODO 增强对象的after对象
                            myAdvice.afterAdvice();
                            return result;
                        }
                    });
//          返回代理对象
            return beanProxy;
        }
//      运行到这里说明不需要代理对象
        return bean;
    }


    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
