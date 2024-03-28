package com.zhangjingqi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 获取用户登录信息的方式
 */
@RestController
@Slf4j
public class CurrentLoginUserController {

    /**
     * import org.springframework.security.core.Authentication;
     * 一旦登录成功，访问下面的请求就可以得到authentication
     *
     * Authentication 继承 Principal
     *
     */
    @GetMapping("/getLoginUser1")
    public Authentication getLoginUser1(Authentication authentication) {
        return authentication;
    }



    /**
     *import java.security.Principal;
     * 一旦登录成功，访问下面的请求就可以得到principal
     */
    @GetMapping("/getLoginUser2")
    public Principal getLoginUser2(Principal principal) {
        return principal;
    }

    /**
     * 一旦我们登陆成功，框架就会把我们的信息放到安全性上文中SpringContext
     * 所以我们可以通过安全性上文SpringContext获取用户信息
     */
    @GetMapping("/getLoginUser3")
    public Principal getLoginUser3() {
//      通过安全上下文持有器获取安全上下文
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
