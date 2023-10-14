package com.zhangjingqi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SpringSecurityApplicationTests {

    @Test
    void testBcrypt() {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode1 = passwordEncoder.encode("123456");
        String encode2 = passwordEncoder.encode("123456");
        String encode3 = passwordEncoder.encode("123456");
        System.out.println(encode1);//$2a$10$hkaTFSnsEBYcZxXMrDpMQu.IiPM5ZAIQ63Vvkq01.oxxv0yVRmKly
        System.out.println(encode2);//$2a$10$nuPqwjhW0e/RZ.h3L1gZx.KanwUNQd4GEB2YoeB/LeVOhavcoBS7O
        System.out.println(encode3);//$2a$10$SPHV9tuI6JhgOwDO2hMR4eq43E5BGmYJDQJ5GltIuK7WSQqi1sHzm

//      参数1：原文   参数2：密文
        boolean result1 = passwordEncoder.matches("123456", encode1);
        boolean result2 = passwordEncoder.matches("123456", encode2);
        System.out.println(result1);//true
        System.out.println(result2);//true


        assertTrue(result1);
        assertTrue(result2);

    }

}
