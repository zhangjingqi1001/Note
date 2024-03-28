package com.zhangjingqi.web;

import com.zhangjingqi.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Account", urlPatterns = "/accountServlet")
public class AccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//      或者携程request.getServletContext()
        ServletContext context = getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
//      web层调用service层，获得AccountService，accountService又存在于Spring容器之中‘
        AccountService accountService = (AccountService) applicationContext.getBean("accountService");
        accountService.transferMoney("1", "2", 100);

    }

}
