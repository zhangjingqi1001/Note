package com.zhangjingqi.controller;

import com.zhangjingqi.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * HttpSession演示
 */
@Slf4j
@RestController
public class SessionController {

    //设置Cookie - 服务器要给浏览器响应数据
    @GetMapping("/c1")
    public Result cookie1(HttpServletResponse response){
        response.addCookie(new Cookie("login_username","itheima")); //设置Cookie/响应Cookie
        return Result.success();
    }

    //获取Cookie
    @GetMapping("/c2")
    public Result cookie2(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("login_username")){
                System.out.println("login_username: "+cookie.getValue()); //输出name为login_username的cookie
            }
        }
        return Result.success();
    }


//  往HTTPSession中存储值
//  服务器会判断当前这次请求对应的会话对象Session是否存在，
//      如果不存在会新创建一个Session，如果存在会获取当前这一次请求对应的Session
    @GetMapping("/s1")
    public Result session1(HttpSession session){
        log.info("HttpSession-s1: {}", session.hashCode());//HttpSession-s1: 1750219908

        session.setAttribute("loginUser", "tom"); //往session中存储数据
        return Result.success();
    }

//  这个地方我们可以声明HttpSession对象，也可以使用HttpServletRequest对象
    @GetMapping("/s2")
    public Result session2(HttpServletRequest request){
        HttpSession session = request.getSession();//拿到当前这次请求对应的会话对象
        log.info("HttpSession-s2: {}", session.hashCode());  //HttpSession-s2: 1750219908

        Object loginUser = session.getAttribute("loginUser"); //从session中获取数据
        log.info("loginUser: {}", loginUser);  //loginUser: tom
        return Result.success(loginUser);
    }
}
