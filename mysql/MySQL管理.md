# MySQL管理

# 一、系统数据库



| 数据库             | 含义                                                         |
| ------------------ | ------------------------------------------------------------ |
| mysql              | 存储MySQL服务器正常运行所需要的各种信息 （时区、主从、用户、权限等） |
| information_schema | 提供了访问数据库元数据的各种表和视图，包含数据库、表、字段类型及访问权限等 |
| performance_schema | 为MySQL服务器运行时状态提供了一个底层监控功能，主要用于收集数据库服务器性能参数 |
| sys                | 包含了一系列方便 DBA 和开发人员利用 performance_schema包含了一系列方便 DBA 和开发人员利用 performance_schema |



 **MySQL数据库**

 比如我们下面的User表，用户以及权限

![image-20230530150659127](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230530150659127.png)



**information_schema**

元数据：数据库本身的一些数据

在InnoDB_tables表中可以查看哪些表是InnoDB引擎

![image-20230530151026544](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230530151026544.png)







# 二、常用工具

## 2.1 MySQL工具

​    该MySQL不是指MySQL服务，而是指MySQL客户端工具。



**语法**

```sql
mysql [options] [database]
```

**选项**

```sql
-u, --user=name #指定用户名

-p, --password[=name] #指定密码

-h, --host=name #指定服务器IP或域名

-P, --port=port #指定连接端口

-e, --execute=name #执行SQL语句并退出
```

​       **-e选项可以在Mysql客户端执行SQL语句，而不用连接到MySQL数据库再执行**，对于一些批处理脚本，这种方式尤其方便。

```sql
mysql -h远程数据库地址（可以没有） -P3306 -uroot –p123456 db01（数据库的名称，也可以是itcast） -e "select * from stu";
```







##  2.2 mysqladmin

​      mysqladmin 是一个执行管理操作的客户端程序。

​      可以用它来**检查服务器的配置和当前状态、创建并删除数据库等**。

**查看帮助文档**

![image-20230530165916191](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230530165916191.png)

