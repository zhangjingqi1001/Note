package com.zhangjingqi.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//描述注解什么时候生效的：运行时有效
@Target(ElementType.METHOD)//当前注解可以作用在哪些地方
public @interface MyLog {
}
