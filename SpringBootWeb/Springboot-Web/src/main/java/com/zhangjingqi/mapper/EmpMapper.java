package com.zhangjingqi.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmpMapper {
//  根据ID删除数据
//  #{id} 占位符
    @Delete("delete from emp where id = #{id}")
    public void delete(Integer id);
}
