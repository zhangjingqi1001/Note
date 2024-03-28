package com.zhangjingqi.service;

import com.zhangjingqi.entity.SysUser;
import org.apache.ibatis.annotations.Param;

public interface SysUserService {
    /**
     * 根据用户名访问用户信息
     * @param userName 用户名
     * @return 用户信息
     */
   public SysUser getByUserName( String userName);



}
