[TOC]



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





### 2.3.2 打开日志信息

```yaml
mybatis:
  configuration.log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```





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





### 3.2.2  预编译SQL

 还是使用下面的代码进行演示，我们已经开始mybatis日志信息，我们关注一下

```java
//  根据ID删除数据
//  #{id} 占位符
    @Delete("delete from emp where id = #{id}")
    public void delete(Integer id);
```



**日志信息：**注意看黑体部分，Parameters替换掉Preparing中占位符

   对于黑体部分有一个专业的名字：**预编码SQL**

> Creating a new SqlSession
> SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@1640c151] was not registered for synchronization because synchronization is not active
> 2023-05-13 11:24:21.721  INFO 12456 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
> 2023-05-13 11:24:21.984  INFO 12456 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
> JDBC Connection [HikariProxyConnection@1905114489 wrapping com.mysql.cj.jdbc.ConnectionImpl@20134094] will not be managed by Spring
> ==>  **Preparing: delete from emp where id = ? **
> ==> **Parameters: 20(Integer)**
> <==    Updates: 0
> Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@1640c151]





**为什么采用预编译SQL？**

*  性能更高
*  更安全（防止SQL注入）



#### 3.2.2.1 性能更高



在java中编写了一条SQL语句，SQL语句要想执行就需要**连接上数据库**，然后才能**将SQL语句发送给mysql数据库服务器**。



发送给服务器之后，服务器并不是立刻执行，而是要**经历  “SQL语法解析检查” -> “优化SQL” -> “编译SQL” -> “执行SQL”**。



MySQL服务器为了效率，会**将优化编译后的结果缓存起来（“SQL语法解析检查” -> “优化SQL” -> “编译SQL”的最终结果）**。下一次执行SQL的时候会先检查缓存当中是否有编译好的SQL语句，如果有就不用再执行这一系列操作了，直接执行SQL语句；如果没有的话，将四步进行完成，然后存入到缓存空间中



**在“执行SQL”操作的时候，会将Parameters参数替换Preparing中的占位符**





#### 3.2.2.2 更安全

**SQL注入**：通过操作输入的数据来修改事先定义好的SQL语句，以达到执行代码对服务器进行攻击的方法。



**假设我们不防止SQL注入（没有预编译）**，就会出现用户名和密码都错误但是还能登陆成功的情况

​     用户名：zhangzhang

​     密码：' or '1'='1

```sql
 select * count(*) from emp where username ='zhangzhang' and password ='' or '1'='1'
```

很明显password已经变成了其他样子： password ='' or '1'='1' ，password只需要满足后面两个条件中的一个条件即可，并且 '1'='1' 永远是true，永远成立





**假设我们防止SQL注入（预编译）**

预编译语句

```sql
 select * count(*) from emp where username = ? and password = ?
```

 此时将用户名与密码输入后，会将  ' or '1'='1 看做一个整体填入到password后面的占位符，所以不存在SQL注入问题

​    用户名：zhangzhang

​     密码：' or '1'='1





**我们使用Mybatis时怎么选择使用预编译SQL？**

   在SQL语句中填写占位符，如#{id}





#### 3.2.2.3参数占位符

* **#{..}**

​          执行SQL时，会将#{..}替换为 ？ ，生成预编译SQL，会自动设置参数值

​         **使用时机： 参数传递**



* **${..}**

​           拼接SQL，直接将参数拼接在SQL语句中，存在SQL注入问题。

​           **使用时机：如果对表明、列表进行动态设置时使用。**



![image-20230513172952982](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230513172952982.png)

### 3.2.3 新增

很明显，下面是写死的，我们不需要这种

```java
@Insert("insert into emp(username,name,gender,image,job,entrydate,dept_id,create_time,update_time)"+
"values('Tom','汤姆',1,'1.jpg',1,'2005-01-01',1,now(),now())")
public void insert();
```



我们选择将字段封装成一个对象，如下所示：

```java
@Insert("insert into emp(username,name,gender,image,job,entrydate,dept_id,create_time,update_time)"+
"values(#{username},#{name},#{gender},#{image},#{job},#{entrydate},#{deptId},#{createTime},#{updateTime})")
public void insert(Emp emp);
```



进行测试

```java
Emp emp = new Emp();
emp.setUsername ("Tom") ;
emp.setName("汤姆") ;
emp. setImage("1.jpg") ;
emp.setGender((short)1) ;emp.setJob((short)1) ;
emp.setEntrydate (LocalDate.of(  2000,1,  1)) ;
emp.setCreateTime(LocalDateTime.now() );
emp.setUpdateTime (LocalDateTime.now());
emp.setDeptId(1);

empMapper.insert(emp);
```





#### 3.2.4.1 主键返回

主键返回：在数据添加成功后，需要获取插入数据库数据的主键。

   如： 添加套餐数据时，还需要维护套餐菜品关系表数据。



> **步骤：**
>
> ​    先保存套餐信息，并获取套餐ID
>
> ​    再保存套餐菜品关联信息（需要记录套餐ID、菜品ID）
>
> 
>
>  所以我们应该学会怎么在保存信息后获取套餐ID



 

```java
//  keyProperty  主键字段， useGeneratedKeys 代表我们需要拿到生成的主键值
    @Options(keyProperty = "id",useGeneratedKeys = true)
    @Insert("insert into emp(username,name,gender,image,job,entrydate,dept_id,create_time,update_time)"+
    "values(#{username},#{name},#{gender},#{image},#{job},#{entrydate},#{deptId},#{createTime},#{updateTime})")
    public void insert(Emp emp);
```





### 3.2.4 更新

SQL语句

```java
@Update("update emp set username = #{username},name = #{name},gender = #{gender},image = #{image},"+
"job= #{job},entrydate= #{entrydate},dept_id= #{deptId},update_time= #{updateTime} where id=#{id}")
public void update(Emp emp);
```





**测试**

```java
Emp emp = new Emp();
emp.setId(22);
emp.setUsername ("Tom2") ;
emp.setName("汤姆8") ;
emp. setImage("1.jpg") ;
emp.setGender((short)1) ;emp.setJob((short)1) ;
emp.setEntrydate (LocalDate.of(  2000,1,  1)) ;
emp.setUpdateTime (LocalDateTime.now());
emp.setDeptId(1);

empMapper.update(emp);
```





### 3.2.5  查询

```java
    @Select("select * from emp id=#{id}")
    public Emp getById(Integer id);
```



测试一下

```java
Emp emp = empMapper.getById(15);
System.out.println(emp);
//Emp(id=15, username=yulianzhou, password=123456, name=俞莲舟, gender=1, image=15.jpg, job=2, entrydate=2011-05-01, deptId=null, createTime=null, updateTime=null)
```



然后发现deptId=null, createTime=null, updateTime=null这三个字段为null，原因是数据封装出现的问题



### 3.2.6 条件查询

```java
@Select("select * from emp where name like '%${name}%' and gender = #{gender} and " +
        "entrydate between #{begin} and #{end} order by update_time desc")
public List<Emp> list(String name , Short gender , LocalDate begin, LocalDate end);
```

**注意我们在模糊查询的时候并没有使用#{name}，而是使用的${name}**

**原因：**

​      $是字符串拼接的符号，它不会生成和编译SQL。会将传递过来的name和%、{}进行拼接

**如果是用#{name}，外面还有单引号，井号与大括号是不能出现在引号之内的，因为#{..}生成的预编译SQL，最后是要被 “？” 代替的，但是占位符“？”是不能出现在引号之内的。 **



但是此时生成的就不是预编译的SQL，存在SQL注入问题。

但是这个问题可以使用SQL中的**concat函数** - 拼接字符串 来解决。

```java
@Select("select * from emp where name like concat('%',#{name},'%') and gender = #{gender} and " +
        "entrydate between #{begin} and #{end} order by update_time desc")
public List<Emp> list(String name , Short gender , LocalDate begin, LocalDate end);
```





### 3.2.7 数据封装



**数据封装：**

* 实体类属性名和数据库表查询返回的字段名一致，mybatis会自动封装
* 如果实体类属性名和数据库表查询返回的字段名不一致，不能自动封装



**解决方案**

* **方案一 字段起别名**

```java
@Select("select id, username, password, name, gender, image, job, entrydate, " +
        "dept_id deptId, create_time createTime, update_time updateTime from emp " +
        "where id=#{id}")
public Emp getById(Integer id);
```

   **测试**

```java
        Emp emp = empMapper.getById(15);
        System.out.println(emp);
//      Emp(id=15, username=yulianzhou, password=123456, name=俞莲舟, gender=1, image=15.jpg, job=2, entrydate=2011-05-01, deptId=2, createTime=2023-05-13T11:16:16, updateTime=2023-05-13T11:16:16)
 
```





* **方案二 通过@Results @Result注解手动映射封装**

一个@Result注解映射一个字段和属性

```java
@Results({
        @Result(column = "dept_id",property = "deptId"),
        @Result(column = "create_time",property = "createTime"),
        @Result(column = "update_time",property = "updateTime")
})
@Select("select * from emp where id=#{id}")
public Emp getById(Integer id);
```





* **方案三 开始mybatis驼峰命名自动映射开关**

```java
@Select("select * from emp where id=#{id}")
public Emp getById(Integer id);
```



```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/mybatis?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: root
mybatis:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```





# 四、 XML映射文件

中文网：https://mybatis.net.cn/getting-started.html



**规范**

* **XML映射文件的名称与Mapper接口名称一致，并且将XML映射文件和Mapper接口放置在相同包下（同包同名）**

​          在resource下创建目录的时候不要使用“.”，要使用“/”，比如“com/zhangjingqi/mapper”



* **XML映射文件的namespace属性为Mapper接口全限定名一致**



* **XML映射文件中SQL语句的id与Mapper接口中的方法名一致，并保持返回类型一致**

![image-20230513173645547](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230513173645547.png)



使用注解来映射简单语句会使代码显得更加简洁，但对于稍微复杂一点的语句，Java注解不仅力不从心，还会让你本就复杂的SQL语句更加混乱不堪。因此，如果你需要做一些复杂的操作，最好是用XML来映射语句。



## 4.1 案例

**方法名称**

```java
public List<Emp> list(String name , Short gender , LocalDate begin, LocalDate end);
```



**配置文件**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.zhangjingqi.mapper.EmpMapper">
<!--  resultType 表示单条记录所封装的类型-->
    <select id="list" resultType="com.zhangjingqi.pojo.Emp">
        select *
        from emp
        where name like concat('%',#{name},'%')
          and gender = #{gender} and entrydate between #{begin}
              and #{end} order by update_time desc
    </select>
</mapper>
```



**流程**

   当调用list(...)方法的时候，mybatis框架就会自动查找namespace属性值与这个接口全类名相同的这份xml映射文件。

   并且在XML映射文件中找到id属性值与方法名称相同的SQL语句，最终运行SQL语句





## 4.2 MybatisX  插件

* MybatisX 是一款基于IDEA快速开发Mybatis的插件，为效率而生。

* 安装

![image-20230513180354759](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230513180354759.png)







# 五、Mybatis 动态SQL

随着用户的输入或外部条件的变化而变化的SQL语句，我们称为**动态SQL**



## 5.1 \<if\>

用于判断条件是否成立。使用test属性进行条件判断，如果条件为true，则拼接SQL



**test属性指定条件，满足条件便拼接字符串**

**where作用**

* 根据字标签动态的判断里面的条件，如果里面条件都不成立，就不会生成where这个关键字

*  where 元素只会在子元素有内容的情况下才插入where子句。会自动取出SQL前面的 and 或者是 or



**\<if\>的作用**

* 比如第一个if与第二个if，加入上面name条件没有成立，那下面这段SQL就会产生错误，多了一个"and"

* 用于判断条件是否成立。使用test属性进行条件判断，如果条件为true，则拼接SQL

  

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.zhangjingqi.mapper.EmpMapper">
    <!--  resultType 表示单条记录所封装的类型-->
    <select id="list" resultType="com.zhangjingqi.pojo.Emp">
        select *
        from emp

        <where>
            <if test=" name != null">
                name like concat('%',#{name},'%')
            </if>

            <if test=" gender != null">
                and gender = #{gender}
            </if>

            <if test="begin !=null and end !=null">
                and entrydate between #{begin} and #{end}
            </if>
        </where>
        order by update_time desc
    </select>

</mapper>
```







## 5.2  \<if\> 案例

**动态更新员工： 更新ID为18的员工**

下面是之前的方式，

```java
@Update("update emp set username = #{username},name = #{name},gender = #{gender},image = #{image}," +
        "job= #{job},entrydate= #{entrydate},dept_id= #{deptId},update_time= #{updateTime} where id=#{id}")
public void update(Emp emp);
```



 有许多的不方便之处，比如我只想更新username、name、updateTime字段，我们调用方法之后，发现除了上面三个字段，其他的都是null（某些字段没有赋值，就会成null），显然不是我们想要的更新结果。

```java
Emp emp = new Emp();
emp.setId(18);
emp.setUsername ("Tom111") ;
emp.setName("汤姆111") ;
emp.setUpdateTime (LocalDateTime.now());
empMapper.update(emp);
```





**下面我们要实现如果有值时就更新，没有值的时候就不更新**

**\<set\>**标签的作用：去掉字段之后多余的","

```xml
    <update id="update">
        update emp
        <set>
        <if test="username!=null">
            username = #{username},
        </if>
        <if test="name!=null">
            name = #{name},
        </if>
        <if test="gender!=null">
            gender = #{gender},
        </if>
        <if test="image!=null">
            image = #{image},
        </if>
        <if test="job !=null">
            job= #{job},
        </if>
        <if test="entrydate!=null">
            entrydate= #{entrydate},
        </if>
        <if test="deptId!=null">
            dept_id= #{deptId},
        </if>
        <if test="updateTime!=null">
            update_time= #{updateTime}
        </if>
        </set>
        where id = #{id}
    </update>
```





## 5.3  \<foreach\>

在批量删除的时候会使用

```java
public void deleteByIds(List<Integer> ids);
```



**属性含义**

* collection ： 指定要遍历的集合或数组，可以是 List、Set、Array、Map 等类型。遍历操作会把元素依次添加到 SQL 语句中。

*  item：指定遍历时，每次遍历出来的元素的名称。

*  separator：分隔符， 每一次遍历出来的元素在拼接的时候用什么分割

*  open： 指定在遍历列表时，拼接到列表的第一个元素前的 SQL 语句。

*  close： 指定在遍历列表时，拼接到列表的最后一个元素后的 SQL 语句。

  

  **配置文件**

  ```xml
      <delete id="deleteByIds">
          delete
          from emp
          where id in
          <foreach collection="list" item="id" separator="," open="(" close=")">
              #{id}
          </foreach>
      </delete>
  
  ```



**测试类**

```java
List<Integer> ids = Arrays.asList(17,18,22);
empMapper.deleteByIds(ids);
```

控制台SQL日志

![image-20230513205124602](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230513205124602.png)



## 5.4 \<sql\> \<include\>

\<sql\> ： 定义可重用的SQL片段

\<include\>： 通过refid，指定包含sql片段



**抽取与引用的作用**：提高代码的复用性

```xml
<sql id="commonSelect">
    select id, username, password,name,gender,image,job,entrydate,dept_id,create_time,update_time
    from emp
</sql>
<select id="list" resultType="com.zhangjingqi.pojo.Emp">
    <include refid="commonSelect"/>

    <where>
        <if test=" name != null">
            name like concat('%',#{name},'%')
        </if>

        <if test=" gender != null">
            and gender = #{gender}
        </if>

        <if test="begin !=null and end !=null">
            and entrydate between #{begin} and #{end}
        </if>
    </where>
    order by update_time desc
</select>
```
