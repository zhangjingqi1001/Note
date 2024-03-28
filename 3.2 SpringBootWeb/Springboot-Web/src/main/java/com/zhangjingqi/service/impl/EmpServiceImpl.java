package com.zhangjingqi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhangjingqi.mapper.EmpMapper;
import com.zhangjingqi.pojo.Emp;
import com.zhangjingqi.pojo.PageBean;
import com.zhangjingqi.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Override
    public PageBean page(Integer page, Integer pageSize, String name, Short gender, LocalDate begin, LocalDate end) {
//     TODO  设置分页参数
        PageHelper.startPage(page, pageSize);
//     TODO  执行查询
        List<Emp> list = empMapper.list(name, gender, begin, end);
        Page<Emp> p = (Page<Emp>) list;

//     TODO  封装PageBean
        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());

        return pageBean;
    }

    @Override
    public void delete(List<Integer> ids) {
        empMapper.delete(ids);
    }

    @Override
    public void save(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        emp.setCreateTime(LocalDateTime.now());

        empMapper.insert(emp);
    }

    @Override
    public Emp getById(Integer id) {
        return empMapper.getById(id);
    }

    @Override
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.update(emp);
    }

    @Override
    public Emp login(Emp emp) {
        return empMapper.getByUsernameAndPassword(emp);
    }

//    @Override
//    public PageBean page(Integer page, Integer pageSize) {
////      TODO 获取总记录数
//        Long count = empMapper.count();
////      TODO 获取分页数据
//        Integer start = (page-1)*pageSize;
//
//        List<Emp> empList = empMapper.page(start, pageSize);
//
//        PageBean pageBean = new PageBean(count,empList);
//
//        return pageBean;
//    }
}
