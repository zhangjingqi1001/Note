package com.zhangjingqi.service;

import com.zhangjingqi.pojo.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> list();

    void deleteById(Integer id) throws Exception;

    void add(Dept dept);
}
