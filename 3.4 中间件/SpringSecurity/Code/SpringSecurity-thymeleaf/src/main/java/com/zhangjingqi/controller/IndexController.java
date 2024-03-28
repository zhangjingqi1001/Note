package com.zhangjingqi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
public class IndexController {

    /**
     * 登录成功后进入主页
     */
    @RequestMapping("/toIndex")
    public String toIndex(){
        return "main";
    }
}
