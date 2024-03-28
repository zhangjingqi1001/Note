package com.zhangjingqi.service.Impl;

import com.zhangjingqi.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TeacherServiceImpl implements TeacherService {

    //预授权注解,此处采用表达式的形式。
    //如果有teacher:add权限，才会执行下面这个方法；反之不会访问
    @PreAuthorize("hasAuthority('teacher:add')")
    @Override
    public String add() {
        log.info("添加教师成功");
        return "添加教师成功";
    }

    //只要有teacher:update或teacher:add权限其中之一，便可以执行下面的方法
    @PreAuthorize("hasAnyAuthority('teacher:update','teacher:add')")
    @Override
    public String update() {
        log.info("修改教师成功");
        return "修改教师成功";
    }

    @PreAuthorize("hasAuthority('teacher:delete')")
    @Override
    public String delete() {
        log.info("删除教师成功");
        return "删除教师成功";
    }

    @PreAuthorize("hasAuthority('teacher:query')")
    @Override
    public String query() {
        log.info("查询教师成功");
        return "查询教师成功";
    }
}
