package com.zhangjingqi.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

//@WebFilter(urlPatterns = "/*")
//public class AbcFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        System.out.println("Abc拦截方法执行，拦截到了请求 ...");
//
//        filterChain.doFilter(servletRequest, servletResponse);
//
//        System.out.println("Abc执行放行后逻辑 ...");
//    }
//}
