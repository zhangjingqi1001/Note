package com.zhangjingqi.service.Impl;

import com.zhangjingqi.utils.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 当我们定义了此类后，系统默认的UserDetailsService不会起作用，下面UserServiceImpl会起作用
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    /**
     * 根据用户名获取用户详情UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser securityUser= new SecurityUser();//我们自己定义的
        if(username==null || !username.equals(securityUser.getUsername())){
//          SpringSecurity框架中自带的异常
            throw new UsernameNotFoundException("该用户不存在或用户名不正确");
        }
//      执行到这里，说明username是没有问题的
//      用户密码对不对，框架会帮我们进行判断
        return securityUser;
    }

}
