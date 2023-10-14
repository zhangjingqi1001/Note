package com.zhangjingqi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


/**
 * 定义一个Bean，用户详情服务接口
 *
 *   系统中默认是有这个UserDetailsService的，也就是默认的用户名（user）和默认密码（控制台生成的）
 *   如果在yaml文件中配置了用户名和密码，那在系统中的就是yaml文件中的信息
 *
 * 我们自定义了之后，就会把系统中的UserDetailsService覆盖掉
 */
@Configuration
public class MySecurityUserConfig  {
    /**
     * 根据用户名把用户的详情从数据库中获取出来,封装成用户细节信息UserDetails（包括用户名、密码、用户所拥有的权限）
     *
     * UserDetails存储的是用户的用户名、密码、去权限信息
     */
    @Bean
    public UserDetailsService userDetailsService() {
//      用户细节信息,创建两个用户
//      此User是SpringSecurity框架中的public class User implements UserDetails, CredentialsContainer
        UserDetails user1 = User.builder().username("zhangjingqi-1").password(passwordEncoder().encode("123456")).roles("student").build();
        UserDetails user2 = User.builder().username("zhangjingqi-2").password(passwordEncoder().encode("123456")).roles("teacher").build();

//      InMemoryUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService,其中UserDetailsManager继承UserDetailsService
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(user1);
        userDetailsManager.createUser(user2);
        return  userDetailsManager;
    }

    /**
     * 配置密码加密器
     * NoOpPasswordEncoder.getInstance() 此实例表示不加密
     * BCryptPasswordEncoder() 会加密
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
