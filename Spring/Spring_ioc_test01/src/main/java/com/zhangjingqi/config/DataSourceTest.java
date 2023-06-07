package com.zhangjingqi.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zhangjingqi.dao.UserDao;
import com.zhangjingqi.dao.impl.UserDaoImpl;
import com.zhangjingqi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
//@Component
public class DataSourceTest {
    //将方法返回值Bean实例以@Bean注解指定的名称存储到Spring容器中
    @Bean("dataSource")
    public DataSource dataSource(@Value("${jdbc.driver}") String driverClassName, UserDaoImpl userDao){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }


    @Bean
    public DataSource beanTest01(@Qualifier("userDaoImpl") UserDao userDao, UserService userService){
        System.out.println(userDao);
        System.out.println(userService);
        DruidDataSource dataSource = new DruidDataSource();
        return dataSource;
    }
}
