package com.zhangjingqi.controller;

import com.zhangjingqi.mapper.EmpMapper;
import com.zhangjingqi.pojo.Emp;
import com.zhangjingqi.pojo.Result;
import com.zhangjingqi.service.EmpService;
import com.zhangjingqi.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private EmpService empService;

//  用Emp对象接收用户名和密码，里面将属性封装好了
    @PostMapping("/login")
    public Result login(@RequestBody Emp emp) {
        log.info("员工登录{}",emp);
        Emp e = empService.login(emp);

//      登陆成功，生成令牌并下发令牌
        if(e!=null){
            Map<String, Object> claims = new HashMap<>();
            claims.put("id",e.getId());
            claims.put("name",e.getName());
            claims.put("username",e.getUsername());
//          生成令牌，员工信息已经在里面了
            String jwt = JwtUtils.generateJwt(claims);
            return Result.success(jwt);
        }

//      登录失败，返回错误信息
        return Result.error("用户名或密码错误");
    }
}
