# Mybatis 



# 一、 Mybatis基本介绍



​    Mybatis 持久层框架，用于简化JDBC开发



官网： https://mybatis.org/mybatis-3/zh/index.html

 



# 二、 Mybatis 快速入门程序



## 2.1 引入Mybatis依赖



```xml
<!--mybatis 起步依赖-->
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.1</version>
</dependency>
    
<!--MySQL驱动包 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.19</version>
</dependency>
```



最新发布的MySQL驱动包，但是我们一般还是使用上面的驱动包

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```



## 2.2 准备工作	

   **数据库相关信息**：  mybatis

```sql
-- 建表
create table user(
 id int unsigned primary key auto_increment comment 'ID',
 name varchar(100) comment '姓名',
 age tinyint unsigned comment '年龄',
 gender tinyint unsigned comment '性别, 1:男, 2:女',
 phone varchar(11) comment '手机号'
) comment '用户表';

-- 测试数据
insert into user(id, name, age, gender, phone) VALUES (null,'白眉鹰
王',55,'1','18800000000');
insert into user(id, name, age, gender, phone) VALUES (null,'金毛狮
王',45,'1','18800000001');
insert into user(id, name, age, gender, phone) VALUES (null,'青翼蝠
王',38,'1','18800000002');
insert into user(id, name, age, gender, phone) VALUES (null,'紫衫龙
王',42,'2','18800000003');
insert into user(id, name, age, gender, phone) VALUES (null,'光明左
使',37,'1','18800000004');
insert into user(id, name, age, gender, phone) VALUES (null,'光明右
使',48,'1','18800000005');
```



**相关实体类**

```java
public class User {
    private Integer id; //id（主键）
    private String name; //姓名
    private Short age; //年龄
    private Short gender; //性别
    private String phone; //手机号
//省略GET, SET方法
}
```





**配置信息：**

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/mybatis?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: root

```



**Mapper**

```java
@Mapper // 在运行时，会自动生成该接口的实现类对象（代理对象），并且将该对象交给IOC容器管理
public interface UserMapper {

//  查询全部用户信息
    @Select("select * from user") // 要执行的sql语句
    public List<User> list();

}
```





**测试程序**

```java
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
```



## 2.3 配置SQL信息

**如果报错，原因**

* IDEA和数据库没有建立连接，不识别信息



**解决方式**

* 在IDEA中配置Mysql数据库连接



![image-20230512161010558](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230512161010558.png)





### 2.3.1 IDEA连接数据库

下图中URL信息填写 2.2配置中的URL即可

![image-20230512161938609](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230512161938609.png)





## 2.4 JDBC 了解

 之前做的JDBC笔记

https://blog.csdn.net/weixin_51351637/article/details/124709121



**JDBC：Java DataBase Connectivity，就是使用Java语言操作关系型数据库的一套API**

   各个数据库厂商去实现这套接口，提供数据库驱动jar包

![image-20230512162608626](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230512162608626.png)





## 2.5 数据库连接池



* 数据库连接池是一个容器，负责分配、管理数据库连接（Connection）
* 允许应用程序重复使用一个现有的数据库连接，而不是再重新创建一个
* 释放空闲时间超过最大空闲时间的连接，来避免因为没有释放连接而引起的数据库链接遗漏



**优点：**

​     资源重用、提升系统响应速度、避免数据库连接遗漏





**没有数据库连接池时：**

   首先创建一个新的连接对象，再执行这条SQL语句，执行完毕后把这个连接对象关闭了来释放资源





**有数据库连接池：**

​    程序在启动时就会在容器当中初始化一定数量的连接对象，客户端在执行SQL语句的时候就会从连接池当中获取一个连接，获取连接之后再执行SQL语句，SQL语句执行完毕之后会把连接再归还给连接池。这样就可以做到连接的复用。 

​    除此之外，当客户占用连接时间过长但没有执行SQL语句并且超过连接池规定的最大空闲时间时，数据库连接池会收回连接





### 2.5.1 Druid数据库连接池



Maven坐标

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.2.8</version>
</dependency>
```



```yaml
spring:
  #指定应用的名称，这一项不是必须的，如果不配置的话默认的时候工程名
  application:
    name: SpringBootWeb
  #数据源
  datasource:
    #德鲁伊连接池
    druid:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/reggie?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true
      username: root
```





# 三、 Mybatis 基础



## 3.1 环境准备

* 准备数据库表emp、dept
*  创建对应实体类



### 3.1.1 数据库表

```sql
-- 部门管理
create table dept
(
 id int unsigned primary key auto_increment comment '主键
ID',
 name varchar(10) not null unique comment '部门名称',
 create_time datetime not null comment '创建时间',
 update_time datetime not null comment '修改时间'
) comment '部门表';

-- 部门表测试数据
insert into dept (id, name, create_time, update_time)
values (1, '学工部', now(), now()),
(2, '教研部', now(), now()),
(3, '咨询部', now(), now()),
(4, '就业部', now(), now()),
(5, '人事部', now(), now());
-- 员工管理

create table emp
(
 id int unsigned primary key auto_increment comment
'ID',
 username varchar(20) not null unique comment '用户名',
password varchar(32) default '123456' comment '密码',
 name varchar(10) not null comment '姓名',
 gender tinyint unsigned not null comment '性别, 说明: 1 男,
2 女',
 image varchar(300) comment '图像',
 job tinyint unsigned comment '职位, 说明: 1 班主任,2 讲师,
3 学工主管, 4 教研主管, 5 咨询师',
 entrydate date comment '入职时间',
 dept_id int unsigned comment '部门ID',
 create_time datetime not null comment '创建时间',
 update_time datetime not null comment '修改时间'
) comment '员工表';
-- 员工表测试数据
INSERT INTO emp (id, username, password, name, gender, image, job,
entrydate, dept_id, create_time, update_time)
VALUES
(1, 'jinyong', '123456', '金庸', 1, '1.jpg', 4, '2000-01-01', 2,
now(), now()),
(2, 'zhangwuji', '123456', '张无忌', 1, '2.jpg', 2, '2015-01-01', 2,
now(), now()),
(3, 'yangxiao', '123456', '杨逍', 1, '3.jpg', 2, '2008-05-01', 2,
now(), now()),
(4, 'weiyixiao', '123456', '韦一笑', 1, '4.jpg', 2, '2007-01-01', 2,
now(), now()),
(5, 'changyuchun', '123456', '常遇春', 1, '5.jpg', 2, '2012-12-05',
2, now(), now()),

(6, 'xiaozhao', '123456', '小昭', 2, '6.jpg', 3, '2013-09-05', 1,
now(), now()),
(7, 'jixiaofu', '123456', '纪晓芙', 2, '7.jpg', 1, '2005-08-01', 1,
now(), now()),
(8, 'zhouzhiruo', '123456', '周芷若', 2, '8.jpg', 1, '2014-11-09', 1,
now(), now()),
(9, 'dingminjun', '123456', '丁敏君', 2, '9.jpg', 1, '2011-03-11', 1,
now(), now()),
(10, 'zhaomin', '123456', '赵敏', 2, '10.jpg', 1, '2013-09-05', 1,
now(), now()),
(11, 'luzhangke', '123456', '鹿杖客', 1, '11.jpg', 5, '2007-02-01',
3, now(), now()),
(12, 'hebiweng', '123456', '鹤笔翁', 1, '12.jpg', 5, '2008-08-18', 3,
now(), now()),
(13, 'fangdongbai', '123456', '方东白', 1, '13.jpg', 5, '2012-11-01',
3, now(), now()),
(14, 'zhangsanfeng', '123456', '张三丰', 1, '14.jpg', 2,'2002-08-01', 2, now(), now()),
(15, 'yulianzhou', '123456', '俞莲舟', 1, '15.jpg', 2, '2011-05-01',
2, now(), now()),
(16, 'songyuanqiao', '123456', '宋远桥', 1, '16.jpg', 2, '2010-01-01', 2, now(), now()),
(17, 'chenyouliang', '123456', '陈友谅', 1, '17.jpg', NULL, '2015-03-21', NULL, now(), now());
```





### 3.1.2 实体类

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emp {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private Short gender;
    private String image;
    private Short job;
    private LocalDate entrydate; //LocalDate类型对应数据表中的date类型
    private Integer deptId;
    private LocalDateTime createTime;//LocalDateTime类型对应数据表中的datetime类型
    private LocalDateTime updateTime;
}

```





## 3.2 基础操作

 

### 3.2.1 删除

**注意：** Mapper接口方法形参只有一个普通类型的参数，#{x}里面的属性名可以随便写，如#{id}，#{value}

```java
//  根据ID删除数据
//  #{id} 占位符
    @Delete("delete from emp where id = #{id}")
    public void delete(Integer id);
```





### 3.2.2 删除（预编译SQL）









