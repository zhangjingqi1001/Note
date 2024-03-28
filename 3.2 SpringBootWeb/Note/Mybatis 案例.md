[TOC]



# Mybatis 案例



# 一、 准备工作



**前置知识：**[Mybatis - 基础](https://blog.csdn.net/weixin_51351637/article/details/130662340?spm=1001.2014.3001.5502)



## 1.1 数据库表

```sql
-- 部门管理
create table dept(
 id int unsigned primary key auto_increment comment '主键ID',
 name varchar(10) not null unique comment '部门名称',
 create_time datetime not null comment '创建时间',
 update_time datetime not null comment '修改时间'
) comment '部门表';

-- 部门表测试数据
insert into dept (id, name, create_time, update_time) values(1,'学工
部',now(),now()),(2,'教研部',now(),now()),(3,'咨询部',now(),now()),
(4,'就业部',now(),now()),(5,'人事部',now(),now());
-- 员工管理(带约束)

create table emp (
 id int unsigned primary key auto_increment comment 'ID',
 username varchar(20) not null unique comment '用户名',
password varchar(32) default '123456' comment '密码',
 name varchar(10) not null comment '姓名',
 gender tinyint unsigned not null comment '性别, 说明: 1 男, 2 女',
 image varchar(300) comment '图像',
 job tinyint unsigned comment '职位, 说明: 1 班主任,2 讲师, 3 学工主
管, 4 教研主管, 5 咨询师',
 entrydate date comment '入职时间',
 dept_id int unsigned comment '部门ID',
 create_time datetime not null comment '创建时间',
 update_time datetime not null comment '修改时间'
) comment '员工表';

-- 员工表测试数据
INSERT INTO emp
 (id, username, password, name, gender, image, job,
entrydate,dept_id, create_time, update_time) VALUES
 (1,'jinyong','123456','金庸',1,'1.jpg',4,'2000-01-
01',2,now(),now()),
 (2,'zhangwuji','123456','张无忌',1,'2.jpg',2,'2015-01-
01',2,now(),now()),
 (3,'yangxiao','123456','杨逍',1,'3.jpg',2,'2008-05-
01',2,now(),now()),
 (4,'weiyixiao','123456','韦一笑',1,'4.jpg',2,'2007-01-
01',2,now(),now()),
 (5,'changyuchun','123456','常遇春',1,'5.jpg',2,'2012-12-
05',2,now(),now()),
 (6,'xiaozhao','123456','小昭',2,'6.jpg',3,'2013-09-
05',1,now(),now()),
 (7,'jixiaofu','123456','纪晓芙',2,'7.jpg',1,'2005-08-
01',1,now(),now()),
 (8,'zhouzhiruo','123456','周芷若',2,'8.jpg',1,'2014-11-
09',1,now(),now()),
 (9,'dingminjun','123456','丁敏君',2,'9.jpg',1,'2011-03-
11',1,now(),now()),
 (10,'zhaomin','123456','赵敏',2,'10.jpg',1,'2013-09-
05',1,now(),now()),
 (11,'luzhangke','123456','鹿杖客',1,'11.jpg',5,'2007-02-
01',3,now(),now()),
 (12,'hebiweng','123456','鹤笔翁',1,'12.jpg',5,'2008-08-
18',3,now(),now()),
 (13,'fangdongbai','123456','方东白',1,'13.jpg',5,'2012-11-
01',3,now(),now()),
 (14,'zhangsanfeng','123456','张三丰',1,'14.jpg',2,'2002-08-
01',2,now(),now()),
 (15,'yulianzhou','123456','俞莲舟',1,'15.jpg',2,'2011-05-
01',2,now(),now()),
 (16,'songyuanqiao','123456','宋远桥',1,'16.jpg',2,'2007-01-
01',2,now(),now()),
 (17,'chenyouliang','123456','陈友谅',1,'17.jpg',NULL,'2015-03-
21',NULL,now(),now());
```





## 1.2 Restfull规范

[Controller层开发、请求与响应、RESTful开发规范_springboot控制层代码规范_](https://blog.csdn.net/weixin_51351637/article/details/128042715)





## 1.3 封装结果类

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;//响应码，1 代表成功; 0 代表失败
    private String msg; //响应信息 描述字符串
    private Object data; //返回的数据

    //增删改 成功响应
    public static Result success() {
        return new Result(1, "success", null);
    }

    //查询 成功响应
    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    //失败响应
    public static Result error(String msg) {
        return new Result(0, msg, null);
    }
}
```



## 1.4 实体类

剩余的实体类都在 “mybatis-基础”中添加了

```java
/*部门类*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dept {
    private Integer id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

```





# 二、部门管理



## 2.1 查询全部部门信息

**SQL**

```java
@Mapper
public interface DeptMapper {
    @Select("select * from dept")
    List<Dept> list();
}
```



**测试接口：**

```java
@GetMapping("/depts")
public Result list() {
    log.info("查询全部数据");

   List<Dept> deptList =  deptService.list();
    return Result.success(deptList);
}
```



**返回结果：**

```json
{
    "code": 1,
    "msg": "success",
    "data": [
        {
            "id": 1,
            "name": "学工部",
            "createTime": "2023-05-13T11:16:16",
            "updateTime": "2023-05-13T11:16:16"
        },
        {
            "id": 2,
            "name": "教研部",
            "createTime": "2023-05-13T11:16:16",
            "updateTime": "2023-05-13T11:16:16"
        },
        {
            "id": 3,
            "name": "咨询部",
            "createTime": "2023-05-13T11:16:16",
            "updateTime": "2023-05-13T11:16:16"
        },
        {
            "id": 4,
            "name": "就业部",
            "createTime": "2023-05-13T11:16:16",
            "updateTime": "2023-05-13T11:16:16"
        },
        {
            "id": 5,
            "name": "人事部",
            "createTime": "2023-05-13T11:16:16",
            "updateTime": "2023-05-13T11:16:16"
        }
    ]
}
```





## 2.2 删除部门

**SQL**

```java
    @Delete("delete from dept where id= #{id}")
    void deleteById(Integer id);
```



**接口**

```java
@DeleteMapping("/depts/{id}")
public Result delete(@PathVariable Integer id) {
    log.info("根据Id删除部门：{}", id);
    deptService.deleteById(id);
    return Result.success();
}
```







## 2.3 新增部门

**SQL**

```java
    @Insert("insert  into dept(name,create_time,update_time) value (#{name},#{createTime},#{updateTime})")
    void add(Dept dept);
```



**接口**

```java
@PostMapping("/depts")
public Result add(@RequestBody Dept dept) {
    deptService.add(dept);
    return Result.success();
}
```







# 三、员工管理

## 3.1 分页查询

**SQL语句**

起始索引与页码、查询返回的记录数有关系

```sql
--  分页查询语法
--  参数1： 起始索引 = （页码-1）*每页展示的记录数
--  参数2： 查询返回的记录数
--  含义： 从第一条记录开始查询，一共查询五条记录
select * from emp LIMIT 0,5
```





**实体类封装分页查询的结果**

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageBean {
    private Long total; //总记录数
    private List rows;  //当前页数数据列表
}
```





**请求参数**

![image-20230514151904945](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230514151904945.png)





**SQL语句**

```java
        @Select("select count(*) from emp")
        public Long count();//查询总记录数

        @Select("select * from emp limit #{start},#{pageSize}")
        public List<Emp> page(Integer start,Integer pageSize); //分页查询获取列表数据

```



**接口代码**

```java
    @GetMapping("/emps")
    public Result page(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询，参数：{}，{}", page, pageSize);
//      调用Service分页查询
        PageBean pageBean = empService.page(page,pageSize);
        return Result.success(pageBean);
    }
```





**业务实现**

```java
    @Override
    public PageBean page(Integer page, Integer pageSize) {
//      TODO 获取总记录数
        Long count = empMapper.count();
//      TODO 获取分页数据
        Integer start = (page-1)*pageSize;

        List<Emp> empList = empMapper.page(start, pageSize);

        PageBean pageBean = new PageBean(count,empList);

        return pageBean;
    }
```







## 3.2 分页查询 - PageHelper插件

**两种方式对比：**所有的操作都是分页插件帮我们实现的，我们只需要正常的执行查询操作即可

![image-20230514172606165](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230514172606165.png)



**Maven坐标**

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.4.6</version>
</dependency>
```





**SQL**

```java
@Select("select * from emp")
public List<Emp> list();
```





**业务层**

```java
    @Override
    public PageBean page(Integer page, Integer pageSize) {
//     TODO  设置分页参数
        PageHelper.startPage(page,pageSize);
//     TODO  执行查询
        List<Emp> list = empMapper.list();
        Page<Emp> p = (Page<Emp>)list;

//     TODO  封装PageBean
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());

        return pageBean;
    }
```





**日志输出**

  仔细观察，就会发现插件所调用的方法就是 3.1 我们手写的SQL语句，只不过插件帮我们完成了而已。

> ==>  Preparing: SELECT count(0) FROM emp 
> ==> Parameters: 
> <==    Columns: count(0)
> <==        Row: 16
> <==      Total: 1
> ==>  Preparing: select * from emp LIMIT ? 
> ==> Parameters: 5(Integer)
> <==    Columns: id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time
> <==        Row: 4, weiyixiao, 123456, 韦一笑, 1, 4.jpg, 2, 2007-01-01, 2, 2023-05-13 11:16:16, 2023-05-13 11:16:16
> <==        Row: 5, changyuchun, 123456, 常遇春, 1, 5.jpg, 2, 2012-12-05, 2, 2023-05-13 11:16:16, 2023-05-13 11:16:16
> <==        Row: 6, xiaozhao, 123456, 小昭, 2, 6.jpg, 3, 2013-09-05, 1, 2023-05-13 11:16:16, 2023-05-13 11:16:16
> <==        Row: 7, jixiaofu, 123456, 纪晓芙, 2, 7.jpg, 1, 2005-08-01, 1, 2023-05-13 11:16:16, 2023-05-13 11:16:16
> <==        Row: 8, zhouzhiruo, 123456, 周芷若, 2, 8.jpg, 1, 2014-11-09, 1, 2023-05-13 11:16:16, 2023-05-13 11:16:16
> <==      Total: 5





## 3.3 分页查询 - 条件查询

![image-20230514151904945](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230514151904945.png)



**接口**

```java
//    @DateTimeFormat注解指定前端传输过来的日期格式
    @GetMapping("/emps")
    public Result page(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, Short gender,
                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate end) {
        log.info("分页查询，参数：{}，{}，{}，{},{},{}", page, pageSize,name,gender,begin,end);
//      调用Service分页查询
        PageBean pageBean = empService.page(page,pageSize,name,gender,begin,end);
        return Result.success(pageBean);
    }
```



**SQL - XML 映射文件**

```java
public List<Emp> list(String name, Short gender, LocalDate begin, LocalDate end);
```

```xml
<mapper namespace="com.zhangjingqi.mapper.EmpMapper">
    <select id="list" resultType="com.zhangjingqi.pojo.Emp">
        select *
        from emp
        <where>
            <if test="name !=null and name!=''">
                name like concat('%',#{name},'%')
            </if>
            <if test="gender !=null">
                and gender =#{gender}
            </if>
            <if test="begin !=null and end!=null">
                and entrydate between #{begin} and #{end}
            </if>
        </where>
        order by update_time desc
    </select>
</mapper>
```





**业务代码**

```java
    @Override
    public PageBean page(Integer page, Integer pageSize, String name, Short gender,LocalDate begin, LocalDate end) {
//     TODO  设置分页参数
        PageHelper.startPage(page,pageSize);
//     TODO  执行查询
        List<Emp> list = empMapper.list(name,  gender,  begin,  end);
        Page<Emp> p = (Page<Emp>)list;

//     TODO  封装PageBean
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());

        return pageBean;
    }
```



## 3.4 批量删除员工

**接口**

```java
@DeleteMapping("/{ids}")
public Result delete(@PathVariable List<Integer> ids) {
    log.info("批量删除操作，ids:{}",ids);
    empService.delete(ids);
    return Result.success();
}
```



**业务逻辑**

```java
@Override
public void delete(List<Integer> ids) {
 empMapper.delete( ids);
}
```





**SQL**

```java
    void delete(@Param("ids") List<Integer> ids);
```



**XML文件映射**

```xml
<delete id="delete">
    delete
    from emp
    <where>
        id in
        <foreach collection="ids" item="id" separator="," open="(" close=")">
            #{id}
        </foreach>
    </where>
</delete>
```



**参考资料：**[mybatis中foreach collection三种用法-腾讯云开发者社区-腾讯云 (tencent.com)](https://cloud.tencent.com/developer/article/1644999)

**再详细讲解一下foreach标签中的collection参数**

collection ： 指定要遍历的集合或数组，可以是 List、Set、Array、Map 等类型。遍历操作会把元素依次添加到 SQL 语句中。

- 在 MyBatis 中，`<foreach>` 标签的 `collection` 属性可以有三种用法：

  1. ***Java 集合和数组方式**

  可以将一个 Java 集合或数组传入方法作为参数，MyBatis 会自动将其转换为 Map 类型，其中 collection 属性指定为参数的名称。下面是使用集合方式的示例：

  ```xml
  <select id="getUsersByIds" parameterType="map" resultType="User">
      SELECT *
      FROM user
      WHERE id IN
      <foreach collection="ids" item="id" open="(" separator="," close=")">
          #{id}
      </foreach>
  </select>
  ```

  在该示例中，`ids` 是传入方法的 List 类型参数的名称，在 SQL 语句中用 `#{id}` 获取集合中的每个元素。

  ​      下面这种方式也是

  ```java
      void delete(@Param("ids") List<Integer> ids);
  ```

  ```xml
  <delete id="delete">
      delete
      from emp
      <where>
          id in
          <foreach collection="ids" item="id" separator="," open="(" close=")">
              #{id}
          </foreach>
      </where>
  </delete>
  ```

  

  2. **嵌套 SQL 语句方式**

  除了直接使用集合参数方式，还可以在 `<foreach>` 标签的 `collection` 属性中直接编写 SQL 语句或调用已定义的 mapper，返回指定列表数据作为集合参数。下面是使用 SQL 语句方式的示例：

  ```xml
  <select id="getUsersByAge" parameterType="int" resultType="User">
      SELECT *
      FROM user
      WHERE age IN
      <foreach collection="select id from age_table where age = #{age}" item="id" open="(" separator="," close=")">
          #{id}
      </foreach>
  </select>
  ```

  在该示例中，使用 SQL 语句方式获取年龄等于某个值的用户 ID 列表，后面进行 in 查询。

  3. **数组方式**

  可以将一个简单的 Java 数组传递给一个方法，下面是使用数组方式的示例：

  ```xml
  <select id="getUsersByIds" parameterType="array" resultType="User">
      SELECT *
      FROM user
      WHERE id IN
      <foreach collection="array" item="id" open="(" separator="," close=")">
          #{id}
      </foreach>
  </select>
  ```

  在该示例中，Mapper 方法接收的参数为简单的数组，直接在 SQL 中使用 `[?]`占位符，并将 array 作为 `collection` 传入即可。





## 3.5 新增员工

**接口**

```java
@PostMapping
public Result save(@RequestBody Emp emp){

    empService.save(emp);

    return Result.success();
}
```



**SQL**

```java
@Insert("insert into emp(username,name,gender,image,job,entrydate,dept_id,create_time,update_time)" +
        "values(#{username},#{name},#{gender},#{image},#{job},#{entrydate},#{deptId},#{createTime},#{updateTime})")
public void insert(Emp emp);
```



**业务层**

```java
@Override
public void save(Emp emp) {
    emp.setUpdateTime(LocalDateTime.now());
    emp.setCreateTime(LocalDateTime.now());

    empMapper.insert(emp);
}
```







## 3.6 修改员工

### 3.6.1 查询回显

**接口**

```java
@GetMapping("/emps/{id}")
public Result getById(@PathVariable Integer id){
    log.info("根据ID查询员工信息：id{}",id);
    Emp emp = empService.getById(id);
    return Result.success(emp);
}
```



**SQL**

```java
@Select("select * from emp where id=#{id}")
public Emp getById(Integer id);
```







### 3.6.2 修改员工

**接口**

```java
@PutMapping
public Result update(@RequestBody Emp emp){
    log.info("更新员工信息");
    empService.update(emp);
    return Result.success();
}
```



**业务实现**

```java
@Override
public void update(Emp emp) {
    emp.setUpdateTime(LocalDateTime.now());
    empMapper.update(emp);
}
```



**XML文件映射**

```xml
<update id="update">
    update emp
    <set>
        <if test="username != null and username != ''">
            username = #{username},
        </if>
        <if test="password != null and password != ''">
            password = #{password},
        </if>
        <if test="name != null and name != ''">
            name = #{name},
        </if>
        <if test="gender != null">
            gender = #{gender},
        </if>
        <if test="image != null and image != ''">
            image = #{image},
        </if>
        <if test="job != null">
            job = #{job},
        </if>
        <if test="entrydate != null">
            entrydate = #{entrydate},
        </if>
        <if test="deptId != null">
            dept_id = #{deptId},
        </if>
        <if test="updateTime != null">
            update_time = #{updateTime}
        </if>
    </set>
    where id = #{id}
</update>
```











# 四、文件上传



## 4.0 html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>上传文件</title>
</head>
<body>
<form action="/upload" method="post" enctype="multipart/form-data" >
    姓名：<input type="text" name="username">
    年龄：<input type="text" name="age">
    头像：<input type="file" name="image">
    <input type="submit" value="提交">
</form>
</body>
</html>
```



## 4.1 简介

* 文件上传，指将本地图片、视频、音频等文件上传到服务器，供其他用户浏览或下载的过程。



这是之前做的文件上传下载的笔记，但是是保存到本地的，知识点挺详细的

[ Springboot——文件的上传与下载(reggie)_](https://blog.csdn.net/weixin_51351637/article/details/130119958?spm=1001.2014.3001.5502)





## 4.2 本地存储

* 开发中基本不用这种方式。如果将文件存储在服务器磁盘当中，前端是没有办法直接访问的

* 如果说服务器中上传了大量的文件占满了磁盘，服务器是不容易扩容的

* 假如服务器的磁盘坏了，里面存储的所有数据全部丢失





**接口**

```java
    @PostMapping("/upload")
    public Result upload(String username, Integer age, MultipartFile image) throws IOException {
//      MultipartFile 介绍文件
//      TODO 文件的name（表单项中的名字），filename已经封装在MultipartFile中
        String originalFilename = image.getOriginalFilename();  // 文件原始名

//      TODO 构造唯一文件名（不能重复） - 采用uuid  通用唯一识别码，长度固定字符串，是不会重复的
        int index = originalFilename.lastIndexOf("."); //最后一个点的坐标

        String imageType = originalFilename.substring(index); //ru .jpg

        String imageName = UUID.randomUUID().toString()+imageType;
        log.info("新文件名：{}",imageName);
//        将文件存储在磁盘目录当中  E:\Note\SpringBootWeb\Note\image
//        image.transferTo(new File("E:/Note/SpringBootWeb/Note/image/"+originalFilename));
//        但是我们一般不使用文件原始名进行存储，因为用户A存储照片的名称为1.jpg,用户B存储照片的名称为1.jpg，那这样用户B的图片将用户A的图片覆盖了

//        TODO 将文件存储在磁盘目录当中  E:\Note\SpringBootWeb\Note\image
        image.transferTo(new File("E:/Note/SpringBootWeb/Note/image/"+imageName));
        return Result.success();
    }
```



**请求**

![image-20230515122926584](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230515122926584.png)



**当我们上传一个比较大的文件（超过1M）时，服务端就会出现异常**

> org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException: The field image exceeds its maximum permitted size of 1048576 bytes.
> 	at org.apache.tomcat.util.http.fileupload.impl.FileItemStreamImpl$1.raiseError(FileItemStreamImpl.java:117) ~[tomcat-embed-core-9.0.74.jar:9.0.74]
> 	at org.apache.tomcat.util.http.fileupload.util.LimitedInputStream.checkLimit(LimitedInputStream.java:76) ~[tomcat-embed-core-9.0.74.jar:9.0.74]
> 	at org.apache.tomcat.util.http.fileupload.util.LimitedInputStream.read(LimitedInputStream.java:135) ~[tomcat-embed-core-9.0.74.jar:9.0.74]
> 	



**解决方案**

```yaml
spring:
  servlet:
    multipart:
#     配置单个文件最大上传大小
      max-file-size: 10MB
#     配置单个请求最大上传大小（一次请求可以上传多个文件）
      max-request-size: 100MB
```



**MultipartFile类常用方法**

![image-20230515124555636](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230515124555636.png)



## 4.3 对象存储 OSS 

[文件上传-阿里云OSS-准备](https://www.bilibili.com/video/BV1m84y1w7Tb?p=148&spm_id_from=pageDriver&vd_source=c01240addcba226237f3c4781490fbae)

**帮助文档**： https://oss.console.aliyun.com/sdk

​                     https://help.aliyun.com/document_detail/32007.html?spm=a2c4g.52834.0.0.663862a9UbH61y





可以通过网络随时存储和调用包括文本、图片、音频和视频在内的各种文件



**SDK**：软件开发工具包，包括辅助软件开发的依赖（jar包）、代码示例等，都可以叫做SDK

**Bucket**:存储空间是用户用于存储对象（Object，就是文件）的容器，所有的对象都必须隶属于某个存储空间



### 4.3.1 maven坐标

因为我使用的java8，只需要引用这个坐标，如果是java9及其以上版本，需要在帮助文档中复制其他的

```xml
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-sdk-oss</artifactId>
    <version>3.15.1</version>
</dependency>
```





### 4.3.2 简单上传

**帮助文档**： https://help.aliyun.com/document_detail/84781.html?spm=a2c4g.84778.0.0.12271cc87u9r8x

```java
public class Demo {

    public static void main(String[] args) throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
//      TODO 我在华北2，所以用北京
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5t6aEYHiGAqeJ28Pz2mR";
        String accessKeySecret = "自己写自己的，不要用我的我没钱";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "picture-typora-zhangjingqi";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "1.jpg"; //最终在OSS中叫什么名字

//      填写本地文件的完整路径，例如D:/exampledir/exampleobject.txt
//      如果没有指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流
        String filePath = "E:\\Note\\SpringBootWeb\\Note\\image\\0bf39d2621167df89780626b602cfad.jpg";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = new FileInputStream(filePath);
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 设置该属性可以返回response。如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 如果上传成功，则返回200。
            System.out.println(result.getResponse().getStatusCode());
        }catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
```





## 4.4 案例集成OSS

* 上传员工的图像并保存起来

*  将上传的图像进行展示

  

  

  

**流程**

![image-20230515140126085](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230515140126085.png)





**接口**

```java
    @Autowired
    private AliOSSUtils aliOSSUtils;

    @PostMapping("/upload")
    public Result upload(MultipartFile image) throws IOException {

        log.info("文件上传，文件名:{}", image.getOriginalFilename());
//      调用阿里云OOS工具类进行上传
        String url = aliOSSUtils.upload(image);
        log.info("文件上传完成，url:{}",url);

        return Result.success(url);
    }
```





### 4.4.1 上传工具类

```java
/**
 * 阿里云 OSS 工具类
 */
@Component
public class AliOSSUtils {

    private String endpoint = "https://oss-cn-beijing.aliyuncs.com";
    private String accessKeyId = "LTAI5t6aEYHiGAqeJ28Pz2mR";
    private String accessKeySecret = "Dm4H7Lbq9NpDDmIEsC1mCX8U694C7b";
    private String bucketName = "picture-typora-zhangjingqi";

    /**
     * 实现上传图片到OSS
     */
    public String upload(MultipartFile file) throws IOException {
        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 避免文件覆盖
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        //文件访问路径  比如 https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/1.jpg
//         相当于把bucketName拼接在http://后面，把文件名字拼接在aliyuncs.com/ 后面
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }

}
```







# 五、 配置文件

## 5.1 介绍



**之前笔记**

[ 读取yaml配置文件中的基本数据+字符串_yaml配置字符串数组](https://blog.csdn.net/weixin_51351637/article/details/124026384)

[ springboot—YAML文件中读取Map集合并带有转义字符（问题记录）_yaml文件map](https://blog.csdn.net/weixin_51351637/article/details/129464820)



```yaml
# 对象/Map集合
user:
  name: zhangsan
  age: 18
  password: 123456

# 数组/List/Set集合
hobby:
  - java
  - game
  - sport
```





## 5.2 @ConfigurationProperties

前提是类中的属性名与yaml文件中属性名保持一致才能将yaml文件属性名对应的值自动填充到类中的属性名

**prefix**：表示前缀

![image-20230515153553960](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230515153553960.png)



* @Value注解只能一个一个的进行外部属性注入
*  @ConfigurationProperties可以批量的将外部属性配置注入到bean对象的属性中







**添加依赖 - 可添加也可不添加**

   我们可以添加下面的依赖，不添加也能使用，只不过Idea会有提示。添加之后会方便我们后续的开发。

   在properties或者yaml配置文件中会提示@ConfigurationProperties注解所在类中的属性。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
</dependency>
```
