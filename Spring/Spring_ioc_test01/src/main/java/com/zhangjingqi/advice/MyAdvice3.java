package com.zhangjingqi.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class MyAdvice3 implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        System.out.println("环绕前****");
//      执行目标方法
//      getMethod得到字节码文件，
//      invoke方法需要两个参数，一个是当前要被执行对象是谁,第二个是方法的参数
        Object result = methodInvocation.getMethod().invoke(methodInvocation.getThis(), methodInvocation.getArguments());
        System.out.println("环绕后****");
        return result;
    }
}
