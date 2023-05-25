







[TOC]





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

## 3.0 排序方式讲解



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



**案例**

*  **在看一下倒序排序**

```sql
explain select id,age,phone from tb_user order by age desc , phone desc ;
```

​    成功的使用了索引

​    Backward index scan; 表示反向使用索引

![image-20230525162900858](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525162900858.png)



*  **刚刚我们创建索引是先age，再phone,那我们此时排序先按phone进行排序，再按age进行排序**

 **Using filesort**： 出现这个字符的原因是违背最左前缀法则的，因为我们在创建索引的时候age是第一个字段，phone是第二个字段，但是我们排序的时候，phone是第一个字段，age是第二个字段，所以这个地方phone没有走索引，age走了索引

```sql
explain select id,age,phone from tb_user order by  phone ,age ;
```

![image-20230525170902685](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525170902685.png)





*  **age Asc ,phone Desc**

​     我们创建age，phone联合索引的时候，并没有指定升序排列还是降序排列，那就默认升序

​     但是这个地方我们的phone是倒序排列，所以就需要额外的排序，导致了Using filesort

```sql
explain select id,age,phone from tb_user order by age Asc ,phone Desc ;
```

![image-20230525171831956](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525171831956.png)



   这个地方也是可以优化的，如下所示

```sql
create index idx_user_age_phone_ad on tb_user(age asc ,phone desc);
```





## 3.1 升序/降序联合索引结构图示

**叶子结点**

  如果排序符合下面的情况，直接返回就可以了，不需要再排，所以走索引的排序会比较快，效率比较高

![image-20230525172940016](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525172940016.png)





**所有的排序规则都有一个条件，就是使用了覆盖索引**

[MySQL——存储引擎于索引应用](https://blog.csdn.net/weixin_51351637/article/details/130863622?spm=1001.2014.3001.5502)



  比如下面的数据，id，age，phone在叶子结点中都存在，不需要回表查询

```sql
explain select id,age,phone from tb_user order by age Asc ,phone Desc ;
```







## 3.2 总结

 上面案例能够正常使用索引排序的情况

  创建索引、联合索引的时候不指定升序排列还是降序排列，默认就是升序排列

*  order by age
*  order by age,phone
*  order by age Desc, phone Desc



一个升序，一个降序会出现filesort现象，但是也可以优化，如下所示

```sql
create index idx_user_age_phone_ad on tb_user(age asc ,phone desc);
```





**总结**

*  根据排序字段建立合适的索引，多字段排序时，也遵循最左前缀法则。
*  尽量使用覆盖索引。
*   多字段排序, 一个升序一个降序，此时需要注意联合索引在创建时的规则（ASC/DESC）。
*   如果不可避免的出现filesort，大数据量排序时，可以适当增大排序缓冲区大小sort_buffer_size(默认256k)。

# 四、Group by优化

*  在分组操作时，可以通过索引来提高效率
*  分组操作时，索引的使用也是满足最左前缀法则的







**将之前的索引全部删除**

```sql
drop index idx_user_pro_age_sta on tb_user;
drop index idx_email_5 on tb_user;
drop index idx_user_age_phone_aa on tb_user;
drop index idx_user_age_phone_ad on tb_user;
```





```sql
explain select profession , count(*) 
from tb_user 
group by profession ;
```

Extra:Using temporary 性能是比较低的

![image-20230525204940944](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525204940944.png)





**创建索引进行优化**

​    尽量创建联合索引

```sql
create index idx_pro_age_status on tb_user(profession,age,status);
```

​    此时变成了Using index

![image-20230525205204663](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525205204663.png)





**如果此时根据age进行分组呢？**

```sql
explain select age , count(*) 
from tb_user 
group by age ;
```

   用到了索引但是页使用了临时表，效率比较低.

​    因为根据age分组不满足最左前缀原则，出现了临时表

![image-20230525205456027](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525205456027.png)







**根据profession、age分组呢？**

```sql
explain select profession, age , count(*) 
from tb_user 
group by  profession,age ;
```

​     满足最左前缀原则，很完美

![image-20230525205744944](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525205744944.png)

**profession过滤，age分组呢？**

```sql
explain select  age , count(*) 
from tb_user 
where profession = '软件工程'
group by  age ;
```

![image-20230525210255599](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230525210255599.png)





# 五、limit优化

​     在数据量比较大时，如果进行limit分页查询，在查询时，越往后，分页查询效率越低。



一般分页查询时，通过创建 覆盖索引 能够比较好地提高性能，可以通过覆盖索引加子查询形式进行优化。

>  ​      不清楚覆盖索引的可以查看下面这一篇文章：
>
>  [MySQL——存储引擎于索引应用](https://blog.csdn.net/weixin_51351637/article/details/130863622?spm=1001.2014.3001.5502)



  首先说明 MySQL不支持 where id in ( ... limit ..) 这种语法，但是我们可以使用下面这种形式的。

```sql
explain select * 
from tb_sku t , (select id from tb_sku order by idlimit 2000000,10) a 
where t.id = a.id;
```





# 六、 count优化

如果数据量很大，在执行count操作时，是非常耗时的。



*  MyISAM 引擎把一个表的总行数存在了磁盘上，因此执行 count(*) 的时候会直接返回这个数，效率很高； 但是如果是带条件的count，MyISAM也慢
*  InnoDB 引擎就麻烦了，它执行 count(*) 的时候，需要把数据一行一行地从引擎里面读出来，然后累积计数。



   **优化思路：手动技术**，借用Redis等缓存技术，插入数据时加1，删除数据时减一

​       **count() **是一个聚合函数，对于返回的结果集，一行行地判断，如果 count 函数的参数不是NULL，累计值就加 1，否则不加，最后返回累计值



>  ​     count(主键)   需要取值，     count(字段) 需要进行判断，count(数字)需要累加，而count(*) 效率最高，不取值直接在服务层按行进行累加
>
>  ​    **效率排名** count(字段) < count(主键 id) < count(1) ≈ count(*)



| count用法   | 含义                                                         |
| ----------- | ------------------------------------------------------------ |
| count(主键) | InnoDB 引擎会遍历整张表，把每一行的 主键id 值都取出来，返回给服务层。服务层拿到主键后，直接按行进行累加(主键不可能为null) |
| count(字段) | 没有not null 约束 : InnoDB 引擎会遍历整张表把每一行的字段值都取出来，返回给服务层，服务层判断是否为null，不为null，计数累加。有not null 约束：InnoDB 引擎会遍历整张表把每一行的字段值都取出来，返回给服务层，直接按行进行累加（省略了判断是否为NULL）。 |
| count(数字) | InnoDB 引擎遍历整张表，但不取值。服务层对于返回的每一行，放一个数字“1”进去，直接按行进行累加。 |
| count(*)    | InnoDB引擎并不会把全部字段取出来，而是**专门做了优化，不取值，服务层直接按行进行累加**。 |

**比如**

**count(1)** : InnoDB会遍历整张表，但不取值。服务层对于返回的每一行，放一个数字“1”进去，直接按行进行累加

​    下面四个结果相同

```sql
select count(1) from tb_user ;
select count(0) from tb_user ;
select count(-1) from tb_user ;
select count(2) from tb_user ;
```







# 七、update优化



当我们在执行更新的SQL语句时，会锁定id为1这一行的数据，然后事务提交之后，行锁释放。

```sql
update course set name = 'javaEE' where id = 1 ; 
```



​     但是当我们在执行如下SQL时。当我们开启多个事务，在执行上述的SQL时，我们发现行锁升级为了表锁。 导致该update语句的性能大大降低。

>     因为name字段没有索引，加的不是行锁而是表锁，当表锁住后，其他会话无法执行update等语句



```sql
update course set name = 'SpringBoot' where name = 'PHP' ; 
```



**优化方法**

*  尽量使用主键修改
*   添加索引，尽量使用有索引表示的字段修改



**总的来说就是根据索引字段进行更新，否则就会出现行锁升级为表锁，影响并发执行的效率**

