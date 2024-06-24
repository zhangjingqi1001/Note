package com.zhangjingqi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;

@Configuration
//开启全局方法安全。 prePostEnabled = true 表示预授权和后授权开启
//此注解中包含@Configuration注解，所以不用重复标识
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AppAuthenticationSuccessHandle appAuthenticationSuccessHandle;

    @Resource
    private AppAuthenticationFailHandle appAuthenticationFailHandle;

    @Resource
    private AppLogoutSuccessHandle appLogoutSuccessHandle;

    @Resource
    private AppAccessDenyHandle appAccessDenyHandle;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()//授权http请求
                .anyRequest() //任何请求
                .authenticated();//都需要认证

        http.formLogin()
                .successHandler(appAuthenticationSuccessHandle) //认证成功处理器
                .failureHandler(appAuthenticationFailHandle) // 认证失败处理器
                .permitAll();//允许表单登录

        http.logout()
                .logoutSuccessHandler(appLogoutSuccessHandle);//登录成功处理器;

        http.exceptionHandling()//异常处理
                .accessDeniedHandler(appAccessDenyHandle);//访问被拒绝处理器
    }


//    授权方法
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.authorizeRequests()//授权http请求
//                .anyRequest() //任何请求
//                .authenticated();//都需要认证
//
//        http.formLogin().permitAll();//允许表单登录
//
//
////      或者下面这种形式
////        http.authorizeRequests()//授权http请求
////                .anyRequest() //任何请求
////                .authenticated()//都需要认证
////                .and()
////                .formLogin().permitAll();
//    }


//    /**
//     * 重写 configure(HttpSecurity http)方法,针对URL进行授权
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.authorizeRequests()//授权http请求
//                .mvcMatchers("/student/**")// 匹配/student开头的请求
//                .hasAnyAuthority("student:add")// 拥有student:add权限的用户可以访问上面的url
//
//                .mvcMatchers("/teacher/**") // 匹配/teacher/**开头的请求
//                .hasAnyAuthority("ROLE_teacher")//拥有ROLE_teacher权限的用户可以访问/teacher/**开头的url
//
//                .anyRequest()// 任何请求
//                .authenticated()//需要验证。注意：没有配置的url，只要登录成功就可以访问
//        ;
//
//        http.formLogin().permitAll();//允许表单登录
//    }
}







