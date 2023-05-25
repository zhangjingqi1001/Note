# SQL 优化



# 一、插入数据

```sql
insert into tb_test values(1,'tom');
insert into tb_test values(2,'cat');
insert into tb_test values(3,'jerry');
......
```



如果我们一次性往数据库中插入多条记录，可以从下面几个方面进行优化



*  **批量插入数据**

 不建议超过1000条

```sql
Insert into tb_test values(1,'Tom'),(2,'Cat'),(3,'Jerry');
```



*  **手动控制事务**

```sql
start transaction;

insert into tb_test values(1,'Tom'),(2,'Cat'),(3,'Jerry');

insert into tb_test values(4,'Tom'),(5,'Cat'),(6,'Jerry');

insert into tb_test values(7,'Tom'),(8,'Cat'),(9,'Jerry');

commit;
```





*  **逐渐顺序插入**

​       性能高于乱序插入

```sql
主键乱序插入 : 8 1 9 21 88 2 4 15 89 5 7 3
主键顺序插入 : 1 2 3 4 5 7 8 9 15 21 88 89
```





*  **大批量插入数据 - Load指令**

​      如果一次性需要插入大批量数据，使用insert语句插入性能较低，此时可以使用MySQL数据库提供的load指令进行插入



```sql
-- 客户端连接服务端时，加上参数 -–local-infile
mysql –-local-infile -u root -p

-- 设置全局参数local_infile为1，开启从本地加载文件导入数据的开关
set global local_infile = 1;

-- 执行load指令将准备好的数据，加载到表结构中
load data local infile '/root/sql1.log' into table tb_user fields terminated by ',' lines terminated by '\n' ;
```



**案例**

​    我们使用load插入数据的时候尽量也按照主键顺序插入，因为性能比较高

**查看参数“local_infile”值**

```sql
select @@local_infile;
```



**创建表结构**

```sql
CREATE TABLE `tb_user2` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`username` VARCHAR(50) NOT NULL,
`password` VARCHAR(50) NOT NULL,
`name` VARCHAR(20) NOT NULL,
`birthday` DATE DEFAULT NULL,
`sex` CHAR(1) DEFAULT NULL,
PRIMARY KEY (`id`),
UNIQUE KEY `unique_user_username` (`username`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 ;
```



**加载数据**

fields terminated by ','   每个字段以逗号分隔

lines terminated by '\n' ;   每一行以\n分隔

```sql
load data local infile 'D:\\tb_sku1.sql' into table tb_user2 fields terminated by ',' lines terminated by '\n' ;
```

   插入两百万条数据大约五十秒。

![image-20230525141055559](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525141055559.png)



# 二、主键优化

**主键顺序插入的性能是要高于乱序插入的**，这块来讲解为什么



## 2.1 数据组织方式

​     在InnoDB存储引擎中，表数据都是根据主键顺序组织存放的，这种存储方式的表称为**索引组织表**

​      如下所示，聚集索引下叶子结点存放整条数据，“6”对应的是id为6的整条数据

![image-20230525142342087](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525142342087.png)



**逻辑存储空间**

![image-20230525142813948](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525142813948.png)





## 2.2 页分裂

   页可以为空，也可以填充一般，也可以填充100%。

​    每个页包含了2-N行数据（如果一行数据多大，会行溢出），根据主键排列



   **主键顺序插入页的情况**

​    如下图所示，从左到右依次添加

![image-20230525143405288](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525143405288.png)







**逐渐乱序插入页的情况**

   如下所示，有两个页，但是都已经写满了

![image-20230525144039581](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525144039581.png)

   如果我们再想插入一个id为50的数据，应该添加在47后面，但是页1和页2已经满了，此时就会开启一个新的数据页，id为50的数据不会直接写到页3上 。

   我们在页1上找到百分之五十的位置，那23,47是在百分之五十位置之外的，将其移动到第三张页，然后将id=50的数据页插入到这张页

![image-20230525144613220](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525144613220.png)



​       之后页的指针也应该发生变化，页1不指向页2而是指向页3，而这种现象称为**页分裂**

![image-20230525144723483](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525144723483.png)





## 2.3 页合并

​     **当删除一行记录时，实际上记录并没有被物理删除，只是记录被标记（flaged）为删除，并且他的空间允许被其他记录声明使用**

​     **当业中删除的记录达到了MERGE_THRESHOLD(默认为页的50%)，InnoDB会开始寻找最靠近页（前或后）看看是否可以将两个页合并并以优化空间使用**

>  ​    MERGE_THRESHOLD 合并页的阀值，可以自己设置，在创建表或者创建索引时指定

​      如下所示，页2中删除的数据达到了百分之五十

![image-20230525151252321](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525151252321.png)

​         那就会看看靠近的页，能否合二为一，页1显然不能，页2的数据可以，合并完成后，便是下面的样子

![image-20230525151426035](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525151426035.png)



​     如果我们插入一个id=20的数据，会直接插入到页3

![image-20230525151514205](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525151514205.png)





## 2.4 主键设计原则

*  **满足业务需求的情况下，尽量降低主键的长度**

​      主键索引只有一个，但是二级索引可能有很多个，在二级索引的叶子结点中挂的就是主键，如果主键长度比较长，二级索引比较多，那占用的磁盘的位置也比较多



*  **插入数据时，尽量选择顺序插入，选择使用AUTO_INCREMENT自增主键**

​        如果不是顺序插入数据，很有可能出现页分裂现象



*  **尽量不要使用UUID做主键或者是其他自然主键，如身份证号**

​       因为每次生成的UUID是无序的，如果插入数据库的时候就会乱序插入



*  **业务操作时，避免对主键的修改**

​       因为修改主键会影响索引结构





# 三、 Order by 优化

>   将我们之前phone、name所设置的索引删除
>
>  ```sql
>  drop index idx_user_phone on tb_user;
>  drop index idx_user_phone_name on tb_user;
>  drop index idx_user_name on tb_user;
>  ```
>
>  



**MySQL两种排序方式**

*  **Using filesort **

​      通过表的索引或全表扫描，读取满足条件的数据行，然后在排序缓冲区sort buffer中完成排序操作，所有不是通过索引直接返回排序结果的排序都叫 FileSort 排序

​     下面的排序并没有通过索引

![image-20230525154459705](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525154459705.png)

​    下面的排序也没有通过索引

![image-20230525154551185](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525154551185.png)



*  **Using index**

​      **通过有序索引顺序扫描直接返回有序数据，这种情况即为 using index，不需要额外排序，操作效率高。**

​        创建联合索引

```sql
create index idx_user_age_phone on tb_user(age,phone);
```

​        下面再来测试两个sql语句

```sql
explain select id,age,phone from tb_user order by age ;

explain select id,age,phone from tb_user order by age, phone ;
```

​        发现是Using Index

![image-20230525162009925](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525162009925.png)



​       **在看一下倒序排序**

```sql
explain select id,age,phone from tb_user order by age desc , phone desc ;
```

​    成功的使用了索引

​    Backward index scan; 表示反向使用索引

![image-20230525162900858](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525162900858.png)



​    **刚刚我们创建索引是先age，再phone,那我们**

```sql
explain select id,age,phone from tb_user order by  phone ,age ;
```









# 四、Group by优化





# 五、limit优化





# 六、 count优化





# 七、update优化

