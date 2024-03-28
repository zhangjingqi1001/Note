package com.zhangjingqi.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
//只有当"accountNonExpired"、“accountNonLocked”、“credentialsNonExpired”、"enabled"都为true时，账户才能使用
//之前我们创建的时候，直接User.builder()创建，之后InMemoryUserDetailsManager对象createUser
public class SecurityUser implements UserDetails {

    /**
     * @return 权限信息
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * @return 用户密码，一定是加密后的密码
     */
    @Override
    public String getPassword() {
        //明文为123456
        return "$2a$10$KyXAnVcsrLaHMWpd3e2xhe6JmzBi.3AgMhteFq8t8kjxmwL8olEDq";
    }

    /**
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return "thomas";
    }

    /**
     * @return 账户是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return 账户是否被锁住
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return 凭据是否过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return 账户是否可用
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
