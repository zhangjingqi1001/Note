package com.zhangjingqi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangjingqi.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseController2 {
    @GetMapping("/ajax/req1")
    @ResponseBody
    public User res1() throws JsonProcessingException {
        //创建JavaBean
        User user = new User();
        user.setUsername("haohao");
        user.setAge(18);

        return user;
    }
}
