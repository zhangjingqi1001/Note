package com.zhangjingqi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
//@Configuration
//开启全局方法安全。 prePostEnabled = true 表示预授权和后授权开启
//此注解中包含@Configuration注解，所以不用重复标识
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 重写 configure(HttpSecurity http)方法
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()//授权http请求
                .anyRequest()//任何请求
                .authenticated();//需要验证

        http.formLogin().permitAll(); //SpringSecurity的表单认证
    }


    /**
     * @return 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
