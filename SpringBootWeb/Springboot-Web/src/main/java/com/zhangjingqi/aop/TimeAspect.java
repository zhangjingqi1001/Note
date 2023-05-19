package com.zhangjingqi.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component //交给容器IOC进行管理
@Aspect //加上这个注解表示不是一个普通的类，而是一个AOP类，在此类中定义模板方法
public class TimeAspect {

//  参数是一个表达式，表示针对哪些特定方法进行编程
//  com.zhangjingqi.service 包名
//  第一个*代表任意返回值 第二个*代表类名或者接口名  第三个*代表方法名
    @Around("execution(* com.zhangjingqi.service.*.*(..))") //切入点表达式
    public Object recordTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long begin = System.currentTimeMillis();

//      result 原始方法执行返回值
        Object result = proceedingJoinPoint.proceed();//调用原始方式运行

        long end = System.currentTimeMillis();

//      proceedingJoinPoint.getSignature() 获取方法的签名，我们就知道是哪个方法了
//      如： List com.zhangjingqi.service.impl.DeptServiceImpl.list()执行耗时：239ms
        log.info(proceedingJoinPoint.getSignature() + "执行耗时：{}ms", end - begin);

//      原始方法的返回值我们需要返回回去
        return result;
    }
}
