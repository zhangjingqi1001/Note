package com.zhangjingqi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 重写 configure(HttpSecurity http)方法
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()//授权http请求
                .mvcMatchers("/student/**")// 匹配/student开头的请求
                .hasAnyAuthority("student:add")// 拥有student:add权限的用户可以访问上面的url

                .mvcMatchers("/teacher/**") // 匹配/teacher/**开头的请求
                .hasAnyAuthority("ROLE_teacher")//拥有ROLE_teacher权限的用户可以访问/teacher/**开头的url

                .anyRequest()// 任何请求
                .authenticated()//需要验证。注意：没有配置的url，只要登录成功就可以访问
        ;

        http.formLogin().permitAll();//允许表单登录
    }
}







