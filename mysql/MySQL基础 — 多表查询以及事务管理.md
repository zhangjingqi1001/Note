[TOC]



# MySQL基础 — 多表查询以及事务管理



# 一、多表查询

## 1.1 对应关系

*  **一对一**

​       多用于单表拆分，将一张表的基础字段放在一张表中，其他详情字段放在另一张表中，以提升操作效率

​       **在任意一方加入外键，关联另外一方的主键，并且设置外键为唯一的(UNIQUE)**

​       因为是一对一，所以需要设置唯一约束

![image-20230522112238330](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230522112238330.png)



*  **一对多**

​        **在多的一方建立外键，指向一的一方的主键**

![image-20230522112109287](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230522112109287.png)





*  **多对多**

​      **建立第三张中间表，中间表至少包含两个外键，分别关联两方主键**

![image-20230522112143302](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230522112143302.png)

## 1.2 准备数据

**学生表**

```sql
create table student(

  id int auto_increment primary key comment '主键ID',
  name varchar(10) comment '姓名',
	
  no varchar(10) comment '学号'
	
) comment '学生表';

insert into student values (null, '黛绮丝', '2000100101'),(null, '谢逊','2000100102'),
                           (null, '殷天正', '2000100103'),(null, '韦一笑', '2000100104');
													 
```



**课程表**

```sql
create table course(

  id int auto_increment primary key comment '主键ID',
	
  name varchar(10) comment '课程名称'
	
) comment '课程表';

insert into course values (null, 'Java'), (null, 'PHP'), 
                          (null , 'MySQL') ,(null, 'Hadoop');		
										 
```



**学生课表关联表**

```sql
create table student_course(

  id int auto_increment comment '主键' primary key,
	
  studentid int not null comment '学生ID', 
	
  courseid int not null comment '课程ID',
	
  constraint fk_courseid foreign key (courseid) references course (id),
	
  constraint fk_studentid foreign key (studentid) references student (id)
	
)comment '学生课程中间表';

insert into student_course values (null,1,1),(null,1,2),(null,1,3),
                                  (null,2,2),(null,2,3),(null,3,4);
```



**dept表**

```sql
-- 创建dept表，并插入数据
create table dept(
  id int auto_increment comment 'ID' primary key,
	
  name varchar(50) not null comment '部门名称'
	
)comment '部门表';

INSERT INTO dept (id, name) VALUES (1, '研发部'), (2, '市场部'),(3, '财务部'),
                                   (4,'销售部'), (5, '总经办'), (6, '人事部');
```


**创建emp表**

```sql
-- 创建emp表，并插入数据
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

-- 添加外键
alter table emp add constraint fk_emp_dept_id foreign key (dept_id) references dept(id);


INSERT INTO emp (id, name, age, job,salary, entrydate, managerid, dept_id)
VALUES
(1, '金庸', 66, '总裁',20000, '2000-01-01', null,5),
(2, '张无忌', 20, '项目经理',12500, '2005-12-05', 1,1),
(3, '杨逍', 33, '开发', 8400,'2000-11-03', 2,1),
(4, '韦一笑', 48, '开发',11000, '2002-02-05', 2,1),
(5, '常遇春', 43, '开发',10500, '2004-09-07', 3,1),
(6, '小昭', 19, '程序员鼓励师',6600, '2004-10-12', 2,1),
(7, '灭绝', 60, '财务总监',8500, '2002-09-12', 1,3),
(8, '周芷若', 19, '会计',48000, '2006-06-02', 7,3),
(9, '丁敏君', 23, '出纳',5250, '2009-05-13', 7,3),
(10, '赵敏', 20, '市场部总监',12500, '2004-10-12', 1,2),
(11, '鹿杖客', 56, '职员',3750, '2006-10-03', 10,2),
(12, '鹤笔翁', 19, '职员',3750, '2007-05-09', 10,2),
(13, '方东白', 19, '职员',5500, '2009-02-12', 10,2),
(14, '张三丰', 88, '销售总监',14000, '2004-10-12', 1,4),
(15, '俞莲舟', 38, '销售',4600, '2004-10-12', 14,4),
(16, '宋远桥', 40, '销售',4600, '2004-10-12', 14,4),
(17, '陈友谅', 42, null,2000, '2011-10-12', 1,null);

```



## 1.3 概述





**问：select * from emp , dept 最终结果为什么是102条？** 

​    emp 记录17条，dept 记录6条，17*6=102

​    其实就是员工表emp所有的记录(17) 与 部门表dept所有记录(6) 的所有组合情况，这种现象称为**笛卡尔积**，在这些组合情况中有许多记录是无用的，我们需要消除笛卡尔积

![image-20230522155734067](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230522155734067.png)



**笛卡尔积**

   左边集合与右边集合所有情况

![image-20230522160308623](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230522160308623.png)





**怎么消除笛卡尔积？**

​     多表连接时添加条件即可

```sql
select * from emp , dept where emp.dept_id = dept.id;
```



![image-20230522160614331](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230522160614331.png)





## 1.4 内连接

内连接查询的是两张表**交集**部分的数据。(也就是绿色部分的数据)

![image-20230522161208113](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230522161208113.png)





**内连接分为两种：隐式内连接、显式内连接**



*  隐式内连接

```sql
SELECT 字段列表 
FROM 表1 , 表2
WHERE 条件 ... ;
```



*  显式内连接

   ```sql
   SELECT 字段列表 
   FROM 表1 [ INNER ] 
   JOIN 表2 ON 连接条件 ... ;
   ```

   



**案例**

*  查询每一个员工的姓名 , 及关联的部门的名称 (显式内连接实现)

```sql
SELECT emp.name '员工姓名', dept.name '部门名称'
FROM emp
 join dept on emp.dept_id = dept.id
 
```



*  查询每一个员工的姓名 , 及关联的部门的名称 (隐式内连接实现)

```sql
select emp.name , dept.name
from emp , dept 
where emp.dept_id = dept.id ;
```



## 1.5 外连接

外连接分为两种，分别是：**左外连接 和 右外连接。**

![image-20230522161208113](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230522161208113.png)







*  **左外连接**

```sql
SELECT 字段列表
FROM 表1 
LEFT [ OUTER ] JOIN 表2 ON 条件 ... ;
```

左外连接相当于查询表1(左表)的所有数据，当然也包含表1和表2交集部分的数据。



*  **右外连接**

```sql
SELECT 字段列表 
FROM 表1
RIGHT [ OUTER ] JOIN 表2 ON 条件 ... ;
```



​     右外连接相当于查询表2(右表)的所有数据，当然也包含表1和表2交集部分的数据



**案例**

*  查询emp表的所有数据, 和对应的部门信息

```sql
select e.*, d.name 
from emp e 
left outer join dept d on e.dept_id = d.id;
```





*  查询dept表的所有数据, 和对应的员工信息(右外连接)

```sql
select d.*, e.* 
from emp e 
right outer join dept d on e.dept_id = d.id;
```



>  **注意事项**
>
>  ​     左外连接和右外连接是可以相互替换的，只需要调整在连接查询时SQL中，表结构的先后顺序就可以了。而我们在日常开发使用时，**更偏向于左外连接**





## 1.6 自连接

​     是把一张表连接查询多次。

```sql
SELECT 字段列表 
FROM 表A 别名A 
JOIN 表A 别名B ON 条件 ... ;
```





>  ​    **注意事项**
>
>  ​      在自连接查询中，必须要为表起别名，要不然我们不清楚所指定的条件、返回的字段，到底是哪一张表的字段。



**案例**

*  查询员工 及其 所属领导的名字

```sql
select a.name , b.name

from emp a , emp b 

where a.managerid = b.id;
```



*   查询所有员工 emp 及其领导的名字 emp , 如果员工没有领导, 也需要查询出来

```sql
select a.name '员工', b.name '领导' 
from emp a 
left join emp b on a.managerid =b.id;
```





## 1.7 联合查询 union

​    对于union查询，就是**把多次查询的结果合并起来**，形成一个新的查询结果集。

*  对于联合查询的**多张表的列数必须保持一致**，**字段类型也需要保持一致。**
*  **union all 会将全部的数据直接合并在一起，union 会对合并之后的数据去重**。



```sql
SELECT 字段列表 FROM 表A ...
UNION [ ALL ]
SELECT 字段列表 FROM 表B ....;
```






**案例**

*  **将薪资低于 5000 的员工 , 和 年龄大于 50 岁的员工全部查询出来**

​        使用union，因为union不仅合并，还会对数据进行去重，可能会存在一些人薪资低于5000并且年龄大于50岁

```sql
select * from emp where salary < 5000
union
select * from emp where age > 50;
```







## 1.8 子查询



​    SQL语句中嵌套SELECT语句，称为嵌套查询，又称子查询。



**语法**

```sql
SELECT * 
FROM t1 
WHERE column1 = ( SELECT column1 FROM t2 ); 
```



**子查询外部的语句可以是INSERT / UPDATE / DELETE / SELECT 的任何一个。**



**分类**

*  标量子查询（子查询结果为单个值）
*   列子查询 (子查询结果为一列)
*   行子查询 (子查询结果为一行)
*   表子查询 (子查询结果为多行多列)





### 1.8.1 标量子查询

​       子查询返回的结果是**单个值**（数字、字符串、日期等），最简单的形式，这种子查询称为**标量子查询**。



**常用的操作符**： =  <>  >  >=  <  <= 



*   **查询 "销售部" 的所有员工信息**

```sql
select * 
from emp 
where dept_id = (
                  select id 
				  from dept 
				  where name = '销售部'
				 );
```



*   **查询在 "方东白" 入职之后的员工信息**

```sql
select * 
from emp 
where entrydate > ( 
                     select entrydate 
					 from emp 
					 where name = '方东白'
				  );
```





### 1.8.2 列子查询

​    子查询返回的结果是**一列（可以是多行）**，这种子查询称为**列子查询**。



   **常见操作符**

| 操作符 |                  描述                  |
| :----: | :------------------------------------: |
|   IN   |      在指定的集合范围之内，多选一      |
| NOT IN |         不在指定的集合范围之内         |
|  ANY   |  子查询返回列表中，有任意一个满足即可  |
|  SOME  | 与ANY等同，使用SOME的地方都可以使用ANY |
|  ALL   |    子查询返回列表的所有值都必须满足    |



**案例**

*  **查询 "销售部" 和 "市场部" 的所有员工信息**

```sql
select * 

from emp

where dept_id in (
                   SELECT id

                   from dept

                   where name = '销售部' or name = '市场部'
                )

```



*  **查询比 财务部 所有人工资都高的员工信息**

```sql
select * 
from emp
where salary > (
                select MAX(salary)
                from emp
                where dept_id = (
                       select id
                       from dept
                       where name ='财务部'
								     	)
               )
```

**或者**

```sql
select * 
from emp 
where salary > all (
                    select salary 
					from emp 
					where dept_id =(
								 select id 
								 from dept 
								 where name = '财务部'
                                    )
					);
```







*   **查询比研发部其中任意一人工资高的员工信息**

```sql
select * 
from  emp
where salary > any (
                   select salary
                   from emp
                   where dept_id = '1'
                 );
```





### 1.8.3 行子查询

子查询返回的结果是**一行**（可以是多列），这种子查询称为行子查询



**常见操作符**：= 、<> 、IN 、NOT IN



*   查询与 "张无忌" 的薪资及直属领导相同的员工信息 ;

```sql
SELECT *
from emp
where (salary , managerid) = (
                    select salary , managerid
                    from emp
                    where name = '张无忌'
                              )
```





### 1.8.4 表子查询

子查询返回的结果是多行多列，这种子查询称为表子查询。

**常用操作符**：IN



*  查询与 "鹿杖客" , "宋远桥" 的职位和薪资相同的员工信息

```sql
select *
from emp
where (salary ,managerid) in (
                     select salary ,managerid
                     from emp
                     where name ='鹿杖客' or name = '宋远桥'
										 )
```





# 二、事务



## 2.1 简介



​       **事务** 是一组操作的集合，它是一个不可分割的工作单位，事务会把所有的操作作为一个整体一起向系统提交或撤销操作请求，**即这些操作要么同时成功，要么同时失败**





​    在业务逻辑执行之前开启事务，执行完毕后提交事务。如果执行过程中报错，则回滚事务，把数据恢复到事务开始之前的状态。

![image-20230522181739056](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230522181739056.png)



>  **注意**： 
>
>  ​      默认MySQL的事务是自动提交的，也就是说，当执行完一条DML语句时，MySQL会立即隐式的提交事务。



##  2.2  操作演示

**数据准备**

```sql
drop table if exists account;

create table account(
  id int primary key AUTO_INCREMENT comment 'ID',
	
  name varchar(10) comment '姓名',
	
  money double(10,2) comment '余额'
	
) comment '账户表';

insert into account(name, money) VALUES ('张三',2000), ('李四',2000);
```



**正常情况**



```sql
-- 1. 查询张三余额
select * from account where name = '张三';

-- 2. 张三的余额减少1000
update account set money = money - 1000 where name = '张三';

-- 3. 李四的余额增加1000
update account set money = money + 1000 where name = '李四';
```

![image-20230522182600626](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230522182600626.png)

​       但是假设我们在第二步之后出错，张三余额减少了1000，但是由于抛异常了，没有执行完第三步或者没有执行第三步，所以凭空的少了1000



  就目前来说，三条sql语句就是三条事务，他们会自动提交

**为了避免这种情况，我们需要把这些操作控制在一个事务范围内**



## 2.3 控制事务



### 2.3.1 控制事务一

*   **查看/设置事务提交方式**

如果为‘1’就是自动提交，如果为‘0’就是手动提交

```sql
SELECT @@autocommit ;
SET @@autocommit = 0 ;
```



*  **提交事务**

```sql
COMMIT;
```



*  **回滚事务**

```sql
ROLLBACK;
```



>  **注意**：
>
>  ​    上述的这种方式，我们是修改了事务的自动提交行为, 把默认的自动提交修改为了手动提交, 此时我们执行的DML语句都不会提交, **需要手动的执行commit进行提交**。





### 2.3.2 控制事务二

   不会修改事务的提交方式，直接开启事务

*   **开启事务**

```sql
START TRANSACTION 或 BEGIN ;
```



*  **提交事务**

```sql
COMMIT;
```



*  **回滚事务**

```sql
ROLLBACK;
```



```sql
-- 开启事务
start transaction;

-- 1. 查询张三余额
SELECT * from account where name ='张三';

-- 2. 张三的余额减少1000
update account set money = (money - 1000) where name = '张三';

-- 3. 李四的余额增加1000
update account set money = money + 1000 where name = '李四';

-- 如果正常执行完毕, 则提交事务
commit;
-- 如果执行过程中报错, 则回滚事务
-- rollback;

```





## 2.4 事务四大特性 ACID

*  **原子性**（Atomicity）：事务是不可分割的最小操作单元，要么全部成功，要么全部失败。



*  **一致性**（Consistency）：事务完成时，必须使所有的数据都保持一致状态

​             比如张三和李四转账，无论是转账成功还是转账失败，两个人金额加起来是恒定不变的

*  **隔离性**（Isolation）：数据库系统提供的隔离机制，保证事务在不受外部并发操作影响的独立环境下运行。

​            比如A事务和B事务同时操作数据库，A事务在操作的时候不会影响并发的B事务的执行，同理B事务的执行不会影响A事务的执行

*  **持久性**（Durability）：事务一旦提交或回滚，它对数据库中的数据的改变就是永久的





## 2.5 并发事务问题



*  **脏读**

​       **一个事务读到另一个事务还没有提交的数据**

​       事务A对ID为1的数据进行更改，但是并没有提交，此时恰好事务B也读取ID为1的数据，好巧不巧的读取到了事务A修改之后的数据

​      很显然这是不正确的，因为事务A并没有提交数据，事务B读取到的内容应该还是修改之前的内容

![image-20230523131620624](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230523131620624.png)



*  **不可重复度**

​       **一个事务先后读取同一条记录，但两次读取的数据不同，称之为不可重复读**

​      如下所示，事务A读取ID为1的数据，读取完成后事务B对ID为1的数据进行更改，更改完成之后事务A又对ID为1的数据进行读取，发现两次读取的数据内容不一样

![image-20230523131932843](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230523131932843.png)



*  **幻读**

​       **一个事务按照条件查询数据时，没有对应的数据行，但是在插入数据时，又发现这行数据已经存在，好像出现了“幻读”**

​        事务A首先向数据库中查询ID为1的数据，发现没有

​        此时事务B向数据库插入了一条ID为1的数据

​        事务A向数据库插入ID为1的数据报错，因为ID为1，不能再次插入

​        事务A向数据库读取ID为1的数据，发现找不到（读不到事务B提交的数据，因为我们解决了“不可重复读”的问题）数据。

​        执行了插入明明有了，但是找不到，这就叫出现了幻觉，所以我们叫幻读



![image-20230523132438470](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230523132438470.png)







## 2.6 事务隔离级别

为了解决并发事务所引发的问题

**事务隔离级别越高，数据越安全，但是性能越低**。从上到下，事务隔离级别递增

| **隔离级别**                       | **脏读** | **不可重复读** | **幻读** |
| ---------------------------------- | -------- | -------------- | -------- |
| **Read uncommitted** 读未提交      | √        | √              | √        |
| **Read committed ** 读已提交       | ×        | √              | √        |
| **Repeatable Read(默认)** 可重复读 | ×        | ×              | √        |
| **Serializable** 串行化            | ×        | ×              | ×        |



>  Oracle默认隔离级别是  Read committed  读已提交





*  **查看事务隔离级别**

```sql
SELECT @@TRANSACTION_ISOLATION;
```



*  **设置事务隔离级别**
*  *  SESSION 设置会话级别，仅代表针对当前客户端窗口有效
   *  GLOBAL 针对所有客户端会话窗口有效

```sql
SET [ SESSION | GLOBAL ] 
TRANSACTION ISOLATION LEVEL { READ UNCOMMITTED | READ COMMITTED | REPEATABLE READ | SERIALIZABLE }
```

>  ISOLATION 为隔离的意思





*  设置隔离级别为“可重复读”

```sql
SET SESSION TRANSACTION ISOLATION LEVEL  REPEATABLE READ 

```

