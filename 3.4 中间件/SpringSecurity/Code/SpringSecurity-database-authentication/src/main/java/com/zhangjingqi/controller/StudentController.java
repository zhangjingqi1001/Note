package com.zhangjingqi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

    @GetMapping("/query")
    private String query(){
        return "query student";
    }

    @GetMapping("/add")
    private String add(){
        return "add student";
    }

    @GetMapping("/delete")
    private String delete(){
        return "delete student";
    }

    @GetMapping("/update")
    private String update(){
        return "export student";
    }
}
