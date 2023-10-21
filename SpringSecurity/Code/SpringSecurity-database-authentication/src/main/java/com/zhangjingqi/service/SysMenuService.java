package com.zhangjingqi.service;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuService {
    List<String> queryPermissionByUserId(@Param("userId") Integer userId);
}
