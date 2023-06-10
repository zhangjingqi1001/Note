package com.zhangjingqi.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface AccountMapper {
//   加钱
    @Update("update account set money=money+#{money} where id=#{id}")
    public void incrMoney(@Param("id") String account, @Param("money") Integer money);
//   减钱
    @Update("update account set money=money-#{money} where id=#{id}")
    public void decrMoney(@Param("id") String account, @Param("money") Integer money);
}
