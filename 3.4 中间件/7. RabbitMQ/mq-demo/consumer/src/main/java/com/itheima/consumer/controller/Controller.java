package com.itheima.consumer.controller;

import com.itheima.consumer.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Controller {
    @Autowired
    private TestService testService;

    @GetMapping("/getA")
    public int getInt(){
        int a = testService.getA();
        testService.setA(a+1);
        return a;
    }
}
