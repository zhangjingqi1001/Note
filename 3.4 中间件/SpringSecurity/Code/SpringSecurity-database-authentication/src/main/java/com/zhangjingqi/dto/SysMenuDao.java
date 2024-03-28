package com.zhangjingqi.dto;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuDao {
   List<String> queryPermissionByUserId(@Param("userId") Integer userId);
}
