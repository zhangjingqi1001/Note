package com.zhangjingqi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @GetMapping("/query")
    @PreAuthorize("hasAuthority('teacher:query')")//预授权
    public String queryInfo() {
        return "teacher query";
    }

}
