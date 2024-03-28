package com.zhangjingqi.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Slf4j
//@Component
//@Aspect
//public class MyAspect1 {
//
////   生命切入点表达式的注解,切点
//    @Pointcut("execution(* com.zhangjingqi.service.*.*(..))")
//    private void pt(){
//
//    }
//
//
//    //前置通知
//    @Before("pt()")
//    public void before(JoinPoint joinPoint) {
//        log.info("before ...");
//    }
//
//    //环绕通知
//    @Around("execution(* com.zhangjingqi.service.*.*(..))")
//    public Object around(ProceedingJoinPoint proceedingJoinPoint)
//            throws Throwable {
//        log.info("around before ...");
//        //调用目标对象的原始方法执行
//        Object result = proceedingJoinPoint.proceed();
//        //原始方法如果执行时有异常，环绕通知中的后置代码不会在执行了
//        log.info("around after ...");
//        return result;
//    }
//
//    //后置通知
//    @After("execution(* com.zhangjingqi.service.*.*(..))")
//    public void after(JoinPoint joinPoint) {
//        log.info("after ...");
//    }
//
//    //返回后通知（程序在正常执行的情况下，会执行的后置通知）
//    @AfterReturning("execution(* com.zhangjingqi.service.*.*(..))")
//    public void afterReturning(JoinPoint joinPoint) {
//        log.info("afterReturning ...");
//    }
//
//    //异常通知（程序在出现异常的情况下，执行的后置通知）
//    @AfterThrowing("execution(* com.zhangjingqi.service.*.*(..))")
//    public void afterThrowing(JoinPoint joinPoint) {
//        log.info("afterThrowing ...");
//    }
//}