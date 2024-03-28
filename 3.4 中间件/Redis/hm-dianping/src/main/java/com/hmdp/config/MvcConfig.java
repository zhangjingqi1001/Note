package com.hmdp.config;

import com.hmdp.utils.LoginInterceptor;

import com.hmdp.utils.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    //  拦截器的注册器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//     登录拦截器
        registry.addInterceptor(new LoginInterceptor( ))
                .excludePathPatterns(
                        "/user/code",
                        "/user/login",
                        "/shop/**",
                        "/blog/hot",
                        "/shop-type/**",
                        "upload/**",
                        "voucher/**"
                ).order(1);

//      刷新token拦截器
//      registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate));  这两种方式都是可以的
//      这个拦截器需要先执行  .order(0) 就说明是先执行，从0开始，默认就是0  如果都没有写order的话，就按照添加拦截器的顺序执行拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**").order(0);
    }
}
