package com.zhangjingqi.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zhangjingqi.anno.MyMapperScan;
import com.zhangjingqi.imports.MyImportBeanDefinitionRegistrar;
import com.zhangjingqi.imports.MyImportSelector;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

//标注当前类是一个配置类（替代配置文件的）
//底层也封装了@Component注解
@Configuration
@ComponentScan("com.zhangjingqi")
//@Import(MyImportBeanDefinitionRegistrar.class)
//@Import(MyImportSelector.class)
@MyMapperScan
public class SpringConfig {
    @Bean
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/mybatis");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        return dataSource;
    }
//    @Bean
//    public SqlSessionFactory sqlSessionFactoryBean(){
//
//    }


//    @Bean
//    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        return sqlSessionFactoryBean;
//    }}

}
