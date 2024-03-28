package com.zhangjingqi.controller;

import com.zhangjingqi.service.TeacherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private TeacherService teacherService;


    @GetMapping("/query")
    public String queryInfo() {
        return teacherService.query();
    }

    @GetMapping("/add")
    public String addInfo() {
        return teacherService.add();
    }

    @GetMapping("/update")
    public String updateInfo() {
        return teacherService.update();
    }

    @GetMapping("/delete")
    public String deleteInfo() {
        return teacherService.delete();
    }
}
