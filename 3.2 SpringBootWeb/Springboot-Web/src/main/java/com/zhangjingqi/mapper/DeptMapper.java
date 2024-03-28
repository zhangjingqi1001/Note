package com.zhangjingqi.mapper;

import com.zhangjingqi.pojo.Dept;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DeptMapper {
    @Select("select * from dept")
    List<Dept> list();


    @Insert("insert  into dept(name,create_time,update_time) value (#{name},#{createTime},#{updateTime})")
    void add(Dept dept);

    @Delete("delete from dept where id= #{id}")
    void deleteById(Integer id);


}
