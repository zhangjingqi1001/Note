package com.hmdp.utils;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//          HandlerInterceptor 这是拦截器
public class LoginInterceptor implements HandlerInterceptor {

    //  前置拦截   在进入controller之前我们进行登录校验
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//      判断是否需要拦截（ThreadLocal中是否有用户）
        if(UserHolder.getUser() ==null){
//            没有的话进行拦截
            response.setStatus(401);
            return  false;
        }
//      有用户则放行
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
