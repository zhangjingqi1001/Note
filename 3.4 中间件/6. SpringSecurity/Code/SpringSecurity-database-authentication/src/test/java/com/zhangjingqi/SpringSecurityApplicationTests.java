package com.zhangjingqi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhangjingqi.dto.SysMenuDao;
import com.zhangjingqi.dto.SysUserDao;
import com.zhangjingqi.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
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

    @Autowired
    private SysUserDao sysUserDao;

    @Test
    void test1() {
        SysUser obama = sysUserDao.getByUserName("obama");
//        System.out.println(obama);
        assertNotNull(obama);
    }


    @Test
    void test2() {
//      初始的JSON
        String originJSON = "{\n" +
                " \"pays\": [\n" +
                "    {\n" +
                "      \"ls_cpscode\": \"string\",\n" +
                "       \"payclass\": \"string\",\n" +
                "      \"paymoney\":\"string\",\n" +
                "      \"paydetail\": {}\n" +
                "    }\n" +
                "  ]\n" +
                "\n" +
                "}";

//      将其转换成JSON对象
        JSONObject temporayJSON = JSONObject.parseObject(originJSON);
        log.info(temporayJSON.toString());

//      TODO 获取paydetail
//      获取pays数组
        JSONArray paysJSONArray = temporayJSON.getJSONArray("pays");
        log.info("获取pays数组 - "+paysJSONArray.toString()); // [{"payclass":"string","paymoney":"string","paydetail":{},"ls_cpscode":"string"}]

//      获取pays数组的第一个对象
        Object paysJSON = temporayJSON.getJSONArray("pays").get(0);
        log.info("获取pays数组的第一个对象 - " +paysJSON.toString()); //{"payclass":"string","paymoney":"string","paydetail":{},"ls_cpscode":"string"}


//      获取pays数组的第一个对象中的paydetail对象
        JSONObject payDetailJSON = JSONObject.parseObject(paysJSON.toString()).getJSONObject("paydetail");
        log.info("获取paydetail对象 - "+payDetailJSON.toString()); //{}

//      TODO 删除原数组中的paydetail对象
        Object paydetail = JSONObject.parseObject(paysJSON.toString()).remove("paydetail");
        System.out.println(paydetail);
        System.out.println(temporayJSON);

//      TODO 将paydetail对象放入到realJson




//      下面正式操作原数组temporayJSON


////      删除paydetail
//        temporayJSON.getJSONObject("pays").remove("paydetail");
//
//        temporayJSON.getJSONObject("pays").put("paydetail", payDetailJSONObject);
//
//        log.info(temporayJSON.toString());
    }

    @Autowired
    private SysMenuDao sysMenuDao;

    @Test
    void test03(){
        List<String> stringList = sysMenuDao.queryPermissionByUserId(1);
        System.out.println(stringList);
    }

}
