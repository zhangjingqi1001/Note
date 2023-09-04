# 20230904 “There is no getter for property named 'null' in 'class xxx'”

运行之后报错

![image-20230904100548429](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230904100548429.png)

定位到具体的代码后，我检查了一下，感觉没什么问题

![image-20230904100617162](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230904100617162.png)

之后检查一下DailyResult实体类，发现字段都是对应的

最后检查了一下主键，发现我数据库中的主键是“accDate”，实体类中也是“accDate”，此时我们需要在“accDate”字段中添加一个@TableId注解

如下所示：

![image-20230904101900174](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230904101900174.png)



但是如果主键是id的话，不加@TableId注解也是可以的

```java
private Integer id;
```

