package com.zhangjingqi.test;

import com.zhangjingqi.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext2.xml");
        AccountService accountService = applicationContext.getBean(AccountService.class);
        accountService.transferMoney("1","2",500);
    }
}
