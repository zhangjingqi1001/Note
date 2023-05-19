package com.zhangjingqi;

import com.zhangjingqi.mapper.EmpMapper;
import com.zhangjingqi.mapper.UserMapper;
import com.zhangjingqi.pojo.Emp;
import com.zhangjingqi.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
class SpringbootWebApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmpMapper empMapper;

    @Test
    void contextLoads() {
        List<User> list = userMapper.list();
        System.out.println(list);
    }

    @Test
    void test02() {
//        empMapper.delete(20);
    }

    @Test
    void test03() {
        Emp emp = new Emp();
        emp.setId(18);
        emp.setUsername("Tom111");
        emp.setName("汤姆111");
        emp.setUpdateTime(LocalDateTime.now());
//        empMapper.update(emp);
    }

    @Test
    void test04() {
        Emp emp = empMapper.getById(15);
        System.out.println(emp);
//      Emp(id=15, username=yulianzhou, password=123456, name=俞莲舟, gender=1, image=15.jpg, job=2, entrydate=2011-05-01, deptId=2, createTime=2023-05-13T11:16:16, updateTime=2023-05-13T11:16:16)
    }


    @Test
    public void test06() {
        List<Integer> ids = Arrays.asList(1, 2, 3);
//        empMapper.deleteByIds(ids);
    }

    /**
     * 生成JWT
     */
    @Test
    public void testGenJwt() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("name", "tom");

//      链式编程 - Jwt令牌在生成的时候所需要设置的一些参数
        String jwt = Jwts.builder()
//              存储在第一个部分         参数一 数字签名算法  参数二 秘钥
                .signWith(SignatureAlgorithm.HS256, "zhangjingqi")
//               JWT令牌所存储的内容（自定义数据，存储在第二个部分，原始自定义数据是JSON格式）
//               可以是Map集合，也可以是Claims对象
                .setClaims(map)
//               设置令牌有效期 - 一个小时后过期， 因为是毫秒，3600*1000代表一个小时
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
//               调用compact会有一个String返回值，就是JWT令牌
                .compact();

        System.out.println(jwt);//eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoidG9tIiwiaWQiOjEsImV4cCI6MTY4NDIxNjUzNX0.s33vECyehznKMrbDqD1Pdx-DrHWkscdyNeWmLnY-ArU

    }

    @Test
    public void testParseJwt(){
        Claims claims = Jwts.parser()
//               指定签名秘钥
                .setSigningKey("zhangjingqi")
//               解析JTW令牌
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoidG9tIiwiaWQiOjEsImV4cCI6MTY4NDIxNjUzNX0.s33vECyehznKMrbDqD1Pdx-DrHWkscdyNeWmLnY-ArU")
//               拿到了我们自定义的内容，也就是Jwt令牌的第二个部分
                .getBody();
        System.out.println(claims); //{name=tom, id=1, exp=1684216535}
    }


}
//Emp(id=15, username=yulianzhou, password=123456, name=俞莲舟, gender=1, image=15.jpg, job=2, entrydate=2011-05-01, deptId=2, createTime=2023-05-13T11:16:16, updateTime=2023-05-13T11:16:16)