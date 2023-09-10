package com.atguigu.paymentdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/books")
public class BookController {

    @GetMapping
    public String getById(){
        System.out.println("springboot is running .... 2");

        log.debug("debug ...");//  专门给程序员调试使用的
        log.info("info ...");
        log.warn("warn ...");
        log.error("error ...");
//      还有一个日志级别是fatal，但是在这里没有提供对应的api，原因是和error合在一块了
//      在日志系统中，对于fatal的定义是“灾难性的后果”，fatal级别是系统处于崩溃状态，但是系统崩溃了，fatal还活着，这是不可能的，所以在这里API合在一块了
        return "springboot is running ...2";
    }
}
