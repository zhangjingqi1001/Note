package com.zhangjingqi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangjingqi.utils.HttpResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证失败就会调用下面的方法
 */
@Component
public class AppAuthenticationFailHandle implements AuthenticationFailureHandler {
    //  JSON序列化器，进行序列化和反序列化
    @Resource
    private ObjectMapper objectMapper;;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //      定义返回对象httpResult
        HttpResult httpResult = HttpResult.builder()
                .code(401)
                .msg("登录失败")
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
