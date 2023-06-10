package com.zhangjingqi.service;

//自定义增强类
public class MyAdvice {
public void beforeAdvice(){
System.out.println("beforeAdvice ...");
}
public void afterAdvice(){
System.out.println("afterAdvice ...");
}
}