package com.zhangjingqi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    /**
     * 跳转到登陆页面
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
//     返回thymeleaf的逻辑视图名。 物理视图=前缀+逻辑视图名+后缀,物理视图 = /templates/ + login + .html
        return "login";
    }

}
