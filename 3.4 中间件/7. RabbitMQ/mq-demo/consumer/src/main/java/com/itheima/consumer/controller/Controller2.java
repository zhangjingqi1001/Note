package com.itheima.consumer.controller;

import com.itheima.consumer.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test2")
public class Controller2 {
    @Autowired
    private TestService testService;

    @GetMapping("/getA2")
    public int getInt(){
        int a = testService.getA();
        testService.setA(a+1);
        return a;
    }
}
