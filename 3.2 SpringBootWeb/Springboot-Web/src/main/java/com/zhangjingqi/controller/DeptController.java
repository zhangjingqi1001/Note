package com.zhangjingqi.controller;

import com.zhangjingqi.pojo.Dept;
import com.zhangjingqi.pojo.Result;
import com.zhangjingqi.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Scope("prototype")
@RestController
//@RequestMapping
@Slf4j
public class DeptController {

    @Autowired
    private DeptService deptService;


    //    @RequestMapping(value = "/depts",method = RequestMethod.GET)
    @GetMapping("/depts")
    public Result list() {
        log.info("查询全部数据");

        List<Dept> deptList = deptService.list();
        return Result.success(deptList);
    }

    @DeleteMapping("/depts/{id}")
    public Result delete(@PathVariable Integer id) throws Exception {
        log.info("根据Id删除部门：{}", id);
        deptService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/depts")
    public Result add(@RequestBody Dept dept) {
        deptService.add(dept);
        return Result.success();
    }
}
