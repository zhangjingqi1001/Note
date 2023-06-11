package com.zhangjingqi.controller;

import com.zhangjingqi.service.QuickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class QuickController {
    @Autowired
    private QuickService quickService;

    @RequestMapping("/show")
    public String show(){
        System.out.println("show running"+quickService);
        return "hello word";
    }
}
