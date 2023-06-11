package com.zhangjingqi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangjingqi.entity.User;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class ParamController {
    @GetMapping("/request1")
    public String request1(HttpServletRequest request) {
//       存储数据
        request.setAttribute("username", "haohao");
        return "/request2";
    }

    @GetMapping("/request2")
    public String request2(@RequestAttribute("username") String username) {
        System.out.println(username);
        return "/index.jsp";
    }

    @GetMapping("/cookies")
    public String cookies(@CookieValue(value = "JSESSIONID", defaultValue = "aaaaa") String jsessionid) {
        System.out.println(jsessionid);
        return "/index.jsp";
    }

    @GetMapping("/headers")
    public String headers(@RequestHeader("Accept-Encoding") String acceptEncoding) {
        System.out.println("Accept-Encoding:" + acceptEncoding);
        return "/index.jsp";
    }

    @GetMapping("/headersMap")
    public String headersMap(@RequestHeader Map<String, String> map) {
        map.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
        return "/index.jsp";
    }

    @PostMapping("/param10")
    public String param10(@RequestBody MultipartFile myFile) throws IOException {
        System.out.println(myFile);
        // 将上传的文件进行保存
        //TODO 获得当前上传的文件的输入流
        InputStream inputStream = myFile.getInputStream();

        //TODO 获得上传文件位置的输出流
        OutputStream outputStream = new FileOutputStream("D:/CourseData/" + myFile.getOriginalFilename());

        //TODO 执行文件拷贝
        IOUtils.copy(inputStream, outputStream);

        outputStream.flush();
        //TODO 关闭流资源
        inputStream.close();
        outputStream.close();

        return "hello";
    }


    @GetMapping("/param1")
    public String param1() {
        return "hello word";
    }

    @PostMapping("/param7")
    public String show6(@RequestBody String body) throws IOException {
        //获得ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        //将json格式字符串转化成指定的User
        User user = objectMapper.readValue(body, User.class);
        System.out.println(user);

        return "/index.jsp";
    }

    @PostMapping("/param8")
    public String param8(@RequestBody User user) throws IOException {
        System.out.println(user);

        return "/index.jsp";
    }
}
