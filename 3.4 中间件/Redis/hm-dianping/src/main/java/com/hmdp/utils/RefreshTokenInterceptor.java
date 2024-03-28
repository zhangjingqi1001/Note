package com.hmdp.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//          HandlerInterceptor 这是拦截器
public class RefreshTokenInterceptor implements HandlerInterceptor {

//  这个类LoginInterceptor 我们并没有给他添加注解，所以这个类并没有交给Spring管理，所以这个地方也没有自动装配的注解
//  这个类的对象使我们自己new出来的（不是Spring帮我们创建的，所以我们不能使用那些注解了）
    private StringRedisTemplate stringRedisTemplate;
//  所以我们使用构造函数注入  很重要哦
//      那我们利用构造函数注入，那谁帮我们注入呢？谁用了它，谁就帮我们注入
    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //  前置拦截   在进入controller之前我们进行登录校验
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//      1.获取请求头中的token     authorization使我们前端定义的一个请求头
        String token = request.getHeader("authorization");
        if(StrUtil.isBlank(token)){
//            不拦截，直接放行
            return true;
        }
//      2.基于Token获取Redis中的用户 这里不用get，get只能获取一个字段的值
//                 entries返回值是一个map
        Map<Object,Object> userMap= stringRedisTemplate.opsForHash()
                                    .entries("login:token:"+token);

//      3.判断用户是否存在
//         这个地方不用担心空指针异常，因为如果是空的话entries返回的就是一个什么也没有的map集合而已
        if(userMap.isEmpty()){
//          不拦截，直接放行
            return true;
        }
//      5.将查询到的hash数据转化为UserDTO对象
//            false 表示不忽略转换过程中的错误
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap,new UserDTO(),false);
//      6.存在，保存用户信息到ThreadLocal  保存在当前线程里面的
        UserHolder.saveUser(userDTO);
//      7.刷新token有效期
        stringRedisTemplate.expire("login:token:"+token,30, TimeUnit.MINUTES);
//      8.放行
        return true;
    }
//  在controller执行之后拦截  这个我们在这里不需要
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    }

//  渲染之后，返回给用户之前   用户业务执行完毕我们要销毁维护信息，避免泄露
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//      移除用户
        UserHolder.removeUser();
    }
}
