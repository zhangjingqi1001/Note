package com.zhangjingqi;

import com.example.HeaderConfig;
import com.example.HeaderParser;
import com.example.TokenParser;
import com.google.gson.Gson;
import com.zhangjingqi.controller.DeptController;
import com.zhangjingqi.mapper.EmpMapper;
import com.zhangjingqi.mapper.UserMapper;
import com.zhangjingqi.pojo.Emp;
import com.zhangjingqi.pojo.Result;
import com.zhangjingqi.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

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
    public void testParseJwt() {
        Claims claims = Jwts.parser()
//               指定签名秘钥
                .setSigningKey("zhangjingqi")
//               解析JTW令牌
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoidG9tIiwiaWQiOjEsImV4cCI6MTY4NDIxNjUzNX0.s33vECyehznKMrbDqD1Pdx-DrHWkscdyNeWmLnY-ArU")
//               拿到了我们自定义的内容，也就是Jwt令牌的第二个部分
                .getBody();
        System.out.println(claims); //{name=tom, id=1, exp=1684216535}
    }

    //  IOC容器对象
//    @Autowired
//    private ApplicationContext applicationContext;

    @Test
    public void testBean() {
//      TODO  根据bean名称获取   若没指定bean名称，默认类名首字母小写
        DeptController deptControllerBean1 = (DeptController) applicationContext.getBean("deptController");
        System.out.println(deptControllerBean1); //com.zhangjingqi.controller.DeptController@249b54af

//      TODO 根据bean的类型获取
        DeptController deptControllerBean2 = applicationContext.getBean(DeptController.class);
        System.out.println(deptControllerBean2);//com.zhangjingqi.controller.DeptController@249b54af

//      TODO 根据bean的名称 及 类型获取
        DeptController deptControllerBean3 = applicationContext.getBean("deptController", DeptController.class);
        System.out.println(deptControllerBean3);//com.zhangjingqi.controller.DeptController@249b54af

    }

    @Test
    public void testScope() {
        for (int i = 0; i < 10; i++) {
            DeptController deptControllerBean2 = applicationContext.getBean(DeptController.class);
            System.out.println(deptControllerBean2);
        }
    }

    @Autowired
    private SAXReader saxReader;
    @Test
    public void testBean2() throws DocumentException {
        Document document = saxReader.read(this.getClass().getClassLoader().getResource("1.xml")
                );

        Element rootElement = document.getRootElement();
        String name = rootElement.element("name").getText();
        String age = rootElement.element("age").getText();
        System.out.println(name + " : " + age); // Tom : 18
    }

    @Autowired
    private Gson gson;

    @Test
    public void testJson(){
        String json = gson.toJson(Result.class);
        System.out.println(json);
    }

    @Autowired
    private ApplicationContext applicationContext ;
    @Test
    public void testTokenParse(){
        System.out.println(applicationContext.getBean(TokenParser.class));
    }

    @Test
    public void testHeaderParser(){
        System.out.println(applicationContext.getBean(HeaderParser.class));
    }

    @Test
    public void testHeaderConfig(){
        System.out.println(applicationContext.getBean(HeaderConfig.class));
    }
}
//Emp(id=15, username=yulianzhou, password=123456, name=俞莲舟, gender=1, image=15.jpg, job=2, entrydate=2011-05-01, deptId=2, createTime=2023-05-13T11:16:16, updateTime=2023-05-13T11:16:16)