[TOC]



# InnoDB引擎



我们之前在这个文章中粗略的介绍了一下InnoDB存储引擎

[ MySQL——存储引擎与索引应用](https://blog.csdn.net/weixin_51351637/article/details/130863622)

下面内容以理解为主



# 一、逻辑存储架构

>  在下面这篇文章 1.3.1.2 InnoDB 逻辑存储结构 中也有介绍
>
>    [ MySQL——存储引擎与索引应用](https://blog.csdn.net/weixin_51351637/article/details/130863622)



**InnnoDB存储结构图**

![image-20230529105408275](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529105408275.png)

-  **TableSpace**： 表空间

    **索引、数据，都是在表空间中存储的，是最外层的逻辑结构**。

    **表空间**（ibd文件），一个MySQL实例可以对应多个表空间，**用于存储记录、索引等数据**

>  **xxx.ibd**：xxx代表的是表名，innoDB引擎的每张表都会对应这样一个表空间文件，**存储该表的表结构（frm-早期的 、sdi-新版的）、数据和索引。**
>
>     比如account表，存储引擎使用的是InnoDB，那account就会对应一个磁盘文件account.ibd
>
>  ![image-20230523144946160](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/606ed47ee6718b7e204a001132fa76fe.png)

 

​         在表空间中又包含很多的段（Segment）



*  **Segment**： 段

 表空间是由各个段组成的，**段分为数据段、索引段、回滚段**等

 **InnoDB是索引组织表，数据段就是B+树的叶子结点，索引段即为B+树的非叶子结点**

 InnoDB中对于段的管理，都是引擎自身完成，不需要人为对其控制。





 在段中又包含许多的区（Extent）



*  **Extent**： 区

**区是表空间的单元结构，每个区的大小为1M。**



默认情况下， InnoDB存储引擎**页大小为16K， 即一个区中一共有64个连续的页**



*  **Page**： 页

默认情况下， InnoDB存储引擎页大小为16K， 即一个区中一共有64个连续的页

**我们存储的一行一行的内容**

页是组成区的最小单元，**页也是InnoDB存储引擎磁盘管理的最小单元**，每个页的大小默认为 16KB。为了保证页的连续性，**InnoDB 存储引擎每次从磁盘申请 4-5 个区。**



*  **Row**：行

   InnoDB存储引擎的数据时按行进行存放的。

​    **行当中存储的具体的字段值、事物的id、回滚指针等**

InnoDB 存储引擎是面向行的，也就是说数据是按行进行存放的，**在每一行中除了定义表时所指定的字段以外**，**还包含两个隐藏字段**

*  **Trx_id**：每次对某条记录进行改动时，都会把对应的事务id赋值给trx_id隐藏列。
*  **Roll_pointer**：每次对某条引记录进行改动时，都会把旧的版本写入到undo日志中，然后**这个隐藏列就相当于一个指针，可以通过它来找到该记录修改前的信息**。





# 二、架构

   MySQL5.5 版本开始，默认使用InnoDB存储引擎，它**擅长事务处理，具有崩溃恢复特性**。

（左侧内存结构，右侧磁盘结构）

左侧内存结构很大一部分是buffer缓冲区

右侧磁盘结构有TableSpace表空间，Doublewrite Buffer 双写缓冲区

![image-20230529131453716](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529131453716.png)



## 2.1 内存结构

在内存结构中标注的**四块区域**：

Buffer Pool ： 缓冲池

Change Buffer： 更改缓冲区

Log Buffer：日志缓冲区域

Adaptive Hash Index：自适应哈希索引

![image-20230529131738386](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529131738386.png)



### 2.1.1 Buffer Pool 缓冲池

​     **缓冲池**是主内存中的一个区域，里面可以**缓存磁盘上经常操作的真实数据**，在执行增删改查操作时，**先操作缓冲池中的数据（若缓冲池中没有数据，则从磁盘加载并缓存），然后再以一定频率刷新到磁盘，从而减少磁盘IO，加快处理速度**。

>  假设没有缓冲池，每一次操作都需要从磁盘读取，就会存在大量的磁盘IO



​      缓冲池以Page页为单位，底层采用链表数据结构管理Page，根据状态，将Page分为三种类型：

*  free page： 空闲Page，未被使用
*  clean page： 被使用page，数据没有被修改过
*  dirty page：脏页，被使用page，数据被修改过，页中数据与磁盘的数据产生了不一致。

如下图所示，一个一个的方块实际就是一个一个的页Page。



![image-20230529132643370](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529132643370.png)



>   **读取数据**
>
>  ​     InnoDB引擎首先会查看Buffer Pool中是否已经加载了这个数据页，如果已经加载则直接返回，否则从磁盘中读取该数据页，并将其存储到Buffer Pool中。
>
>  ​      而对于缓存了更新操作的数据页，InnoDB引擎会从Change Buffer中读取更新操作，并在将缓存的更新操作应用到对应的数据页之前，先加载该数据页到Buffer Pool中。



### 2.1.2  Change Buffer 更改缓冲区

   **Change Buffer**： 更改缓冲区（针对于非唯一二级索引页），在执行DML语句时，如果这些数据Page没有在Buffer Pool中，不会直接操作磁盘，而会将数据变更存在更改缓冲区 Change Buffer中，在未来数据被读取时，再将数据合并恢复到Buffer Pool中，再将合并后的数据刷新到磁盘中。

>  ​      执行增删改中，如果这些数据Page没有在Buffer Pool中，此时不会操作磁盘，而是将这部分的操作缓冲在Change Buffer之中，将来读取这部分数据的时候，再将这一部分的数据合并到Buffer Pool中，之后再将Buffer Pool合并之后的数据刷新到磁盘





**Change Buffer的意义是什么呢**

​     与聚集索引不同，二级索引通常是非唯一的，并且以相对随机的顺序插入二级索引。同样，删除和更新可能会影响索引树中不相邻的二级索引页，如果每一次都操作磁盘，会造成大量的磁盘IO。有了ChangeBuffer之后，我们可以在缓冲池中进行合并处理，减少磁盘IO。

>  增删改的时候可以先操作change Buffer，然后再以一定的频率把change Buffer当中的数据同步到buffer Pool，然后再刷新到磁盘当中。

![image-20230529134657562](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529134657562.png)











### 2.1.3 Log Buffer 日志缓冲区域

​    日志缓冲区，用来保存要写入到磁盘中的log日志数据（redo log 、undo log），默认大小为 16MB，日志缓冲区的日志会定期刷新到磁盘中。

​    如果需要更新、插入或删除许多行的事务，增加日志缓冲区的大小可以节省磁盘 I/O。



>  **参数**
>
>  **innodb_log_buffer_size**：缓冲区大小
>
>  ​     下面这两种方式都可以
>
>  ```sql
>  show variables  like 'innodb_log_buffer_size';
>  
>  show variables  like '%log_buffer_size%';
>  ```
>
>  ![image-20230529143512083](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529143512083.png)
>
>  **innodb_flush_log_at_trx_commit**：日志刷新到磁盘时机，取值主要包含以下三个
>
>  *  1: 日志在每次事务提交时写入并刷新到磁盘，默认值。
>  *  0: 每秒将日志写入并刷新到磁盘一次。
>  *  2: 日志在每次事务提交后写入，并每秒刷新到磁盘一次。
>
>  ```sql
>  show variables  like 'innodb_flush_log_at_trx_commit';
>  ```
>
>  ![image-20230529143652767](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529143652767.png)





### 2.1.4  Adaptive Hash Index 自适应hash索引

>  **只有Memory存储引擎支持Hash结构，但是InnoDB引擎具有自适应Hash功能，Hash索引是存储引擎根据B+Tree在指定条件下自动构建的**
>
>   hash索引在进行等值匹配时，一般性能是要高于B+树的，因为hash索引一般只需要一次IO即可，而B+树，可能需要几次匹配，所以hash索引的效率要高，但是hash索引又不适合做范围查询、模糊匹配等。



 **自适应hash索引，用于优化对Buffer Pool数据的查询**。

 InnoDB存储引擎会监控表上个索引页的查询，如果观察到hash索引可以提升速度，，则建立hash索引，此称之为**自适应hash索引**。



>  **自适应哈希索引，无需人工干预，是系统根据情况自动完成。**
>
>  参数： adaptive_hash_index ， 自适应哈希索引的开关
>
>  使用模糊匹配观察一下是否开启
>
>  ```sql
>  show variables  like '%hash_index%';
>  ```
>
>  ![image-20230529142147111](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529142147111.png)





## 2.2 磁盘结构

![image-20230529143857569](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529143857569.png)



### 2.2.1 System Tablespace 系统表空间

**系统表空间**是Change Buffer更改缓冲区的存储区域，如果表是在系统表空间而不是每个文件或通用表空间中创建的，它也可能包含表和索引数据。

>  (在MySQL5.x版本中还包含InnoDB数据字典、undolog等)

![image-20230529144743336](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529144743336.png)

**参数**

innodb_data_file_path

```sql
show variables  like 'innodb_data_file_path';
```

系统表空间，默认的文件名叫 ibdata1。

![image-20230529144653523](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529144653523.png)

### 2.2.2  File-Per-Table Tablespaces

​     **每张表独立的表空间**,并不会在system tablespace系统表空间中存放。

​     如果开启了innodb_file_per_table开关，每个表的文件表空间包含单个InnoDB表的数据和索引 ，并存储在文件系统上的单个数据文件中

![image-20230529144835794](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529144835794.png)



**开关参数，默认开启**

innodb_file_per_table

```sql
show variables  like 'innodb_file_per_table';
```

![image-20230529145303043](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529145303043.png)



我们之前也看过表空间文件（.ibd结尾的），下面的每一个文件都是一个表空间文件，在里面存放的表的结构以及表中的数据、索引

![image-20230523144946160](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/606ed47ee6718b7e204a001132fa76fe.png)









### 2.2.3   General Tablespaces 通用表空间

通用表空间，需要通过 CREATE TABLESPACE 语法创建通用表空间，在创建表时，可以指定该表空间。

![image-20230529145711778](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529145711778.png)

*  **创建表空间**

ADD DATAFILE 指定我们表空间关联的表空间文件

ENGINE 指定存储引擎

```sql
CREATE TABLESPACE 表空间的名字 ADD DATAFILE 'file_name' ENGINE = engine_name;
```

如下所示，创建表空间

```sql
create tablespace ts_itcast add datafile 'myitcast.ibd' engine = innodb;
```





*  **创建表时指定表空间**

```sql
CREATE TABLE xxx ... TABLESPACE ts_name;
```



创建表并指定表空间

```sql
create table a(
     id int primary key auto_increment,
	 name varchar(10) 
)engine INNODB tablespace ts_itcast;
```



可以找到对应的文件，在下面的通用表空间中有我们的表a

![image-20230529155605307](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529155605307.png)





### 2.2.4 Undo Tablespaces 撤销表空间

**撤销表空间**，MySQL实例在初始化时会自动创建两个默认的undo表空间（初始大小16M），用于存储undo log日志。

​    两个文件分别别undo001，undo002（默认是这两个名，在data目录下）

![image-20230529160618237](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529160618237.png)





### 2.2.5 Temporary Tablespaces

InnoDB 使用会话临时表空间和全局临时表空间。存储用户创建的临时表等数据



### 2.2.6  Doublewrite Buffer Files 双写缓冲区

​     双写缓冲区，innoDB引擎将**数据页从Buffer Pool刷新到磁盘前，先将数据页写入双写缓冲区文件中**，便于系统异常时恢复数据。

​     下面是双写缓冲区文件

![image-20230529160909017](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529160909017.png)



### 2.2.7 Redo Log 重做日志

**重做日志，是用来实现事务的持久性**



>  ​      当事务提交之后会把所有修改信息都会存到该日志中, 用于在刷新脏页到磁盘时,发生错误时, 进行数据恢复使用。



**该日志文件由两部分组成**

*  重做日志缓冲（redo logbuffer）

   在内存中

*  重做日志文件（redo log）

   在磁盘中



该日志不会永久保存，会每隔一段时间去清理之前没有用的redo log.

事务提交之后，redo log日志存在的必要也不大了，因为它就是为了保证异常时进行数据恢复

![image-20230529161608668](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529161608668.png)





## 2.3 后台线程

>   内存中的数据是怎么刷新到磁盘空间里的呢？ 
>
>  ​    涉及了一组后台线程



**作用**：将InnoDB存储引擎的缓冲池当中的数据在合适的时机刷新到磁盘文件当中

![image-20230529162757437](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529162757437.png)

*  **Master Thread**

​        核心后台线程，负责调度其他线程，还负责将缓冲池中的数据异步刷新到磁盘中, 保持数据的一致性，还包括脏页的刷新、合并插入缓存、undo页的回收 。

​      在InnoDB存储引擎中大量使用了AIO来处理IO请求, 这样可以极大地提高数据库的性能，而IO Thread主要负责这些IO请求的回调。





*  **IO Thread**

​     在InnoDB存储引擎中大量使用了AIO（异步非阻塞IO）来处理IO请求, 这样可以极大地提高数据库的性能

​    而**IOThread主要负责这些IO请求的回调**。

| 线程类型             | 默认个数 | 职责                         |
| -------------------- | -------- | ---------------------------- |
| Read thread          | 4        | 负责读操作                   |
| Write thread         | 4        | 负责写操作                   |
| Log thread           | 1        | 负责将日志缓冲区刷新到磁盘   |
| Insert buffer thread | 1        | 负责将写缓冲区内容刷新到磁盘 |

查看到InnoDB的状态信息,里面有IO信息

```sql
show engine innodb status;
```

这些线程全部采用的是AIO，异步线程

![image-20230529171550004](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529171550004.png)

  目前read线程、write线程都是在等待接收请求

![image-20230529171640843](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529171640843.png)



*  **Purge Thread**

主要用于回收事务已经提交了的undo log，在事务提交之后，undo log可能不用了，就用它来回收。



*  **Page Cleaner Thread**

协助 Master Thread 刷新脏页到磁盘的线程，它可以减轻 Master Thread 的工作压力，减少阻塞



# 三、事物原理 

事务的基础知识：[MySQL基础 — 多表查询以及事务管理](https://blog.csdn.net/weixin_51351637/article/details/130863570?spm=1001.2014.3001.5502)



​     原子性、一致性、持久性是InnoDB存储引擎底层的两份日志来保障的

​     隔离性是由InnoDB存储引擎底层的锁机制、MVCC多版本并发控制来实现的

![image-20230529185536522](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529185536522.png)



   refo log、undo log在前面2.1.3（日志缓冲区域）中提到过



## 3.1 redo log 重做日志

​    **持久性就是由redo log来保障的**

​     **redo log 重做日志**，记录的是事务提交时数据页的物理修改，用来实现事务的持久性。

​     该日志文件由两部分组成：重做日志缓冲（redo log buffer）以及重做日志文件（redo log file），前者是在内存中，后者在磁盘中。

​     **当事务提交之后会把所有修改信息都存放到该日志文件中，用于在刷新脏页到磁盘，发生错误时，进行数据恢复使用**。

****

​       在InnoDB引擎内存结构中，主要的存储区域就是缓冲池，在缓冲池中缓存了很多的数据页。

​       当我们在一个事务中，执行多个增删改的操作时，InnoDB引擎会先操作缓冲池中的数据，如果缓冲区没有对应的数据，会通过后台线程将磁盘中的数据加载出来，存放在缓冲区中，然后将缓冲池中的数据修改，修改后的数据页我们称为**脏页**。



>  ​    脏页：内存中缓存的数据与磁盘上的数据不一致的状态。



​         而**脏页则会在一定的时机，通过后台线程刷新到磁盘中，从而保证缓冲区与磁盘的数据一致**。 



>  ​    而缓冲区的脏页数据并不是实时刷新的，而是一段时间之后将缓冲区的数据刷新到磁盘中，**假如**刷新到磁盘的过程**出错**了，而**提示给用户事务提交成功，而数据却没有持久化下来，这就出现问题了，没有保证事务的持久性**（事务一旦提交或回滚，它对数据库中的数据的改变就是永久的）。



![image-20230529190948018](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529190948018.png)





**如何解决上述问题呢？**

   **InnoDB中提供了一份日志 redo log 重做日志**，当对缓冲区的数据进行增删改之后，会首先将操作的数据页的变化，记录在redolog buffer中。

   在事务提交时，会将redo log buffer中的数据刷新到redo log磁盘文件中。

   过一段时间之后，如果刷新缓冲区的脏页到磁盘时，发生错误，此时就可以借助于redo log进行数据恢复，这样就保证了事务的持久性。

   而如果脏页成功刷新到磁盘 或 或者涉及到的数据已经落盘，此时redolog就没有作用了，就可以删除了，所以存在的两个redolog文件是循环写的。



![image-20230529191607407](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529191607407.png)





​    **为什么每一次提交事务，要刷新redo log 到磁盘中呢，而不是直接将buffer pool中的脏页刷新到磁盘呢 ？**

​          这么做存在严重的性能问题。

​          因为在业务操作中，我们操作数据一般都是随机读写磁盘的，而不是顺序读写磁盘。 而redo log在往磁盘文件中写入数据，由于是日志文件，所以都是顺序写的。顺序写的效率，要远大于随机写。 这种**先写日志的方式，称之为 WAL**（Write-Ahead Logging）。



## 3.2 undo log 回滚日志

​      **解决事务的原子性**（事务是不可分割的最小操作单元，要么全部成功，要么全部失败）

​      **回滚日志**，用于记录数据被修改前的信息 , **作用**包含两个： **提供回滚**(保证事务的原子性) 、**MVCC(**多版本并发控制) 



>  ​      比如我们执行一条update语句的时候，在undolog里面将会记录这条语句在更新之前长什么样
>
>  ​      **undo log和redo log**不同，undo log记录逻辑日志，redo log记录物理日志。
>
>  ​    物理日志： 主要记录数据里面的内容长什么样
>
>  ​    逻辑日志： 每一步执行的是什么样的操作
>
>     **对逻辑日志的理解**：
>
>  ​    可以认为当delete一条记录时，undo log中会记录一条对应的insert记录，反之亦然。
>
>  ​     当update一条记录时，它记录一条对应相反的update记录。
>
>  ​     当执行rollback时，就可以从undo log中的逻辑记录读取到相应的内容并进行回滚。



**Undo log销毁**：undo log在事务执行时产生，事务提交时，并不会立即删除undo log，因为这些日志可能还用于MVCC。



**Undo log存储**：undo log采用段的方式进行管理和记录，存放在前面介绍的 rollback segment回滚段中，内部包含1024个undo log segment。





# 四、MVCC

   MVCC的具体实现，还需要依赖于数据库记录中的三个隐式字段、undo log日志、readView。

## 4.1 基本概念

### 4.1.1 当前读



  **读取的是记录的最新版本**，读取时还要保证其他并发事务不能修改当前记录，会对读取的记录进行加锁。

>  如：select ... lock in share mode(共享锁)，select ...for update、update、insert、delete(排他锁)都是一种当前读。



两个客户端且都开启事务，客户端B对id为1的数据进行修改，正常情况下客户端A是读取不到客户端B修改后的数据，因为事务是相互隔离的。

   如果此时客户端B事务提交了，客户端A还是查不到的，因为当前隔离级别是Repeatable Read(默认) 可重复读

>  事务隔离级别：[MySQL基础 — 多表查询以及事务管理](https://blog.csdn.net/weixin_51351637/article/details/130863570?spm=1001.2014.3001.5502&ydreferer=aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl81MTM1MTYzNz90eXBlPWJsb2c%3D)



![image-20230529201443829](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529201443829.png)



  我们上图中的Select语句并不是当前读，如果我们想让上面的Select语句变成当前读，只需要改为select ... lock in share mode(共享锁)或者select ...for update

   如下所示： 我们的客户端A没有提交事务，客户端B提交了事务，但是此时客户端A可以读取到客户端B提交的事务了。

​    **也就是说当前读读取到的就是最新的数据记录**

![image-20230529201954561](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529201954561.png)







### 4.1.2 快照读

简单的select（不加锁）就是快照读，快照读，读取的是记录数据的可见版本，有可能是历史数据，不加锁，是非阻塞读。

 

比如下图，客户端B提交了事务，但是客户端A还是读取不到，就是因为下图中的Select是快照读，读取的数据也是历史数据

![image-20230529201443829](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529201443829.png)



**Read Committed** 读已提交： 每次Select，都生成一个快照读

**Repeatable Read**(默认) 可重复读：开启事务后第一个Select语句才是快照读的地方

>  ​        比如select * from stu 是第一个执行select语句的地方，是快照读,会产生一个快照，后续我们再使用select * from stu 查询数据时，实际上直接查的就是前面产生的这个快照数据（历史数据），也就保证了可重复读。



**Serializable** 串行化：快照读会退化为当前读，每一次读取数据都会加锁





### 4.1.3 MVCC 多版本并发控制

​     **指维护一个数据的多个版本，使得读写操作没有冲突，快照读为MySQL实现MVCC提供了一个非阻塞读功能**。

   MVCC的具体实现，还需要依赖于数据库记录中的三个隐式字段、undo log日志、readView。





## 4.2 隐藏字段

​        当我们创建了下面这张表后，除了下面三个显示出来的字段，InnoDB引擎还会自动的给我们添加三个隐藏字段。

![image-20230529204139773](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529204139773.png)



| 隐藏字段    | 含义                                                         |
| ----------- | ------------------------------------------------------------ |
| DB_TRX_ID   | 最近修改事务ID，记录插入这条记录或最后一次修改该记录的事务ID。 |
| DB_ROLL_PTR | 回滚指针，指向这条记录的上一个版本，用于配合undo log，指向上一个版本。 |
| DB_ROW_ID   | 隐藏主键，如果表结构没有指定主键，将会生成该隐藏字段。       |





## 4.3 undo log回滚日志

​    **回滚日志，在insert、update、delete的时候产生的便于数据回滚的日志**。

​    当insert的时候，产生的undo log日志只在回滚时需要，在事务提交后，可被立即删除。

​    而update、delete的时候，产生的undo log日志不仅在回滚时需要，在快照读时也需要，不会立即被删除。



### 4.3.1 undo log 版本链

新插入一条数据。   此条记录是新插入的，所以没有回滚指针

![image-20230529210027290](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529210027290.png)



然后，有四个并发事务同时在访问这张表。

![image-20230529210148953](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529210148953.png)

*  **首先事务2执行**

如下图所示，首先事务2将id为30的的记录进行修改，修改为age为3

![image-20230529210148953](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529210148953.png)



​      在修改记录之前，InnoDB记录undo log日志，，记录数据变更之前的样子; 然后再更新记录，并且记录本次操作的事务ID，回滚指针，回滚指针用来指定如果发生回滚，回滚到哪一个版本。如下所示

![image-20230529210655565](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529210655565.png)

   **执行完上面的操作后提交事务**



*  **之后事务3再执行**

**将id为30记录，name改为A3**

同样，在更新之前需要将原来的数据记录到undo日志当中，然后再更新数据

执行完成之后就成下面的样子了

![image-20230529211330069](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529211330069.png)

**我们执行完成之后undo log日志并没有删除，就是因为有其他的事务在使用此条uodo log日志**

提交事务



*  **执行事务四**

   相同的流程

![image-20230529211607109](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529211607109.png)





**最终**我们发现，不同事务或相同事务对同一条记录进行修改，会导致该记录的undolog生成一条记录版本链表，链表的头部是最新的旧记录，链表尾部是最早的旧记录



**那在我们查询的时候，最终会回到哪一个版本呢？**

这不是由版本链控制的，具体要回到哪个版本，涉及到MVCC实现原理当中的第三个组件：readView



## 4.4 readView

​     ReadView（读视图）是 **快照读 SQL执行时MVCC提取数据的依据，记录并维护系统当前活跃的事务（未提交的）id**。

>  ​    快照读读取的不一定是最新的记录，很有可能是历史记录，我们刚刚带undo log日志中产生的数据都是历史记录
>
>  ​      **那我们快照读在读取的时候到底读取哪个历史记录呢？**
>
>  ​      就是由readview来决定的，因为readView记录并维护系统当前活跃的事务（未提交的）id



ReadView中包含了**四个核心字段**：在快照读的时候MVCC提取事务的依据就依赖于下面四个核心字段

| 字段           | 含义                                                 |
| -------------- | ---------------------------------------------------- |
| m_ids          | 当前活跃的事务ID集合                                 |
| min_trx_id     | 最小活跃事务ID                                       |
| max_trx_id     | 预分配事务ID，当前最大事务ID+1（因为事务ID是自增的） |
| creator_trx_id | ReadView创建者的事务ID                               |



**版本链数据的访问规则**

trx_id 代表当前undolog版本链对应事务ID。

![image-20230529214634264](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230529214634264.png)



**不同的隔离级别，生成ReadView的时机不同**：

*  READ COMMITTED： 在事务中每一次执行快照读时生成ReadView
*  REPEATABLE READ：仅在事务中第一次执行快照读时生成ReadView，后续复用该ReadView。



## 4.5 MVCC原理分析

MVCC的**实现原理**就是通过 InnoDB表的**隐藏字段**（只要依靠事务id与回滚指针）、**UndoLog 版本链**、**ReadView**来实现的。

而**MVCC + 锁，则实现了事务的隔离性**。 

而**一致性则是由redolog 与 undolog保证**。

![image-20230530135025147](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230530135025147.png)



>   理解一下4.4 readView版本链数据的访问规则

**RC隔离级别下，在事务中每一次执行快照读时生成ReadView**

**RR隔离级别下，仅在事务中第一次执行快照读时生成ReadView，后续复用该ReadView**





### 4.5.1 RC隔离级别提取原理

**RC隔离级别下，在事务中每一次执行快照读时生成ReadView**

我们可以分析一下刚刚事务5，在RC隔离级别下生成的ReadView

![image-20230530123836380](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230530123836380.png)

**第一次查询**

id为30的记录m_ids:{3,4,5}，因为事务2在此行处已经提交了。

最小活动事务id 即min_trx_id是3

预分配事务id 即max_trx_id是6（，当前最大事务**ID+1**）

创建者事务id 即creator_trx_id是5



**第二次查询**

id为30的记录m_ids:{4,5}，因为事务2、3在此行处已经提交了。

最小活动事务id 即min_trx_id是4

预分配事务id 即max_trx_id是6（，当前最大事务**ID+1**）

创建者事务id 即creator_trx_id是5

![image-20230530123708881](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230530123708881.png)





**事务5第一次select读取的哪个版本？**

拿着db_trx_id到右边的表进行比对，

当trx_id = 4 时，四个不等式都不满足

 当trx_id = 3 时，四个不等式都不满足

 当trx_id = 2 时，满足第二个等式，所以可以访问此条记录

![image-20230530124736059](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230530124736059.png)

最终返回快照读的结果就是下面这条数据，而这条数据正式事务二所提交的

![image-20230530124946132](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230530124946132.png)





**事务5第二次select读取的哪个版本？**

与上面的流程相同

![image-20230530125149680](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230530125149680.png)

最终访问的是，事务3提交的

![image-20230530125223239](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230530125223239.png)





### 4.5.2 RR隔离级别提取原理

**RR隔离级别下，仅在事务中第一次执行快照读时生成ReadView，后续复用该ReadView**

放我们执行**第一个select语句的时候会产生一个快照读ReadView**

记录了

id为30的记录m_ids:{3,4,5}，因为事务2在此行处已经提交了。

最小活动事务id 即min_trx_id是3

预分配事务id 即max_trx_id是6（，当前最大事务**ID+1**）

创建者事务id 即creator_trx_id是5

如果我们**再执行第二个Select语句，不会再创建一个readView，会复用第一个Select语句**了

![image-20230530134456570](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230530134456570.png)