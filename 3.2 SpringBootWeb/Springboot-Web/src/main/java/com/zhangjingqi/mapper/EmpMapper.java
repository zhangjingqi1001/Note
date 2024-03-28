package com.zhangjingqi.mapper;

import com.zhangjingqi.pojo.Emp;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EmpMapper {
    //  根据ID删除数据
//  #{id} 占位符
//    @Delete("delete from emp where id = #{id}")
//    public void delete(Integer id);

    //  keyProperty  主键字段， useGeneratedKeys 代表我们需要拿到生成的主键值
    @Options(keyProperty = "id", useGeneratedKeys = true)
    @Insert("insert into emp(username,name,gender,image,job,entrydate,dept_id,create_time,update_time)" +
            "values(#{username},#{name},#{gender},#{image},#{job},#{entrydate},#{deptId},#{createTime},#{updateTime})")
    public void insert(Emp emp);


    //    @Select("select id, username, password, name, gender, image, job, entrydate, " +
//            "dept_id deptId, create_time createTime, update_time updateTime from emp " +
//            "where id=#{id}")
//    public Emp getById(Integer id);

//    @Results({
//            @Result(column = "dept_id",property = "deptId"),
//            @Result(column = "create_time",property = "createTime"),
//            @Result(column = "update_time",property = "updateTime")
//    })
//    @Select("select * from emp where id=#{id}")
//    public Emp getById(Integer id);


    @Select("select * from emp where id=#{id}")
    public Emp getById(Integer id);

//    @Select("select * from emp where name like concat('%',#{name},'%') and gender = #{gender} and " +
//            "entrydate between #{begin} and #{end} order by update_time desc")
//    public List<Emp> list(String name , Short gender , LocalDate begin, LocalDate end);

//        public List<Emp> list(String name , Short gender , LocalDate begin, LocalDate end);


    @Select("select count(*) from emp")
    public Long count();

    @Select("select * from emp limit #{start},#{pageSize}")
    public List<Emp> page(Integer start, Integer pageSize);

    //        @Select("select * from emp")
    public List<Emp> list(String name, Short gender, LocalDate begin, LocalDate end);

    void delete(@Param("ids") List<Integer> ids);

    void update(Emp emp);

    @Select("select  * from emp where username =#{username} and password = #{password}")
    Emp getByUsernameAndPassword(Emp emp);

    /**
     * 根据部门ID删除该部门下的员工数据
     * @param id 部门id
     */
    @Delete("delete from emp where dept_id  =#{id}")
    void deleteByDeptId(Integer id);
}
