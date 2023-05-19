package com.zhangjingqi.interceptor;

import com.alibaba.fastjson.JSON;
import com.zhangjingqi.pojo.Result;
import com.zhangjingqi.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    //  目标资源方法执行前执行（Controller方法执行之前）， true：放行， false：不放行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //  TODO 1.获取请求url
        String requestURL = request.getRequestURL().toString(); //不toString就是StringBuffer类型
        log.info("请求的url:{}", requestURL);

        //  TODO 2.判断请求url中是否包含login，如果包含，说明是登录操作，放行
        if (requestURL.contains("/login")) {
            log.info("登录操作，放行...");
            return true;
        }

        //  TODO 3.获取请求头中的令牌（token）
        String token = request.getHeader("token");

        //  TODO 4.判断令牌是否存在，如果不存在，返回错误结果（未登录）
        if (!StringUtils.hasLength(token)) { //spring当中的工具类
//            说明字符串为null，返回错误结果（未登录）
            log.info("请求头token为空，返回未登录的信息");
            Result error = Result.error("NOT_LOGIN");
//          手动转JSON
            String errorJson = JSON.toJSONString(error);
//          response.getWriter()获取输出流，write()直接将数据响应给浏览器
            response.getWriter().write(errorJson);
            return false;
        }

        //  TODO 5.解析token，如果解析失败，返回错误结果（未登录）
//      说明存在令牌，校验
        try {
            Claims claims = JwtUtils.parseJWT(token);
        } catch (Exception e) { // 出现异常代表着解析失败
            e.printStackTrace();
            log.info("解析令牌失败，返回未登录错误信息");
            Result error = Result.error("NOT_LOGIN");
//          手动转JSON
            String errorJson = JSON.toJSONString(error);
//          response.getWriter()获取输出流，write()直接将数据响应给浏览器
            response.getWriter().write(errorJson);
            return false;
        }
//       到这里说明令牌解析成功，直接放行
        //  TODO 6.放行
        log.info("令牌合法，放行");
//        return HandlerInterceptor.super.preHandle(request, response, handler);
        return true;
    }

    //  目标资源方法执行后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle.....");
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    //  视图渲染完毕后执行，最后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        System.out.println("afterCompletion......");
    }
}
