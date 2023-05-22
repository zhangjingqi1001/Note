# Mysql基础



# 一、数据模型

## 1.1 关系型数据库与非关系型数据库



*  **关系型数据库 （RDBMS）**

    **概念**：建立在关系模型基础上，由多张表相互连接的**二维表**组成的数据库。

​         Mysql数据库就是一个关系型数据库

​        **二维表**：

​           使用表存储数据，格式统一，便于维护

​           使用SQL语言操作，标准统一，使用方便

![image-20230521110341537](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230521110341537.png)



*  **非关系型数据库**

​       不通过表结构存储数据的数据库。

​        比如Redis就是一个非关系型数据库





## 1.2 Mysql 数据模型

​      安装完MySQL之后，我们的计算机就成为Mysql数据库服务器，我们就可以通过客户端连接Mysql数据库管理系统DBMS，然后可以使用SQL语句，通过数据库管理系统来创建数据库，也可以通过数据库管理系统在指定的数据库中创建表

![image-20230521111429940](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230521111429940.png)



|    **名称**    |                  **全称**                  |               简称                |
| :------------: | :----------------------------------------: | :-------------------------------: |
|     数据库     |   存储数据的仓库，数据是有组织的进行存储   |          DataBase（DB）           |
| 数据库管理系统 |         操纵和管理数据库的大型软件         | DataBase Management System (DBMS) |
|      SQL       | 操作关系型数据库的编程语言，定义了一套操作 |  Structured Query Language (SQL)  |



# 二、SQL



## 2.1 SQL 通用语法

*  SQL语句可以单行或多行书写，以分号结尾



*  SQL语句可以使用空格/缩进来增强语句可读性



*  MySQL数据库的SQL语句不区分大小写，关键字建议使用大写

*  单行注释： --注释内容  或者  #注释内容
*  多行注释： /*注释内容*/





## 2.2 SQL分类

|  分类   |              全称              |                        说明                        |
| :-----: | :----------------------------: | :------------------------------------------------: |
| **DDL** |  **Data Definition Language**  | 数据定义语言，用来定义数据库对象(数据库，表，字段) |
| **DML** | **Data Manipulation Language** | 数据操作语言，用来对数据库表中的数据进行**增删改** |
| **DQL** |    **Data Query Language**     |     数据**查询**语言，用来查询数据库中表的记录     |
| **DCL** |   **Data Control Language**    |   数据控制语言，用来创建数据库用户、控制数据库的   |





## 2.3 DDL

​     **概念**：Data Definition Language，数据定义语言，用来**定义数据库对象(数据库，表，字段) **。



### 2.3.1 数据库操作

*  **查询所有数据库**

    ``` sql
show databases ;
    ```



*  **查询当前数据库**

```sql
select database() ;
```



*   **创建数据库**

```sql
create database [ if not exists ] 数据库名 [ default charset 字符集 ] [ collate 排序
规则 ] ;
```



​    数据库的**字符集**不建议设置为UTF-8，因为UTF-8的汉字存储长度是三个字节，但是数据库中有一些特殊字符是占用四个字节的，所以 **推荐使用utf8mb4**，因为支持四个字节





*  **删除**

```sql
DROP DATABASE [if exists] 数据库名;
```



*  **使用**

```sql
USE 数据库名;
```



### 2.3.2 表操作 — 创建 & 查询

*  **查询当前数据库所有表**

```sql
show tables;
```



*  **查看指定表结构**

``` sql
desc 表名 ;
```

​      可以查看到指定表的字段，字段的类型、是否可以为NULL，是否存在默认值等信息

![image-20230521140004198](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230521140004198.png)



*  **查询指定表的建表语句**

``` sql
show create table 表名 ;
```

​    通过这条指令，主要是用来查看建表语句的，而有部分参数我们在创建表的时候，并未指定也会查询到，因为这部分是数据库的默认值，如：存储引擎、字符集等。



>  CREATE TABLE `dept` (
>    `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键\r\nID',
>    `name` varchar(10) NOT NULL COMMENT '部门名称',
>    `create_time` datetime NOT NULL COMMENT '创建时间',
>    `update_time` datetime NOT NULL COMMENT '修改时间',
>    PRIMARY KEY (`id`),
>    UNIQUE KEY `name` (`name`)
>  ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表'



![image-20230521140039094](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230521140039094.png)



*  **创建表**

``` sql
CREATE TABLE 表名(
字段1 字段1类型 [ COMMENT 字段1注释 ],
字段2 字段2类型 [COMMENT 字段2注释 ],
字段3 字段3类型 [COMMENT 字段3注释 ],
......
字段n 字段n类型 [COMMENT 字段n注释 ]
) [ COMMENT 表注释 ] ;
```

**最后一个字段后面没有逗号**

``` sql
create table if not exists tb_user(
     id     int         COMMENT '编号',
	 name   varchar(50) COMMENT '姓名',
	 age    int         COMMENT '年龄',
	 gender varchar(1)  COMMENT '性别'

) comment '用户表';
```

![image-20230521140811004](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230521140811004.png)



### 2.3.3 表操作— 修改&删除

*  **添加字段**

     ``` sql
ALTER TABLE 表名 ADD 字段名 类型 (长度) [ COMMENT 注释 ] [ 约束 ];
     ```



为emp表增加一个新的字段”昵称”为nickname，类型为varchar(20)

```sql
ALTER TABLE emp ADD nickname varchar(20) COMMENT '昵称';
```





*  **修改数据类型**

```sql
ALTER TABLE 表名 MODIFY 字段名 新数据类型 (长度);
```



*  **修改字段名和类型**

``` sql
ALTER TABLE 表名 CHANGE 旧字段名 新字段名 类型 (长度) [ COMMENT 注释 ] [ 约束 ];
```



```sql
ALTER TABLE emp CHANGE nickname username varchar(30) COMMENT '昵称';
```



*  **删除字段**

   ``` sql
   ALTER TABLE 表名 DROP 字段名;
   ```

   将emp表的字段username删除

   ```sql
   ALTER TABLE emp DROP username;
   ```



*  **修改表名**

```sql
ALTER TABLE 表名 RENAME TO 新表名;
```



*  **删除表**

```sql
DROP TABLE [ IF EXISTS ] 表名;
```

 在删除表的时候，表中的全部数据也都会被删除。



*  **删除指定表，并重新创建该表**

```sql
TRUNCATE TABLE 表名;
```

truncate: 截断、截取

 在删除表的时候，表中的全部数据也都会被删除。



### 2.3.4 数据类型

**三类**：数值类型、字符串类型、日期时间类型。



#### 2.3.4.1 数值类型

 TINYINT 类似Java语言的byte，占位1个字节

SMALLINT 类似Java语言当中的short，占位2个字节

INT/INTEGER 类似Java语言中int，占用4个字节

BIGINT 类似Java语言当中的long，占用8个字节

**前五种是整形，后三种是浮点型**

FLOAT 类似Java语言中的float，占用4个字节

DOUBLE 类似Java语言中的double类型，占用8个字节

|    类型     |  大小   |                 有符号(SIGNED)范围                  |                  无符号(UNSIGNED)范围                  |        描述        |
| :---------: | :-----: | :-------------------------------------------------: | :----------------------------------------------------: | :----------------: |
|   TINYINT   | 1 byte  |                     (-128，127)                     |                        (0，255)                        |      小整数值      |
|  SMALLINT   | 2 bytes |                   (-32768，32767)                   |                       (0，65535)                       |      大整数值      |
|  MEDIUMINT  | 3bytes  |                 (-8388608，8388607)                 |                     (0，16777215)                      |      大整数值      |
| INT/INTEGER | 4bytes  |              (-2147483648，2147483647)              |                    (0，4294967295)                     |      大整数值      |
|   BIGINT    | 8bytes  |                   (-2^63，2^63-1)                   |                      (0，2^64-1)                       |     极大整数值     |
|    FLOAT    | 4bytes  |      (-3.402823466 E+38，3.402823466351 E+38)       |       0 和 (1.175494351 E-38，3.402823466 E+38)        |    单精度浮点数    |
|   DOUBLE    | 8bytes  | (-1.7976931348623157E+308，1.7976931348623157E+308) | 0 和(2.2250738585072014E-308，1.7976931348623157E+308) |   双精度浮点数值   |
|   DECIMAL   |         |             依赖于M(精度)和D(标度)的值              |               依赖于M(精度)和D(标度)的值               | 小数值(精确定点数) |



**什么是精度和标度？**

   如123.45， 精度就是整个长度5，标度就是小数点后位数2



**score double(4,1)是什么含义？**

   score字段的精度是4（100.0），只有一位小数



**怎么使用无符号范围？**

   age tinyint unsigned  表示age字段的范围是(0，255)





#### 2.3.4.2 字符串类型

|    类型    |         大小          |             描述             |
| :--------: | :-------------------: | :--------------------------: |
|    CHAR    |      0-255 bytes      |   定长字符串(需要指定长度)   |
|  VARCHAR   |     0-65535 bytes     |   变长字符串(需要指定长度)   |
|  TINYBLOB  |      0-255 bytes      | 不超过255个字符的二进制数据  |
|  TINYTEXT  |      0-255 bytes      |         短文本字符串         |
|    BLOB    |    0-65 535 bytes     |    二进制形式的长文本数据    |
|    TEXT    |    0-65 535 bytes     |          长文本数据          |
| MEDIUMBLOB |  0-16 777 215 bytes   | 二进制形式的中等长度文本数据 |
| MEDIUMTEXT |  0-16 777 215 bytes   |       中等长度文本数据       |
|  LONGBLOB  | 0-4 294 967 295 bytes |   二进制形式的极大文本数据   |
|  LONGTEXT  | 0-4 294 967 295 bytes |         极大文本数据         |



**char 与 varchar 的区别？**

​      char 与 varchar 都可以描述字符串，char是定长字符串，指定长度多长，就占用多少个字符，和字段值的长度无关 。而varchar是变长字符串，指定的长度为最大占用长度 。**相对来说，char的性能会更高些。**因为varchar在存储的时候会根据内容计算存储空间大小，所以性能低一些



*  **用户名 username ------> 长度不定, 最长不会超过50**

​         username varchar(50)



*  **性别 gender ---------> 存储值, 不是男,就是女**

​          gender char(1)



*  **手机号 phone --------> 固定长度为11**

​           phone char(11)





#### 2.3.4.3 日期时间类型

DATE、TIME、DATETIME使用的会多一些

|   类型    | 大小 |                   范围                    |        格式         | 描述                     |
| :-------: | :--: | :---------------------------------------: | :-----------------: | ------------------------ |
|   DATE    |  3   |         1000-01-01 至 9999-12-31          |     YYYY-MM-DD      | 日期值                   |
|   TIME    |  3   |          -838:59:59 至 838:59:59          |      HH:MM:SS       | 时间值或持续时间         |
|   YEAR    |  1   |               1901 至 2155                |        YYYY         | 年份值                   |
| DATETIME  |  8   | 1000-01-01 00:00:00 至9999-12-31 23:59:59 | YYYY-MM-DD HH:MM:SS | 混合日期和时间值         |
| TIMESTAMP |  4   | 1970-01-01 00:00:01 至2038-01-19 03:14:07 | YYYY-MM-DD HH:MM:SS | 混合日期和时间值，时间戳 |





#### 2.3.4.4  总结案例

**设计一张员工信息表，要求如下：**

1. 编号（纯数字）

2. 员工工号 (字符串类型，长度不超过10位)

3. 员工姓名（字符串类型，长度不超过10位）

4. 性别（男/女，存储一个汉字）

5. 年龄（正常人年龄，不可能存储负数）

6. 身份证号（二代身份证号均为18位，身份证中有X这样的字符）

7. 入职时间（取值年月日即可）



```sql
create table emp(
id int comment '编号',
workno varchar(10) comment '工号',
name varchar(10) comment '姓名',
gender char(1) comment '性别',
age tinyint unsigned comment '年龄',
idcard char(18) comment '身份证号',
entrydate date comment '入职时间'
) comment '员工表';
```







## 2.4 DML

​      DML英文全称是Data Manipulation Language(数据操作语言)，用来对数据库中表的数据记录进行**增、删、改操作**。

*  添加数据（INSERT）
*  修改数据（UPDATE）
*  删除数据（DELETE）





### 2.4.1 插入数据

*  **指定字段添加数据**

``` sql
INSERT INTO 表名 (字段名1, 字段名2, ...) VALUES (值1, 值2, ...); 1
```



比如：

```sql
insert into employee(id,workno,name,gender,age,idcard,entrydate)
values(1,'1','Itcast','男',10,'123456789012345678','2000-01-01');
```



*  **给全部字段添加数据**

```sql
INSERT INTO 表名 VALUES (值1, 值2, ...);
```



**但是数据的字段一定要进行对应**

```sql
insert into employee values(2,'2','张无忌','男',18,'123456789012345670','2005-01-01');
```



*  **批量添加数据**

``` sql
INSERT INTO 表名 (字段名1, 字段名2, ...)
VALUES (值1, 值2, ...), 
       (值1, 值2, ...), 
       (值1, 值2, ...) ;
```

 **或者**

```sql
INSERT INTO 表名 
VALUES (值1, 值2, ...), 
       (值1, 值2, ...), 
       (值1, 值2, ...) ;
```



**案例**：

```sql
insert into employee 
values(3,'3','韦一笑','男',38,'123456789012345670','2005-01-01'),
      (4,'4','赵敏','女',18,'123456789012345670','2005-01-01');
```





**注意事项**

*  插入数据时，指定的字段顺序需要与值的顺序是一一对应的。
*  字符串和日期型数据应该包含在引号中。
*  插入的数据大小，应该在字段的规定范围内。







### 2.4.2 更新数据

**语法**

```sql
UPDATE 表名 SET 字段名1 = 值1 , 字段名2 = 值2 , .... [ WHERE 条件 ] ;
```



**案例**

*  修改id为1的数据，将name修改为itheima

```sql
update employee set name = 'itheima' where id = 1;
```



*   修改id为1的数据, 将name修改为小昭, gender修改为 女

```sql
update employee set name = '小昭' , gender = '女' where id = 1;
```



*  将所有的员工入职日期修改为 2008-01-01

```sql
update employee set entrydate = '2008-01-01';
```



**注意**

​    修改语句的条件可以有，也可以没有，如果没有条件，则会修改整张表的所有数据。





### 2.4.3 删除数据

**语法**

```sql
DELETE FROM 表名 [ WHERE 条件 ] ;
```



*   **删除gender为女的员工**

```sql
delete from employee where gender = '女';
```



*  **删除所有员工**

```sql
delete from employee;
```





**注意事项**

*   DELETE 语句的条件可以有，也可以没有，如果没有条件，则会删除整张表的所有数据。
*  DELETE 语句不能删除某一个字段的值(可以使用UPDATE，将该字段值置为NULL即可)
*  当进行删除全部数据操作时，datagrip会提示我们，询问是否确认删除，我们直接点击Execute即可。









## 2.5 DQL 

​     DQL英文全称是Data Query Language(数据查询语言)，数据查询语言，用来查询数据库中表的记录。



### 2.5.1 数据准备

```sql
drop table if exists employee;

create table emp(
  id int comment '编号',
  workno varchar(10) comment '工号',
  name varchar(10) comment '姓名',
  gender char(1) comment '性别',
  age tinyint unsigned comment '年龄',
  idcard char(18) comment '身份证号',
  workaddress varchar(50) comment '工作地址',
  entrydate date comment '入职时间'
)comment '员工表';


INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (1, '00001', '柳岩666', '女', 20, '123456789012345678', '北京', '2000-01-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (2, '00002', '张无忌', '男', 18, '123456789012345670', '北京', '2005-09-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (3, '00003', '韦一笑', '男', 38, '123456789712345670', '上海', '2005-08-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (4, '00004', '赵敏', '女', 18, '123456757123845670', '北京', '2009-12-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (5, '00005', '小昭', '女', 16, '123456769012345678', '上海', '2007-07-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (6, '00006', '杨逍', '男', 28, '12345678931234567X', '北京', '2006-01-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (7, '00007', '范瑶', '男', 40, '123456789212345670', '北京', '2005-05-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (8, '00008', '黛绮丝', '女', 38, '123456157123645670', '天津', '2015-05-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (9, '00009', '范凉凉', '女', 45, '123156789012345678', '北京', '2010-04-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (10, '00010', '陈友谅', '男', 53, '123456789012345670', '上海', '2011-01-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (11, '00011', '张士诚', '男', 55, '123567897123465670', '江苏', '2015-05-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (12, '00012', '常遇春', '男', 32, '123446757152345670', '北京', '2004-02-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (13, '00013', '张三丰', '男', 88, '123656789012345678', '江苏', '2020-11-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (14, '00014', '灭绝', '女', 65, '123456719012345670', '西安', '2019-05-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (15, '00015', '胡青牛', '男', 70, '12345674971234567X', '西安', '2018-04-01');

INSERT INTO emp (id, workno, name, gender, age, idcard, workaddress, entrydate)
VALUES (16, '00016', '周芷若', '女', 18, null, '北京', '2012-06-01');
```





### 2.5.2 基本语法

```sql
SELECT
  字段列表
FROM
  表名列表
WHERE
  条件列表
GROUP BY
  分组字段列表
HAVING
  分组后条件列表
ORDER BY
  排序字段列表
LIMIT
  分页参数
```



*  基本查询（不带任何条件）
*  条件查询（WHERE）
*  聚合函数（count、max、min、avg、sum）
*  分组查询（group by）
*  排序查询（order by）
*  分页查询（limit）



### 2.5.3 基础查询

*  **查询多个字段**

``` sql
SELECT 字段1, 字段2, 字段3 ... 
FROM 表名 ;
```

  

``` sql
SELECT * 
FROM 表名 ;
```



**注意  \* 号代表查询所有字段，在实际开发中尽量少用（不直观、影响效率）。**



**案例**

```sql
select id,workno,name,gender,age,idcard,workaddress,entrydate 
from emp;
```







*  **字段设置别名**

``` sql
SELECT 字段1 [ AS 别名1 ] , 字段2 [ AS 别名2 ] ... 
FROM 表名;
```



```sql
SELECT 字段1 [ 别名1 ] , 字段2 [ 别名2 ] ... 
FROM 表名;
```



**案例**

```sql
select workaddress as '工作地址' 
from emp;
```



```sql
select workaddress '工作地址' 
from emp;
```





*  **去除重复记录**

```sql
SELECT DISTINCT 字段列表 
FROM 表名;
```



```sql
select distinct workaddress '工作地址'
from emp;
```



### 2.5.4 条件查询

**语法**

```sql
SELECT 字段列表 FROM 表名 
WHERE 条件列表 ;
```



**常见比较运算符条件**

|     比较运算符      |                   功能                   |
| :-----------------: | :--------------------------------------: |
|         \>          |                   大于                   |
|         \>=         |                 大于等于                 |
|          <          |                   小于                   |
|         <=          |                 小于等于                 |
|          =          |                   等于                   |
|      <> 或 !=       |                  不等于                  |
| BETWEEN ... AND ... |      在某个范围之内(含最小、最大值)      |
|       IN(...)       |       在in之后的列表中的值，多选一       |
|     LIKE 占位符     | 模糊匹配(_匹配单个字符, %匹配任意个字符) |
|       IS NULL       |                  是NULL                  |



**常见逻辑运算符**

| 逻辑运算符 |            功能             |
| :--------: | :-------------------------: |
| AND 或 &&  |   并且 (多个条件同时成立)   |
| OR 或 \|\| | 或者 (多个条件任意一个成立) |
|  NOT 或 !  |          非 , 不是          |



**案例**

*  查询年龄等于18 或 20 或 40 的员工信息

```sql
select *
from emp 
where age = 18 or age = 20 or age =40;

select * 
from emp 
where age in(18,20,40);
```



*  查询姓名为两个字的员工信息 

```sql
select * 
from emp 
where name like '__';
```



*   查询身份证号最后一位是X的员工信息

```sql
select * 
from emp 
where idcard like '%X';

select * 
from emp 
where idcard like '_________________X';
```





### 2.5.4 聚合函数

**将一列数据作为一个整体，进行纵向计算**



**语法**

```sql
SELECT 聚合函数(字段列表) 
FROM 表名 ;
```



**NULL值是不参与所有聚合函数运算!!!!!!!**



**常见的聚合函数**

| 函数  |   功能   |
| :---: | :------: |
| count | 统计数量 |
|  max  |  最大值  |
|  min  |  最小值  |
|  avg  |  平均值  |
|  sum  |   求和   |



**案例**

*  统计该企业员工数量

```sql
-- 统计的是总记录数,null也统计
select count(*) from emp; 

-- 统计的是idcard字段不为null的记录数
select count(idcard) from emp; 
```



下面等同于select count(*) from emp; 

```sql
select count(1) from emp; 
```



>  ​      "count(1)"是SQL查询语句中的一种写法，用于对一张表进行计数。它的作用是统计表中所有的行数，可以用来查询表中数据的总量。与其等价的写法是"count(*)"，它也可以用来计数。不同的数据库系统在使用count函数时可能存在些许差异，但通常都支持这两种写法。



### 2.5.5 分组查询

**语法**：

```sql
SELECT 字段列表 
FROM 表名 [ WHERE 条件 ] 
GROUP BY 分组字段名 
[HAVING 分组后过滤条件 ];
```



>  **where与having区别?**
>
>  *  **执行时机不同**：where是分组之前进行过滤，不满足where条件，不参与分组；而having是分组之后对结果进行过滤。
>
>  *  **判断条件不同**：where不能对聚合函数进行判断，而having可以。



**注意事项**

*  **分组之后，查询的字段一般为聚合函数和分组字段，查询其他字段无任何意义。**

*  **执行顺序**: where > 聚合函数 > having 。

*  支持**多字段分组**, 具体语法为 : group by columnA,columnB



**案例**

*   根据性别分组 , 统计男性员工 和 女性员工的数量

```sql
select gender, count(*)
from emp
group by gender ;
```



*  根据性别分组 , 统计男性员工 和 女性员工的平均年龄

```sql
select gender, avg(age) 
from emp 
group by gender ;
```



*  查询年龄小于45的员工 , 并根据工作地址分组 , 获取员工数量大于等于3的工作地址

```sql
select workaddress, count(*) address_count 
from emp 
where age < 45 
group by workaddress 
having address_count >= 3;
```



*   **统计各个工作地址上班的男性及女性员工的数量**

``` sql
select workaddress, gender, count(*) '数量' 
from emp 
group by gender , workaddress;
```





### 2.5.6 排序查询

排序在日常开发中是非常常见的一个操作，有升序排序，也有降序排序。

**语法**

```sql
SELECT 字段列表 
FROM 表名 
ORDER BY 字段1 排序方式1 , 字段2 排序方式2 ;
```



**排序方式**

*  ASC : 升序(默认值)，从小到大
*  DESC: 降序，从大到小



>  **注意事项**：
>
>  *  如果是升序, 可以不指定排序方式ASC ;
>
>  *  如果是多字段排序，当第一个字段值相同时，才会根据第二个字段进行排序 ;



**案例**

*  根据年龄对公司的员工进行升序排序

``` sql
select * from emp order by age asc;

select * from emp order by age;
```



*  根据年龄对公司的员工进行升序排序 , 年龄相同 , 再按照入职时间进行降序排序

```sql
select * 
from emp 
order by age asc , entrydate desc;
```







### 2.5.7 分页查询

   分页操作在业务系统开发时，也是非常常见的一个功能，我们在网站中看到的各种各样的分页条，后台都需要借助于数据库的分页操作。

​     

**语法**

```sql
SELECT 字段列表 FROM 表名 LIMIT 起始索引, 查询记录数 ;
```



>  **注意事项**:
>
>  *  起始索引从0开始，起始索引 = （查询页码 - 1）* 每页显示记录数。
>
>  *  分页查询是数据库的方言，不同的数据库有不同的实现，MySQL中是LIMIT。
>
>  *  如果查询的是第一页数据，起始索引可以省略，直接简写为 limit 10。



**案例**

*  查询第1页员工数据, 每页展示10条记录

```sql
select * from emp limit 0,10;
select * from emp limit 10;
```



*  查询第2页员工数据, 每页展示10条记录 --------> (页码-1)*页展示记录数

```sql
select * from emp limit 10,10;
```



### 2.5.8 执行顺序

 ![image-20230521183907907](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230521183907907.png)







## 2.6 DCL

​       DCL英文全称是**Data Control Language**(数据控制语言)，用来管理数据库用户、控制数据库的访问权限。



### 2.6.1 管理用户

*  **查询用户**

```sql
select * 
from mysql.user;
```

  **或者**

```sql
use mysql;

select * 
from user;
```

![image-20230521185226639](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230521185226639.png)



>  ​       其中 Host代表当前用户访问的主机, 如果为localhost, 仅代表只能够在当前本机访问，是不可以远程访问的。 User代表的是访问该数据库的用户名。
>
>  ​      在MySQL中需要通过Host和User来唯一标识一个用户。



*   **创建用户**

   ```sql
   CREATE USER '用户名'@'主机名' IDENTIFIED BY '密码';
   ```

​       仅仅是创建了用户，并没有权限访问其他数据库



**创建用户itcast, 只能够在当前主机localhost访问, 密码123456**

```sql
create user 'itcast'@'localhost' identified by '123456'; 
```



**创建用户heima, 可以在任意主机访问该数据库, 密码123456**

```sql
create user 'heima'@'%' identified by '123456';
```





*  **修改用户密码**

```sql
ALTER USER '用户名'@'主机名' IDENTIFIED WITH mysql_native_password BY '新密码' ;
```



**修改用户heima的访问密码为1234**

```sql
alter user 'heima'@'%' identified with mysql_native_password by '1234';
```



*  **删除用户**

   ```sql
   DROP USER '用户名'@'主机名' ;
   ```

**删除 itcast@localhost 用户**

```sql
drop user 'itcast'@'localhost';
```





>  **注意事项**
>
>  *   在MySQL中需要通过用户名@主机名的方式，来唯一标识一个户。
>  *   主机名可以使用 % 通配。
>  *   这类SQL开发人员操作的比较少，主要是DBA（ Database Administrator 数据库管理员）使用。





### 2.6.2 权限控制

**权限官方文档**：[MySQL :: MySQL 8.0 Reference Manual :: 6.2.2 Privileges Provided by MySQL](https://dev.mysql.com/doc/refman/8.0/en/privileges-provided.html)



**常用权限**

|        权限         |        说明        |
| :-----------------: | :----------------: |
| ALL, ALL PRIVILEGES |      所有权限      |
|       SELECT        |      查询数据      |
|       INSERT        |      插入数据      |
|       UPDATE        |      修改数据      |
|       DELETE        |      删除数据      |
|        ALTER        |       ALTER        |
|        DROP         | 删除数据库/表/视图 |
|       CREATE        |   创建数据库/表    |



*  **查询权限**

```sql
SHOW GRANTS FOR '用户名'@'主机名' ;
```



```sql
show grants for 'heima'@'%';
```



*  **授予权限**

```sql
GRANT 权限列表 ON 数据库名.表名 TO '用户名'@'主机名';
```



 授予 'heima'@'%' 用户itcast数据库所有表的所有操作权限

```sql
grant all on itcast.* to 'heima'@'%';
```





*  **撤销权限**

```mysql
REVOKE 权限列表 ON 数据库名.表名 FROM '用户名'@'主机名';
```



```sql
revoke all on itcast.* from 'heima'@'%';
```







>  **注意事项**：
>
>  *   多个权限之间，使用逗号分隔
>
>  *  授权时， 数据库名和表名可以使用 * 进行通配，代表所有。









# 三、函数

**函数 是指一段可以直接被另一段程序调用的程序或代码**



**四类**： 字符串函数、数值函数、日期函数、流程函数





## 3.1 字符串函数

|           函数           |                           功能                            |
| :----------------------: | :-------------------------------------------------------: |
|   CONCAT(S1,S2,...Sn)    |       字符串拼接，将S1，S2，... Sn拼接成一个字符串        |
|        LOWER(str)        |                  将字符串str全部转为小写                  |
|        UPPER(str)        |                  将字符串str全部转为大写                  |
|     LPAD(str,n,pad)      | 左填充，用字符串pad对str的左边进行填充，达到n个字符串长度 |
|     RPAD(str,n,pad)      | 右填充，用字符串pad对str的右边进行填充，达到n个字符串长度 |
|        TRIM(str)         |                去掉字符串头部和尾部的空格                 |
| SUBSTRING(str,start,len) |      返回从字符串str从start位置起的len个长度的字符串      |



*  **concat ：字符串拼接**

```sql
select concat('hello','Mysql')
```

**结果**：helloMysql



*  **lower：全部转小写**

```sql
SELECT LOWER('Hello')
```

**结果**：hello



*  **upper : 全部转大写**

```sql
SELECT UPPER('Hello')
```

**结果**：HELLO



*  **lpad : 左填充**

```sql
SELECT LPAD('Hello',10,'Mysql')
```

**结果**：MysqlHello



*   **rpad : 右填充**

```sql
SELECT rpad('Hello',10,'Mysql')
```

**结果**：HelloMysql



*   **trim : 去除头尾空格**

```sql
select trim(' Hello MySQL ');
```

**结果**：Hello MySQL



*   **substring : 截取子字符串**

```sql
select substring('Hello MySQL',1,5);
```

**结果**:Hello





**案例**

  由于业务需求变更，企业员工的工号统一为5位数，目前不足五位数的全部在前面补0.

 ```sql
update emp set workno = lpad(workno, 5, '0');
 ```



 





## 3.2 数值函数





|    函数    |                功能                |
| :--------: | :--------------------------------: |
|  CEIL(x)   |              向上取整              |
|  FLOOR(x)  |              向下取整              |
|  MOD(x,y)  |            返回x/y的模             |
|   RAND()   |         返回0~1内的随机数          |
| ROUND(x,y) | 求参数x的四舍五入的值，保留y位小数 |



**案例**

通过数据库的函数，生成一个六位数的随机验证码。

```sql
SELECT  lpad(floor(rand()*1000000) ,6,'0')
```



```sql
select lpad(round(rand()*1000000 , 0), 6, '0');
```









## 3.3 日期函数

|               函数                |                           功能                            |
| :-------------------------------: | :-------------------------------------------------------: |
|             CURDATE()             |                       返回当前日期                        |
|             CURTIME()             |                       返回当前时间                        |
|               NOW()               |                    返回当前日期和时间                     |
|            YEAR(date)             |                    获取指定date的年份                     |
|            MONTH(date)            |                    获取指定date的月份                     |
|             DAY(date)             |                    获取指定date的日期                     |
| DATE_ADD(date, INTERVAL exprtype) |     返回一个日期/时间值加上一个时间间隔expr后的时间值     |
|       DATEDIFF(date1,date2)       | 返回起始时间date1 和 结束时间date2之间的天（date1-date2） |





*  date_add：增加指定的时间间隔

```sql
select date_add(now(), INTERVAL 70 YEAR );

select date_add(now(), INTERVAL 70 MONTH );

select date_add(now(), INTERVAL 70 DAY );
```



*   datediff：获取两个日期相差的天数

```sql
-- -61
select datediff('2021-10-01', '2021-12-01');
```





**案例**

​      查询所有员工的入职天数，并根据入职天数倒序排序。

 ```sql
ELECT name , datediff(now(),entrydate) as date
from emp
ORDER BY date DESC

select name, datediff(curdate(), entrydate) as 'entrydays' 
from emp 
order by entrydays desc;
 ```









## 3.4 流程函数

可以在SQL语句中实现条件筛选，从而提高语句的效率。

 

|                             函数                             |                           功能                            |
| :----------------------------------------------------------: | :-------------------------------------------------------: |
|                      IF(value , t , f)                       |            如果value为true，则返回t，否则返回f            |
|                   IFNULL(value1 , value2)                    |       如果value1不为空，返回value1，否则返回value2        |
|   CASE WHEN [ val1 ] THEN [res1] ... ELSE [ default ] END    |    如果val1为true，返回res1，... 否则返回default默认值    |
| CASE [ expr ] WHEN [ val1 ] THEN [res1] ... ELSE [ default ] END | 如果expr的值等于val1，返回res1，... 否则返回default默认值 |



*  **case when then else end**

**需求**: 

​    查询emp表的员工姓名和工作地址 (北京/上海 ----> 一线城市 , 其他 ----> 二线城市)

```sql
select
   name,
  ( case workaddress when '北京' then '一线城市' when '上海' then '一线城市' else '二线城市' end ) as '工作地址'
	
from emp;
```









# 四、约束



## 4.1 分类

**概念**：约束是作用于表中字段上的规则，用于限制存储在表中的数据。



**目的**：保证数据库中数据的正确、有效性和完整性。



|           约束           |                           描述                           |   关键字    |
| :----------------------: | :------------------------------------------------------: | :---------: |
|         非空约束         |                限制该字段的数据不能为null                |  NOT NULL   |
|         唯一约束         |          保证该字段的所有数据都是唯一、不重复的          |   UNIQUE    |
|         主键约束         |         主键是一行数据的唯一标识，要求非空且唯一         | PRIMARY KEY |
|         默认约束         |      保存数据时，如果未指定该字段的值，则采用默认值      |   DEFAULT   |
| 检查约束(8.0.16版本之后) |                 保证字段值满足某一个条件                 |    CHECK    |
|         外键约束         | 用来让两张表的数据之间建立连接，保证数据的一致性和完整性 | FOREIGN KEY |



>  **注意**
>
>     约束是作用于表中字段上的，可以在创建表/修改表的时候添加约束。



## 4.2 约束演示

![image-20230522101709164](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230522101709164.png)





```sql
create table tb_user(
  id     int          PRIMARY KEY AUTO_INCREMENT  COMMENT 'ID唯一标识',
	
	name   varchar(10)  NOT NULL UNIQUE             COMMENT '姓名',
	
	age    int          CHECK  (0<age&& age<=120)   COMMENT '年龄',
	
	status char(1)      DEFAULT(1)                  COMMENT '状态',
	
	gender char(1)                                  COMMENT '状态'

);
```





## 4.3 外键约束



**外键**：用来让两张表的数据之间建立连接，从而保证数据的一致性和完整性。



### 4.3.1 准备数据

```sql
create table dept(

  id int auto_increment comment 'ID' primary key,

  name varchar(50) not null comment '部门名称'

)comment '部门表';


INSERT INTO dept (id, name) VALUES (1, '研发部'), (2, '市场部'),(3, '财务部'), (4,'销售部'), (5, '总经办');

create table emp(
  id int auto_increment comment 'ID' primary key,
  name varchar(50) not null comment '姓名',
  age int comment '年龄',
  job varchar(20) comment '职位',
  salary int comment '薪资',
  entrydate date comment '入职时间',
  managerid int comment '直属领导ID',
  dept_id int comment '部门ID'
)comment '员工表';


INSERT INTO emp (id, name, age, job,salary, entrydate, managerid, dept_id)
VALUES
(1, '金庸', 66, '总裁',20000, '2000-01-01', null,5),(2, '张无忌', 20,'项目经理',12500, '2005-12-05', 1,1),
(3, '杨逍', 33, '开发', 8400,'2000-11-03', 2,1),(4, '韦一笑', 48, '开发',11000, '2002-02-05', 2,1),
(5, '常遇春', 43, '开发',10500, '2004-09-07', 3,1),(6, '小昭', 19, '程序员鼓励师',6600, '2004-10-12', 2,1);
```



![image-20230522103445309](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230522103445309.png)





### 4.3.2 语法

*  添加外键

``` sql
CREATE TABLE 表名(
字段名 数据类型,
    
...
    
[CONSTRAINT] [外键名称] FOREIGN KEY (外键字段名) REFERENCES 主表 (主表列名)
);
```



```sql
ALTER TABLE 表名 ADD CONSTRAINT 外键名称 FOREIGN KEY (外键字段名) REFERENCES 主表 (主表列名) ;
```



*  删除外键

```sql
ALTER TABLE 表名 DROP FOREIGN KEY 外键名称;
```



### 4.3.3 删除/更新行为

​         添加了外键之后，再删除父表数据时产生的约束行为，我们就称为删除/更新行为。具体的删除/更新行为有以下几种

| 行为        | 说明                                                         |
| ----------- | ------------------------------------------------------------ |
| NO ACTION   | 当在父表中删除/更新对应记录时，首先检查该记录是否有对应外键，如果有则不允许删除/更新。 (与 RESTRICT 一致) 默认行为 |
| RESTRICT    | 当在父表中删除/更新对应记录时，首先检查该记录是否有对应外键，如果有则不允许删除/更新。 (与 NO ACTION 一致) 默认行为 |
| CASCADE     | 当在父表中删除/更新对应记录时，首先检查该记录是否有对应外键，如果有，则也删除/更新外键在子表中的记录。 |
| SET NULL    | 当在父表中删除对应记录时，首先检查该记录是否有对应外键，如果有则设置子表中该外键值为null（这就要求该外键允许取null）。 |
| SET DEFAULT | 父表有变更时，子表将外键列设置成一个默认的值 (Innodb不支持)  |



**具体语法**

   将行为设置为CASCADE

```sql
ALTER TABLE 表名 ADD CONSTRAINT 外键名称 FOREIGN KEY (外键字段) REFERENCES 主表名 (主表字段名) ON UPDATE CASCADE ON DELETE CASCADE;
```







