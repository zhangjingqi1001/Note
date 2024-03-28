package com.zhangjingqi.vo;

import com.zhangjingqi.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class SecurityUser implements UserDetails {

    private static final long serialVersionUID = -1314948905954698478L;

    private final SysUser sysUser ;

//  用户权限
    private List<SimpleGrantedAuthority> simpleGrantedAuthorities;

    public SecurityUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }


    /**
     *  这个集合中对象的类型必须是GrantedAuthority类或其子类
     * @return 权限信息
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return simpleGrantedAuthorities;
    }

    /**
     * @return 用户密码，一定是加密后的密码
     */
    @Override
    public String getPassword() {
        String myPassword = sysUser.getPassword();
//      将密码擦除，避免在前端页面进行展示
        sysUser.setPassword(null);
        return myPassword;
    }

    /**
     * @return 用户名
     */
    @Override
    public String getUsername() {
        return sysUser.getUsername();
    }

    /**
     * @return 账户是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return sysUser.getAccountNoExpired() != 0;
    }

    /**
     * @return 账户是否被锁住
     */
    @Override
    public boolean isAccountNonLocked() {
        return sysUser.getAccountNoLocked() !=0;
    }

    /**
     * @return 凭据是否过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return sysUser.getCredentialsNoExpired() !=0 ;
    }

    /**
     * @return 账户是否可用
     */
    @Override
    public boolean isEnabled() {
        return sysUser.getEnabled() !=0 ;
    }
}
