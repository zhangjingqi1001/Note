package com.zhangjingqi.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

//  定义Filter，并重写三个方法
//  在web服务器启动的时候，会自动创建Filter过滤器对象
//不配置的话过滤器不会生效,urlPatterns表示拦截什么样的请求，/*代表拦截所有请求
//@WebFilter(urlPatterns = "/*")
public class DemoFilter implements Filter {

//  初始化方法，过滤器创建完毕之后会自动调用init方法，只会调用一次（只会在创建时调用一次）
//  一般在这里完成一些资源及环境的准备操作
    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
        System.out.println("init 初始化方法执行了");
    }


//  每一次拦截到请求都会调用的方法，最为重要的方法，是会被调用多次
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("Demo拦截方法执行，拦截到了请求 ...");
        chain.doFilter(request, response);
        System.out.println("Demo执行放行后逻辑 ...");
    }

//  销毁方法。服务区关闭时调用，只调用一次
//  一般在这里完成资源的释放和环境的清理操作
    public void destroy() {
//        Filter.super.destroy();
        System.out.println("destroy 销毁方法执行了 ");
    }

}
