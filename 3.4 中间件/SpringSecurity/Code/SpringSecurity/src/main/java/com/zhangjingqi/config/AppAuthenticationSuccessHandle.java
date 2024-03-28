package com.zhangjingqi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangjingqi.utils.HttpResult;
import org.omg.CORBA.Object;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证成功就会调用该接口里的方法
 */
@Component
public class AppAuthenticationSuccessHandle implements AuthenticationSuccessHandler {

//  JSON序列化器，进行序列化和反序列化
    @Resource
    private ObjectMapper objectMapper;;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//      定义返回对象httpResult
        HttpResult httpResult = HttpResult.builder()
                .code(200)
                .msg("登陆成功")
                .build();

        String strResponse = objectMapper.writeValueAsString(httpResult);

//      响应字符集
        response.setCharacterEncoding("UTF-8");
//      响应内容类型JSON,字符集utf-8
        response.setContentType("application/json;charset=utf-8");
//      响应给前端
        PrintWriter writer = response.getWriter();
        writer.println(strResponse);
        writer.flush();
    }
}
