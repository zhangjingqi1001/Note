package com.zhangjingqi.dto;

import com.zhangjingqi.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserDao {

    /**
     * 根据用户名访问用户信息
     * @param userName 用户名
     * @return 用户信息
     */
    SysUser getByUserName(@Param("userName") String userName);


}
