# Redis的最佳实践

# 一、Redis键值设计

## 1.1 key结构

Redis的Key可以自定义，但最好遵循下面几个最佳实践的约定

* **遵循基本格式**

  \[业务名称\]:\[数据名\]:\[id\]

  

* **长度不超过44字节**

  长度越小，占用的空间就越小

  

* **不包含特殊字符**

  避免出现bug



**例如**

我们的登录业务，保存用户信息，其key是这样的： `login:user:10`



**优点**

* **可读性强**

* **避免key冲突**

* **方便管理**。

  前缀一样的key，都会出现在同一个目录下

* **更节省内存**。

   key是string类型，底层编码包含int、embstr和raw三种。

  > int：在key全是数值的情况下，会采用int的编码，把字符串直接当成数字存储，这样存储就会小很多
  >
  > 如果key不是全数字，那会采用SDS模式，特殊的编码方式。
  >
  > **而embstr、raw在SDS下存储方式不一样**。
  >
  > `embstr`在存储时是一段连续的空间，编码更紧凑，占用空间更小，但是有一个条件：embstr在小于44字节使用
  >
  > 如果超过了44个字节，他就会转为`raw`模式进行存储
  >
  > `raw`模式下的存储空间不是连续的，而是指向另外的一段空间
  >
  > 空间不连续的时候，性能肯定会受到一定的影响，还有可能会产生一些内存的碎片，随意内存占用就会比embstr高一些
  >
  > **为了提高内存的使用率，减少内存碎片，推荐使用embstr编码**
  >
  > 这不是我们能控制的，但是我们能控制把字符串的长度缩小到44个字节以内，自然就会使用embstr编码了



**示例**

创建一个普通的key与value

```sh
set num 123
```

查看key的类型:类型是“string”

```sh
type num
```

查看编码方式:"int"

```sh
object encoding num
```

创建一个普通的key与value

```sh
set name Jack
```

查看编码方式:"embstr"

```sh
object encoding name
```

创建一个普通的key与value

```sh
set name aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
```

查看编码方式:"raw"

```sh
object encoding name
```





## 1.2 拒绝BigKey

### 1.2.1 BigKey介绍

BigKey通常以Key的大小和Key中成员的数量来综合判定

* **Key本身的数据量过大**

  一个String类型的Key，它的值为5 MB。

  > String类型最大允许内存是512M，但是达到5M已经是一个超级大的key了

* **Key中的成员数过多**

  一个ZSET类型的Key，它的成员数量为10,000个

  > 集合类型的key，里面的成员数量过多

* **Key中成员的数据量过大**

  一个Hash类型的Key，它的成员数量虽然只有1,000个但这些成员的Value(值)总大小为100MB。

****

**具体怎么判断元素的大小呢**

命令：

```
MEMORY USAGE key
```

这个地方衡量的是 key、value以及他们两个之间的数据结构的总的一个字节数量

但是这个命令对内存的占用量非常的大，不推荐使用

![image-20230723142314133](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723142314133.png)

我们判断元素的大小不一定非要准确的值，我们只需要衡量值value就行，别的不需要管

那我们就能使用下面的命令：查看字符串的长度。

通过这个查看一下占了多少个字节

```
STRLEN key
```

对于集合类型来说，我们只需要查看集合大小。

```
LLEN 集合key
```

![image-20230723142840052](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723142840052.png)

****

**推荐值**

* 单个key的value小于10KB
* 对于集合类型的key，建议元素数量小于1000



### 1.2.2 BigKey的危害

* **网络阻塞**
  对BiqKey执行读请求时，少量的QPS就可能导致带宽使用率被占满，导致Redis实例，乃至所在物理机变慢
* **数据倾斜**
  BigKey所在的Redis实例内存使用率远超其他实例，无法使数据分片的内存资源达到均衡
* **Redis阻赛**
  对元素较多的hash、list、zset等做运算会耗时较久，使主线程被阻塞
* **CPU压力**
  对BigKey的数据序列化和反序列化会导致CPU的使用率飙升，影响Redis实例和本机其它应用

### 1.2.3 如何发现BigKey

* **redis-cli --bigkeys**

  > 如果有密码的话，执行redis-cli  -a 密码 --bigkeys

  利用redis-cli提供的--bigkeys参数，可以遍历分析所有key，并返回Key的整体统计信息与每个数据的Top1的big key

  ![image-20230723144617823](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723144617823.png)



> 但是这个统计只能看到top1，那第二是不是BigKey呢？第三呢？都不一定，这就是坏处，信息不够完整，但是可以作为一个基本的参考



* **scan扫描**
  自己编程，利用scan扫描Redis中的所有key，**利用strlen、hlen等命令判断key的长度**(此处不建议使用MEMORY USAGE)第三方工具

> 逐个扫描所有的key，不会占用Redis当中的主线程，一次只扫描一小部分

![image-20230723144930425](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723144930425.png)

* * **cursor**：游标，第一次给0即可。然后第二次用第一次查询返回的结果

    当光标回到0的时候，证明全部扫描完毕了

![image-20230723145428128](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723145428128.png)

* * **MATCH pattern**：要匹配哪种类型的key，不指定就是所有的key

* * **COUNT**：一次获取几个，默认是10个

  下面的光标回到了0，证明全部扫描完毕了

![image-20230723145245269](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723145245269.png)



```java
    //设置的字符串最大大小
    final static int STR_MAX_LEN = 10 * 1024;
    //设置Hash的最大大小
    final static int HASH_MAX_LEN = 500;

    @Test
    void testScan() {
        int maxLen = 0;
        long len = 0;
        //初始化游标
        String cursor = "0";
        do {
            // 扫描并获取一部分key
            ScanResult<String> result = jedis.scan(cursor);
            // 记录cursor
            cursor = result.getCursor();
            //扫描到的这部分key
            List<String> list = result.getResult();
            if (list == null || list.isEmpty()) {
                break;
            }
            // 遍历
            for (String key : list) {
                // 判断key的类型
                String type = jedis.type(key);
                switch (type) {
                    case "string":
                        len = jedis.strlen(key);
                        maxLen = STR_MAX_LEN;
                        break;
                    case "hash":
                        len = jedis.hlen(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    case "list":
                        len = jedis.llen(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    case "set":
                        len = jedis.scard(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    case "zset":
                        len = jedis.zcard(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    default:
                        break;
                }
                if (len >= maxLen) {
                    System.out.printf("Found big key : %s, type: %s, length or size: %d %n", key, type, len);
                }
            }
        } while (!cursor.equals("0"));
    }
```





* **利用第三方工具**

  如 Redis-Rdb-Tools 分析RDB快照文件，全面分析内存使用情况

  > [GitHub - sripathikrishnan/redis-rdb-tools: Parse Redis dump.rdb files, Analyze Memory, and Export Data to JSON](https://github.com/sripathikrishnan/redis-rdb-tools?spm=a2c4q.11186623.0.0.14073c9cldKVDv)
  >
  > 但是需要Python的运行环境

* **网络监控**
  自定义工具，监控进出Redis的网络数据，超出预警值时主动告警

### 1.2.4  删除BigKey

BigKey内存占用较多，即便时删除这样的key也需要耗费很长时间，导致Redis主线程阻塞，引发一系列问题。

* r**edis 3.0 及以下版本**
  如果是集合类型，则遍历BigKey的元素，先逐个删除子元素，最后删除BigKey

* **Redis 4.0以后**
  Redis在4.0后提供了异步删除的命令: unlink

![image-20230723165122587](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723165122587.png)

![image-20230723165150416](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723165150416.png)





## 1.3 恰当的数据结构



### 1.3.1 案例：存储对象

比如存储User对象，我们有三种存储方式：

**方式一：JSON字符串**

优点：实现简单粗暴

缺点：数据耦合，不够灵活。不能针对某一个字段进行操作

![image-20230723165737837](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723165737837.png)



**方式二：字段打散**

优点：灵活访问任意字段

缺点：占用空间大、无法做统一控制（想获取User的信息只能一条一条的获取）

![image-20230723165746933](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723165746933.png)



**方式三：hash**

优点：底层使用ziplist，空间占用小，可以灵活访问对象的任意字段

缺点：代码相对复杂

![image-20230723165756993](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723165756993.png)



### 1.3.2 案例：优化Key

假如有hash类型的key，其中有100万对field和value，field是自增id，这个kev存在什么问题? 如何优化?

![image-20230723170509794](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723170509794.png)

**第一个问题：内存占用问题**

* **方案一**

我们的Hash结构底层使用ziplist，优点不是占用空间小嘛？为什么这里还会出现内存占用问题

Redis的Hash类型的ziplist是有使用条件的：

当hash的entry数量超过500时，会使用哈希表而不是ziplist，内存占用过多。

可以通过hash-max-ziplist-entries配置entry上限，但是如果entry过多会导致BigKey问题（所以说不建议这么做）

![image-20230723171104439](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723171104439.png)



* **方案二**

拆分String类型，将一个Hash类型转换成了一个100万个key，但是这么做会有很大的内存占用问题

![image-20230723171329530](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723171329530.png)

string结构底层没有太多内存优化，内存占用较多

想要批量获取这些数据比较麻烦



* **方案三**

拆分为小的hash，将id/100作为key（将来会有1万个Key），将id%100作为field，这样每100个元素作为一个Hash

![image-20230723172102847](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723172102847.png)



## 1.4 总结

**Key的最佳实践**

* 固定格式:[业务名]:[数据名]:[id]
* 足够简短:不超过44字节
* 不包含特殊字符

**Value的最佳实践**

* 合理的拆分数据，拒绝BigKey
* 选择合适数据结构
* Hash结构的entry数量不要超过1000
* 设置合理的超时时间



# 二、批处理优化

 数据量少的情况下可以写一个Java代码将数据库中数据写入到Redis中，但是如果数据量非常多的话，就不能像之前那么写了。



## 2.1 Pipeline

### 2.1.1 单个命令的执行流程

假如说有一条命令要发送

首先客户端会向Redis发起一个请求，这里的命令需要在网络中传输

Redis服务端收到命令后便执行命令，执行完后再将结果返回给客户端

也就是说：**一次命令的响应时间=1次往返的网络传输耗时+1次Redis执行命令耗时**

![image-20230723195923054](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723195923054.png)

**命令在网络中传输的时间与Redis执行命令的时间，哪个更长一点？**

Redis的执行时间是非常快的，Redis的并发往往可以达到数万，也就是说执行一次命令的耗时是数万分之一（微秒）。

我们往返的网络耗时是在毫秒之间，相对于我们执行命令的耗时，相差了好多个数量级（也就是说执行命令的时间可以忽略不计）



### 2.1.2 N条命令依次执行

N次命令的响应时间=N次往返的网络传输耗时+N次Redis执行命令耗时

因为执行命令的时间都不长，可以认为时间都浪费在网络传输上面了。

![image-20230723201700776](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723201700776.png)

不建议使用这种方式，因为我们要节省运输的时间，提高效率。



### 2.1.3 N条命令批量执行

**我们要N条命令批量执行怎么办**？

我们在发送命令的时候，客户端直接把n条命令一次性向Redis服务端发送过去，在Redis的服务端直接依次执行n个命令，最后把执行的所有结果合并在一起一次性返回。

**N次命令的响应时间=1次往返的网络传输耗时+N次Redis执行命令耗时**

![image-20230723202302465](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723202302465.png)

**怎么样才能批量的执行命令呢**？

Redis提供了很多Mxxx这样的命令，可以实现批量插入数据，例如：

mset、hmset

* **mset**

![image-20230723202534052](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723202534052.png)

利用mset批量插入10万条数据.这里我们一次性添加1000个key与value，并没有一次性添加10万条。如果一次性添加10万条的话，会加大网络传输的压力。

```java
String[] arr = new String[2000];
int j;
for (int i = 1; i<= 100000; i++){
    //任何数字%1000 是在0~999范围内
    //<< 1 表示左移一位，也就是×2的意思
    //在这里实现了，j永远是偶数，j+1永远是奇数，这样键和值就仅仅挨在一起，是连续的
    j= (i% 1000) << 1;
    arr[j] = "test:key_" + i;
    arr[j + 1] = "value_" + i;
    
    if (j== 0){
        jedis.mset(arr);
    }
}
```

> 一次插入过多的数据，可能会直接把带宽给占满，导致网络堵塞。
>
> 在我们这里是1000个键值对，1000组数据

但是mset只能处理string数据类型

* **hmset**

哈希里面的批处理，只能处理Hash，别的不行



### 2.1.4 Pipeline批处理

MSET虽然可以批处理，但是却只能操作部分数据类型，因此如果有复杂数据类型的批处理需要，建议使用Pipeline功能

> 这是一种管道方案，把我们很多很多的命令塞到管道里，一次性传过去，这种方式和mset比较像，但不一样

```java
// 创建管道
Pipeline pipeline = jedis.pipelined();

for (int i = l; i <= 100000; i++) {
    // 放入命令到管道
    pipeline.set("test:key_" + i, "value_" + i);
    if (i % 1000 == 0) {
        // 每放入1000条命令，批量执行
        pipeline.sync();
    }
}    
```



### 2.1.5 总结

**Pipeline与mset的区别**

mset这样的命令是有一定的限制的，而Pipeline是无限制的，可以任意命令做组合，而且key可以自定义

mset这样的操作要比Pipeline操作要快，因为m操作是Redis内置的操作。m操作里面的多组key和value，它会把它作为一个原子性的操作，也就是说一次性全执行完，中间不会有其他命令来插队。

但是对于Pipeline来说，这些命令只是一块到达Redis的，但是一块不一块执行可就不一定了

Pipeline里面的命令传输是有先后顺序的，而且我们的命令在传输给Redis的过程中，其他的客户端也可以向Redis传输命令，这些命令到达Redis的时间是有先后顺序的，这些命令到达Redis之后会进入一个队列做排队，然后Redis的线程会依次执行命令

**批量处理的方案**

* 原生的M操作
  
* Pipeline批处理

  

**注意事项**:

* 批处理时不建议一次携带太多命令
* pipeline的多个命令之间不具备原子性





## 2.2 集群下的批处理

如MSET或Pipeline这样的批处理需要在一次请求中携带多条命令，而此时如果Redis是一个集群，那批**处理命令的多个key必须落在一个插槽中，否则就会导致执行失败**。

> **为什么批处理的多个key插槽必须一样**？
>
> 集群模式下总共会用16000多个插槽，分配到不同的Redis节点上，当我们去set任意的一个key的时候，它会根据key计算出插槽值，然后放到对应的节点上。
>
> 我们现在批处理，一次携带上千条的命令，这些命令计算出来的插槽值很有可能是不一样的，这样一来他们保存到不同的节点上（保存到不同的节点上需要很多个连接 ）
>
> 但是我们要的批处理是要在一次连接当中，把所有的命令都执行，

![image-20230723215745224](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723215745224.png)

**第三种并行slot方式**其实就是第二种串行slot方法的延伸。我们可以使用多线程来执行各组传输的命令，而且各组的传输是同时进行的，那这样就可以看做是一次网络传输

****



对于**第四种hash_tag方式**可以查看下面文章中的插槽介绍

[ Redis-持久化、主从集群、哨兵模式、分片集群、分布式缓存_我爱布朗熊的博客-CSDN博客](https://blog.csdn.net/weixin_51351637/article/details/131744537#43__1258)

我们把每一个key都指定上一个相同的前缀（hash_tag），那最终计算出来的插槽也是相同的，那这样的话，批处理也能够执行

![image-20230723220703157](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723220703157.png)

但是第四种也有一些问题。比如说我们批量处理的时候有一万条数据，那我们使用第四种方式的时候就是将一万条数据都放到同一个插槽上面了，这个插槽所对应的数据节点存储的数据就比较多，这就叫做数据倾斜

****

**推荐第三种并行slot方式**

```java
    private JedisCluster jedisCluster;

    @BeforeEach
    void setUp() {
        // 配置连接池
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8);
        poolConfig.setMaxIdle(8);
        poolConfig.setMinIdle(0);
        poolConfig.setMaxWaitMillis(1000);
        HashSet<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.150.101", 7001));
        nodes.add(new HostAndPort("192.168.150.101", 7002));
        nodes.add(new HostAndPort("192.168.150.101", 7003));
        nodes.add(new HostAndPort("192.168.150.101", 8001));
        nodes.add(new HostAndPort("192.168.150.101", 8002));
        nodes.add(new HostAndPort("192.168.150.101", 8003));
        jedisCluster = new JedisCluster(nodes, poolConfig);
    }
```



> @BeforeEach注解的作用如下：
>
> 1. 在每个测试方法执行之前，用于设置测试环境，例如初始化测试数据、创建对象实例等。
> 2. 提供了一种统一的方式来确保在每个测试方法中都执行某些共同的操作。
> 3. 可以减少重复的代码，提高测试代码的维护性和可读性。



执行下面代码

```java
    @Test
    void testMSet() {
        jedisCluster.mset("name", "Jack", "age", "21", "sex", "male");

    }
```

控制台显示没有办法去做这个分配。jedisCluster默认没有帮我们解决集群下的批处理问题

![image-20230723230656525](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230723230656525.png)

只能我们自己解决批处理问题

**串行slot方法**

```java
    @Test
    void testMSet2() {
        Map<String, String> map = new HashMap<>(3);
        map.put("name", "Jack");
        map.put("age", "21");
        map.put("sex", "Male");

        Map<Integer, List<Map.Entry<String, String>>> result = map.entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        entry -> ClusterSlotHashUtil.calculateSlot(entry.getKey()))
                );
        for (List<Map.Entry<String, String>> list : result.values()) {
            String[] arr = new String[list.size() * 2];
            int j = 0;
            for (int i = 0; i < list.size(); i++) {
                j = i<<2;
                Map.Entry<String, String> e = list.get(0);
                arr[j] = e.getKey();
                arr[j + 1] = e.getValue();
            }
            jedisCluster.mset(arr);
        }
    }
```

**spring的实现**

spring提供的客户端stringRedisTemplate，已经解决了集群下的批处理的问题

```java
  @Test
    void testMSetInCluster() {
        Map<String, String> map = new HashMap<>(3);
        map.put("name", "Rose");
        map.put("age", "21");
        map.put("sex", "Female");
        stringRedisTemplate.opsForValue().multiSet(map);


        List<String> strings = stringRedisTemplate.opsForValue().multiGet(Arrays.asList("name", "age", "sex"));
        strings.forEach(System.out::println);

    }
```

**所以我们直接使用上面的Spring代码即可，Spring已经帮我们完成了封装**