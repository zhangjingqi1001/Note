package com.zhangjingqi.controller;

import com.zhangjingqi.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ResponseController {
    @GetMapping("/res1")
    public String res1() {
        //重定向方式
        return "redirect/index.jsp";
    }

    @GetMapping("/res2")
    public String res2() {
        //请求转发方式
        return "forward:/index.jsp";
    }

    @GetMapping("/res3")
    public ModelAndView res3(ModelAndView modelAndView) {
        //modelAndView 封装模型数据和视图名
        //设置模型数据
        User user = new User();
        user.setUsername("zhangjingqi");
        user.setAge(18);
        modelAndView.addObject("user",user);
        //设置视图名称，在页面中展示模型数据
        modelAndView.setViewName("/index.jsp");

        return modelAndView;
    }

    @GetMapping("/res4")
    @ResponseBody
    public String res4() {
        //请求转发方式
        return "hello";
    }

}
