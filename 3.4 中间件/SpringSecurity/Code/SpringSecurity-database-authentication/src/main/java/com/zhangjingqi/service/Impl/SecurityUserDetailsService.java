package com.zhangjingqi.service.Impl;

import com.zhangjingqi.entity.SysUser;
import com.zhangjingqi.service.SysMenuService;
import com.zhangjingqi.service.SysUserService;
import com.zhangjingqi.vo.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//      1.从数据库获取用户的详情信息
        SysUser sysUser = sysUserService.getByUserName(username);

        if (null == sysUser){
//          这个异常信息是SpringSecurity中封装的
            throw new UsernameNotFoundException("用户没有找到");
        }

//      2.获取该用户的权限
        List<String> permissionList = sysMenuService.queryPermissionByUserId(sysUser.getUserId());

//        List<SimpleGrantedAuthority> simpleGrantedAuthorities = permissionList.stream().map(permission -> new SimpleGrantedAuthority(permission) ).collect(Collectors.toList());
//      将集合的泛型转换成SimpleGrantedAuthority （GrantedAuthority类的子类即可）
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = permissionList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());


//      2.封装成UserDetails类，SecurityUser类实现了UserDetails接口
        SecurityUser securityUser = new SecurityUser(sysUser);
        securityUser.setSimpleGrantedAuthorities(simpleGrantedAuthorities);

        return securityUser;
    }
}
