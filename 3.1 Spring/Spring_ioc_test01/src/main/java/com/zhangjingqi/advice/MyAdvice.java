package com.zhangjingqi.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

//自定义增强类
@Component
@Aspect //表示此类是切面类
public class MyAdvice {
    @Before("")
    public void beforeAdvice() {
        System.out.println("beforeAdvice ...");
    }

    @After("")
    public void afterAdvice() {
        System.out.println("afterAdvice ...");
    }

    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
//      环绕前
        System.out.println("环绕前通知");
//      目标方法
        joinPoint.proceed();
///     环绕后
        System.out.println("环绕后通知");
    }
}