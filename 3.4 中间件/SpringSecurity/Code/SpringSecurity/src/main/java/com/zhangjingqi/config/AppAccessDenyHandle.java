package com.zhangjingqi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangjingqi.utils.HttpResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AppAccessDenyHandle implements AccessDeniedHandler {
    //  JSON序列化器，进行序列化和反序列化
    @Resource
    private ObjectMapper objectMapper;;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //      定义返回对象httpResult
        HttpResult httpResult = HttpResult.builder()
                .code(403)
                .msg("访问被拒绝，您没有权限访问该资源")
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
