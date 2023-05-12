package com.zhangjingqi;

import com.zhangjingqi.mapper.UserMapper;
import com.zhangjingqi.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringbootWebApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        List<User> list = userMapper.list();
        System.out.println(list);
    }

}
