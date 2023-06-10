package com.zhangjingqi.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {
    private String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";


    //  web容器启动，ServletContext创建
    //contextInitialized只执行一次，服务器启动，ServletContext创建，此方法执行
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("AccountServlet*******************");
        ServletContext servletContext = sce.getServletContext();
        //TODO 获取contextConfigLocation配置文件的名称
        String contextConfigLocation = servletContext.getInitParameter(CONTEXT_CONFIG_LOCATION);
        // 解析出配置文件的名称
        contextConfigLocation = contextConfigLocation.substring("classpath:".length());
        //TODO 1. 创建Spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(contextConfigLocation);
        //TODO 2. 将Spring容器存储到ServletContext域当中
        servletContext.setAttribute("applicationContext",applicationContext);

    }
}
