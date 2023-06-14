

# Spring boot项目实战小知识点





# 一、spring boot工程打包





原文链接：https://blog.csdn.net/qq_47183158/article/details/122999763





## 1.1  使用Maven进行打包

SpringBoot自带一个更简单的spring-boot-maven-plugin插件可以用来打包，只需要在pom.xml中加入以下配置：

> ```xml
> <project>
>     <build>
>         <plugins>
>             <plugin>
>                 <groupId>org.springframework.boot</groupId>
>                 <artifactId>spring-boot-maven-plugin</artifactId>
>             </plugin>
>         </plugins>
>     </build>
> </project>
> ```



无需任何配置，这个插件会自动定位应用程序的入口Class，执行以下Maven命令即可打包：



```
$ mvn clean package
```








## 1.2  使用IDEA进行打包



右键我们的项目，然后点击“Open Module Settings”

![image-20230222131417357](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222131417357.png)



以此点击  ”Artifacts“--->  “+”  --->  "JAR " --->   "From modules with dependencies "

![image-20230222131543695](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222131543695.png)



![image-20230222131733015](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222131733015.png)



![image-20230222131757647](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222131757647.png)



![image-20230222131823014](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222131823014.png)



​      开始打包，点击右侧的Maven Projects，打开`LIfecycle`，先点击`clean`，再点击`package`，生成target文件夹，里面有以项目名命名加版本号的jar文件，至此打包完成。

![image-20230222131957233](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222131957233.png)







## 1.3 运行jar包



在我们的目录下找到target文件夹，在此文件夹中找到我们所需的jar包



![image-20230222132433799](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222132433799.png)



运行窗口运行下面的命令

```
java -jar mock-0.0.1-SNAPSHOT.jar
```



![image-20230222132345382](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222132345382.png)









# 二、从配置文件中读取Map集合最简单的方式



**配置文件内容**

```yaml
MockControllerYAML:
  path:
    customerDTOHashMap: '{"12345678":7,"1234567":5}'


```



**提取内容的代码**

```java
    @Value("#{${MockControllerYAML.path.customerDTOHashMap:{}}}")
    HashMap<String, Integer> customerDTOHashMap = new HashMap<>();
```





# 三、使用两条不同的URL，请求同一个接口



   经过我的尝试，第一种方式我好像实现不了，只能实现第二中凡是

> 资料来源：https://blog.csdn.net/qq_40898875/article/details/116464903?ops_request_misc=&request_id=&biz_id=102&utm_term=%E6%80%8E%E4%B9%88%E5%AE%9E%E7%8E%B0%E4%B8%8D%E5%90%8C%E8%AF%B7%E6%B1%82%E8%AE%BF%E9%97%AE%E5%90%8C%E4%B8%80%E4%B8%AA%E6%96%B9%E6%B3%95&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduweb~default-3-116464903.142^v73^control_1,201^v4^add_ask,239^v2^insert_chatgpt&spm=1018.2226.3001.4187







```java
@RestController
@RequestMapping(value ={"/mock","/register"})
public class MockController {
    @PostMapping("/damBoard")
    public ResultObject damBoard(@RequestBody CustomerDTO customerDTO){}
    
}
```



下面两张方式都是可以的



![image-20230223102732967](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230223102732967.png)

![image-20230223102752183](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230223102752183.png)









# 四、使用maven坐标导入时出现cannot reconnect 

​      我在导入坐标的时候出现了cannot reconnect  ，翻译：不能连接我把idea重启了一下，解决了这个错误

​      下次再遇到这个错误的时候，如果重启idea解决不了这个问题，在子项目的pom.xml文件下更改下面的配置

​      groupId、artifactId、version三个参数直接从父工程中获取

​     多增添一条 <relativePath>../pom.xml</relativePath>，路径直接指向父工程的pom.xml文件文件

```xml
<parent>
    <groupId>cn.itcast.demo</groupId>
    <artifactId>cloud-demo</artifactId>
    <version>1.0</version>
    <relativePath>../pom.xml</relativePath>
</parent>
```





# 五、@LoadBalanced负载均衡

> 视频资料：[12-Ribbon-负载均衡原理_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1LQ4y127n4/?p=14&spm_id_from=pageDriver&vd_source=c01240addcba226237f3c4781490fbae)





```java
@MapperScan("cn.itcast.order.mapper")
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }

}
```



@LoadBalanced是一个标记

   标记 RestTemplate发起的请求要被我们将来的Ribbon拦截和处理



那这个拦截的动作是谁去完成的呢？

​     下面这个拦截器就会拦截由客户端发起的http请求

![image-20230227180644586](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230227180644586.png)



下面是定义方法的名字

![image-20230227180738144](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230227180738144.png)







# 六、@Builder注解



> 资料来源：http://fendou.net.cn/index.php/a/369
>
> ​                    https://blog.csdn.net/qq_39249094/article/details/120881578





- 作用于类，将其变成建造者模式
- 可以以链的形式调用
- 初始化实例对象生成的对象是不可以变的，可以在创建对象的时候进行赋值（如果想改变的话需要在@Builder后面添加参数toBuilder=true）
- 需要在原来的基础上修改可以加 set 方法，final 字段可以不需要初始化
- 生成一个全参的构造函数









## 6.0 Lombok坐标

```xml
<dependency>
   <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>         
    <version>0.10.2</version>
</dependency>
```



​         提供在设计数据实体时，对外保持private setter，而对属性的赋值采用Builder的方式，这种方式最优雅，也更符合封装的原则，不对外公开属性的写操作



​         @Builder声明实体，表示可以进行Builder方式初始化

​         @Value注解，表示只公开getter，对所有属性的setter都封闭，即private修饰，所以它不能和@Builder一起用



## 6.1 注解使用

```java
@Builder
@Getter
@Data
public class UserInfo {
    private String name;
    private String email;

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public static void main(String[] args) {
        UserInfo userInfo = UserInfo.builder().build();
        System.out.println("userInfo---->"+userInfo);

        UserInfo userInfo1 = UserInfo.builder()
                .name("zzl")
                .email("bgood@sina.com")
                .build();
        System.out.println("userInfo1---->"+userInfo1);
    }
}
```



![image-20230302104930099](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230302104930099.png)







## 6.2 注解的属性介绍



### 6.2.1 toBuilder

- 设置为 true 可以对这个对象进行拷贝生成新的对象，可以再修改，默认为 false

​    

**怎么设置为true？**

​         @Builder(toBuilder = true)



**我们使用UserInfo.builder().build()创建出来之后，还可以修改对象的内容么(不使用set方法)？**

![image-20230302105454190](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230302105454190.png)

​    

我们此时发现如果想对已经构建了的对象在修改的话，会出错，并找不到这个方法，我们只需要在类注解上添加@Builder(toBuilder = true)即可

  

```java
@Builder(toBuilder = true)
@Getter
public class UserInfo {}
```

```java
userInfo = userInfo.toBuilder()
        .name("OK")
        .email("zgood@sina.com")
        .build();
```



### 6.2.2  @Builder.Default 注解

​    非 final 的字段可以有默认值



```java
@Builder.Default
private String name = "刘亦菲";
```



​    我们下面虽然没有对name赋值，但是输出时”name“依然会时"刘亦菲"

```java
UserInfo userInfo = UserInfo.builder().build();
System.out.println("userInfo---->"+userInfo);
```





​      final字段加不加Default都可以初始化成功，因为final字段如果第一次不是null的话，就不可修改（简单的来说，final字段有了初始值之后就不可更改）

```java
private final Integer age = 18; 
```

​    这两种写法都可以

```java
@Builder.Default
private final Integer age;  
```





### 6.2.3 buildMethodName

​           指定创建实体类的方法名，默认值为 build

​          当我们指定内部静态类的方法名为“test”的时候，发现下面已经开始报错了

![image-20230302111413469](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230302111413469.png)



当我们把这里改成test之后便不会报错了

![image-20230302111505009](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230302111505009.png)





### 6.2.4  builderMethodName

  指定创建内部静态类的方法名，默认值为 builder

![image-20230302111729415](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230302111729415.png)





### 6.2.5  builderClassName

指定内部静态的类名，默认值为 “”，默认创建的类名为 **thisclassBuilder**



  这个我不太懂，不知道怎么演示





### 6.2.6 access

设置 builderMethodName 的访问权限修饰符，默认为 public

共有 PUBLIC、MODULE、PROTECTED、PACKAGE、PRIVATE，其中 MODULE 是 Java 9 的新特性



**access = AccessLevel.PUBLIC**







### 6.2.7 setterPrefix

​      设置 setter 方法的前缀，默认为 “”







## 6.3 处理添加无参构造函数报错时报错

@Builder 会生成一个全参构造方法，因此就没有了无参构造方法，但当我们遇到需要无参构造方法时就会发生问题，这个时候手写或者加上 [@NoArgsConstructor](https://blog.csdn.net/qq_39249094/article/details/120987277) 都会报错

![image-20230302112434951](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230302112434951.png)





### 6.3.1 处理方案1

​      **加上 @AllArgsConstructor**

![image-20230302112646716](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230302112646716.png)





### 6.3.2 处理方案2

​        使用 @Builder 对一个 DTO 实现一个构造器，但是在做 Json 反序列化的时候发生错误，原因就是缺少无参公共的构造函数，而手动写一个无参构造函数的时候编译错误，就是和 @Builder 冲突

​       虽然标准的 @Builder 没法是需要私有化构造函数的，但是在某些场景下我们需要对这种标准变形，这个时候 lombok 提供了 @Tolerate 实现对冲突的兼容





  **使用@Tolerate注解**



我们手动添加一个无参构造函数，但是当运行之后就会出现错误

![image-20230302114258210](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230302114258210.png)



但是当我们在无参构造函数上添加@Tolerate注解之后就可以正常运行

![image-20230302114410710](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230302114410710.png)

## 6.4 @Builder内部



- 创建一个名为 ThisClassBuilder 的内部静态类，并具有和实体类相同的属性（称为构建器）
- 在构建器中：对于目标类中的所有的属性和未初始化的 final 字段，都会在构建器中创建对应属性
- 在构建器中：创建一个无参的 default 构造函数
- 在构建器中：实体类中的每个参数，都会对应创建类似于 setter 的方法，方法名与该参数名相同。 并且返回值是构建器本身（便于链式调用）
- 在构建器中：会创建一个 build 方法，调用 build 方法，就会根据设置的值进行创建实体对象
- 在构建器中：会生成一个 toString 方法
- 在实体类中：会创建一个 builder 方法，它的目的是用来创建构建器
  

```java
@Builder
public class User {
    private String username;
    private String password;
}
```






​    
```java
public class User {
    private String username;
    private String password;


    User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
//  在实体类中会创建一个 builder 方法，它的目的是用来创建构建器
    public static User.UserBuilder builder() {
        return new User.UserBuilder();
    }
//  构建器
    public static class UserBuilder {
        //在构建器中：对于目标类中的所有的属性和未初始化的 final 字段，都会在构建器中创建对应属性
        private String username;
        private String password;
        
        //在构建器中：创建一个无参的 default 构造函数
        UserBuilder() {
        }

        //在构建器中：实体类中的每个参数，都会对应创建类似于 setter 的方法，方法名与该参数名相同。 并且返回值是构建器本身（便于链式调用）        
        public User.UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public User.UserBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        //在构建器中：会创建一个 build 方法，调用 build 方法，就会根据设置的值进行创建实体对象
        public User build() {
            return new User(this.username, this.password);
        }
        
        //在构建器中：会生成一个 toString 方法 
        public String toString() {
            return "User.UserBuilder(username=" + this.username + ", password=" + this.password + ")";
        }
    }
}   
```










# 七、@ApiModel注解与@ApiModelProperty注解



> 资料来源：https://juejin.cn/post/7109835493952454693



## 7.1 初了解

​     **@ApiModel**注解是用**在接口相关的实体类**上的注解，它主要是用来对使用该注解的接口相关的实体类**添加额外的描述信息**，**常常和@ApiModelProperty注解配合使用**





​    **@ApiModelProperty**注解则是作用**在接口相关实体类的属性（字段）**上的注解，用来对具体的接口相关实体类中的**参数添加额外的描述信息**，除了可以和 @ApiModel 注解关联使用，也会单独拿出来用。



作用域不同，@ApiModel作用在类上，@ApiModel作用来属性上







## 7.2 Maven坐标

导入swagger的依赖：

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-annotations</artifactId>
    <version>1.5.13</version>
</dependency>

```





## 7.3 ApiModel

主要的属性有value属性和description属性



- **value**属性就是对所需要特别说明的接口相关实体类进行描述。

  ​      具体使用就如上面的例子一样，如果不使用value时，默认值就是实体类的名称，所以除非有特殊说明或者实体类不清晰，否则直接使用默认值即可。

  

- **description**属性就是对所需要特别说明的接口相关实体类进行较长的描述。

  ​        比如上面的例子，如果想对用户实体添加必要的描述信息，可以如下所示：



```java
@ApiModel(value = "用户实体类，用户相关字段", description = "用户实体中包含用户相关的所有业务字段，主要字段有姓名、年龄、性别，用于登录使用")
public class User{
}
```











## 7.4 ApiModelProperty

属性：value、name、required 、hidden、allowEmptyValue



```java
@Data
@ApiModel(value = "用户实体类，用户相关字段", description = "用户实体中包含用户相关的所有业务字段，主要字段有姓名、年龄、性别，用于登录使用")
public class User {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("姓名")
    private String name;
}

```





### 7.4.1 value属性

对实体类中的字段进行描述和补充说明，解释该字段代表什么意思。

理解为它就是一个注释的作用，方便清楚字段的含义。

```java
@ApiModelProperty(
    value = "是否成功：200-成功，其他失败",
    required = true,
    example = "200"
)
private String code;
```







### 7.4.2 name属性

**name**属性即重写该属性名字，比如上述例子，name可以这样使用

```java
@ApiModelProperty(value = "user表主键Id",name="id")
   private Integer id;
```



### 7.4.3 required 属性

**required**属性就是用来描述实体中的参数字段是否必传,默认false，如果使用true，则该字段后面会有一个红色的星号

```java
 @ApiModelProperty(value = "user表主键Id",name="id",required=true)
   private Integer id;
   
   @ApiModelProperty(required = false)//或者不行就是默认false
   private Integer age;

```





### 7.4.4 **hidden**属性

用来描述实体中参数字段是否显示在Swagger界面中，默认也是false，true表示隐藏。

```java
   @ApiModelProperty(hidden = true)
   private String address;
```





### 7.4.5 **allowEmptyValue**属性

​       用来描述实体参数的值是否可以为空值。在 ApiModelProperty 注解中直接声明 allowEmptyValue属性的值即可，如果不声明该属性，则默认为false，即字段参数的值不可以为空。

​       使得master字段声明其值可以为空，即在参数传递时可以不填充值

```java
  @ApiModelProperty(allowEmptyValue = true)
   private String master;
```





### 7.4.6  **example**属性

```java
@ApiModelProperty(
    value = "是否成功：200-成功，其他失败",
    required = true,
    example = "200"
)
private String code;
```







### 7.4.7 dataType属性

表示的是字段的类型

```java
@ApiModelProperty(required = false，dataType = "int")//或者不写就是默认false
   private Integer age;
```











```
i18n:
  props:
    zh_CN:
      SystemConfigEnum:
        DEFAULT_DOMAIN_DATA_MODEL_NAME: 数据模型
        DEFAULT_SYSTEM_NAME: 系统
        STRATEGY_NAME: 策略
        DIRECTORY_ROOT: 策略根目录
        DEFAULT_TEST_NAME: 测试数据集
        COPY_TO_STRATEGY: 复制于策略
        DEFAULT_DES: 默认细分
      ApproveResultTypeEnum:
         PASS: 审批通过
         REFUSED: 审批拒绝
      BlazeStrategyActionTypeEnum:
        ADD_NEW: 新建
        SUB_VERIFY: 提交审批
        APPROVAL_RELEASE: 审批通过
        APPROVAL_REJECT: 审批拒绝
        TO_TEST: 退回开发
        ENABLE: 启用
        DISABLE: 停用
        DELETE: 删除
        ONLINE_PUBLISH: 发布
        AUTO_DISABLE: 自动停用
        PUBLISH_IMPORT: 导入
      BlazeStrategyStatusEnum:
        DEV: 开发
        APPROVING: 待审批
        PASS: 审批通过
        REJECT: 审批拒绝
        RELEASED: 启用
        DEACTIVATED: 停用
        DISCARD: 已删除
      OpTypeEnum:
        MODIFY: 修改
        DELETE: 删除
      PredicateOperatorEnum:
        EQUAL: 等于
        NOT_EQUAL: 不等于
        IN: 在集合
        NOT_IN: 不在集合
        INCLUDE: 包含
        NOT_INCLUDE: 不包含
        LESS_THAN: 小于
        LESS_OR_EQUAL: 小于等于
        GREATER_THAN: 大于
        GREATER_OR_EQUAL: 大于等于
        CLOSE_OPEN: 左闭右开
        OPEN_OPEN: 开区间
        OPEN_CLOSE: 左开右闭
        CLOSE_CLOSE: 闭区间
        NA: N/A
        'NULL': 为空
        NONULL: 不为空
        ELSE: ALL other  
```







```
ComputeModeEnum:
  CALL_COUNT: 按数量
  SUCCESS: 成功率
  SELECT: 查得率
  RESP: 平均响应时间
  MAX: 最大响应时间
  RATIO: 按比例
DimensionEnum:
  SERVER: 服务
  BUCKET: 细分
FunctionTypeEnum:
  RANGE: 区间范围
  QUARTILE: 四分位
  TENTHS: 十分位
  TEMPLATE: 变量模板
  FUNCTION: 公共方法
  PREP: 数据预处理
SelectTimeEnum:
  CURRENT: 当天
  SEVEN: 最近7天
  MONTH: 最近30天
  YEAR: 最近1年
  ALL: 所有
  ASSIGN: 指定日期
  MONTH3: 3个月
  MONTH6: 6个月
  YEAR2: 2年
SelectTypeEnum:
  CALL_COUNT: 调用量
  STATE: 状态分布
  RESULT_DIST: 结果分布
  RATIO: 比例
TimeGroupTypeEnum:
  hour: 小时
  day: 一天
  month: 一个月
  year: 一年
AbtestGroupTypeEnum:
  CHAMPION: 冠军分组
  CHALLENGER: 挑战者分组
AdminModuleErrorCode:
  COMMON_DEFAULT: 已知的业务处理
  COMMON_SEARCH: 首页全局搜索
  COMMON_VAR_TREE: 变量树统一接口
  COMMON_TYPE: 返回类型
  COMMON_SCORE_RESULT: 获取评分结果
  COMMON_PMML: PMML模型执行结果
  COMMON_MATCH_VAR: 变量匹配树接口
  COMMON_TEMPLATE_STATIC: 静态模板
  COMMON_TEMPLATE_VAR_BY_PATH: 根据路径查询变量树
  COMMON_TEMPLATE_PROVIDEER_ADD: dataProvider追加数据接口
  COMMON_TEMPLATE_PROVIDER_THIS: 只有this_dataProvider追加数据接口
  COMMON_TEMPLATE_PROVIDER_BY_PATH: 通过变量路径查询providerName
  COMMON_TEMPLATE_BUCKET: 细分分流静态模板配置+所有dataProvider数据
  COMMON_TEMPLATE_OUTSIDE: 外部服务静态模板配置+所有dataProvider数据
  COMMON_TEMPLATE_PARENT_ATT: 根据变量路径（对象）获取一级基本属性
  COMMON_TEMPLATE_SAME_TYPE_OBJ: 根据变量路径（对象）获取相同类型的对象变量
  COMMON_TEMPLATE_DYNAMIC_OBJ: 根据变量路径（对象）动态获取相对象
  COMMON_TEMPLATE_SAME_OBJ: 根据变量路径（对象）获取相对象数组
  COMMON_TEMPLATE_PARENT_DIFF: 根据变量路径（对象）一级属性对比
  COMMON_TEMPLATE_OUTPUT_VAR: 获取输出数据
  SYS_CONFIG_DEFAULT: 已知的业务处理
  SYS_CONFIG_SAVE: 新增修改系统参数
  SYS_CONFIG_SET: 设置参数值
  SYS_CONFIG_LIST: 参数列表
  SYS_CONFIG_BY_NAME: 根据参数名查参数信息
  SYS_CONFIG_DELETE_CHECK: 根据系统参数id删除校验
  SYS_CONFIG_DELETE: 删除系统参数
  SYS_CONFIG_DOCUMENT_LIST: 系统帮助文档列表
  SYS_CONFIG_DOCUMENT_TREE_LIST: 系统帮助文档树列表
  SYS_CONFIG_DOCUMENT_DETAIL: 系统帮助文档详情
  SYS_CONFIG_DOCUMENT_ADD: 添加系统帮助文档
  SYS_CONFIG_DOCUMENT_UPDATE: 修改系统帮助文档
  SYS_CONFIG_DOCUMENT_DELETE: 删除系统帮助文档
  SYS_CONFIG_DOCUMENT_CATEGORY_LIST: 系统帮助文档分类列表
  SYS_CONFIG_DOCUMENT_CATEGORY_ADD: 系统帮助文档添加分类
  SYS_CONFIG_DOCUMENT_CATEGORY_UPDATE: 系统帮助文档修改分类
  SYS_CONFIG_DOCUMENT_CATEGORY_DELETE: 系统帮助文档删除分类
  ENV_CONFIG_DEFAULT: 已知的业务处理
  ENV_CONFIG_QUERY: 查询环境参数
  ENV_CONFIG_UPDATE: 更新环境参数
  ENV_CONFIG_TARGET_ADD: 创建目标环境配置
  ENV_CONFIG_TARGET_QUERY: 查询目标环境配置
  ENV_CONFIG_TARGET_ALL: 查询全部环境配置
  ENV_CONFIG_TARGET_UPDATE: 更新环境配置
  ENV_CONFIG_TARGET_START_STOP: 启用停用环境配置
  ENV_CONFIG_TARGET_DELETE: 删除目标环境配置
  LOGS_DEFAULT: 已知的业务处理
  LOGS_LIST: 日志列表
  SYS_DYNAMIC_DEFAULT: 已知的业务处理
  SYS_DYNAMIC_LIST: 动态信息
  SYS_DYNAMIC_URL: 获取动态信息跳转链接
  PROFILES_DEFAULT: 已知的业务处理
  PROFILES_WORK_DOMAIN: 我工作的领域
  PROFILES_WORK_STRATEGY: 策略工作区
  PROFILES_PART_STRATEGY: 我参与的策略
  PROFILES_WAIT_STRATEGY: 待审批的策略
  PROFILES_APPROVAL_STRATEGY: 审批的策略
  PROFILES_CHECK_COMPONENT: 我检出的组件
  PROFILES_PART_COMPONENT: 我参与的组件
  PROFILES_EDIT_COMPONENT: 待修改的组件
  PROFILES_RECYCLE_TYPE: 个人回收站类型
  PROFILES_RECYCLE_LIST: 个人回收站
  PROFILES_RECYCLE_RECOVERY: 个人回收站-恢复
  PROFILES_RECYCLE_CLEAR: 个人回收站-彻底删除
  DOMAIN_DEFAULT: 已知的业务处理
  DOMAIN_LIST: 获取领域列表
  DOMAIN_LIST_BY_TYPE: 根据类型获取领域列表
  DOMAIN_DETAIL: 领域详情
  DOMAIN_ATTRIBUTE: 领域概述查询
  DOMAIN_ADD: 添加领域
  DOMAIN_UPDATE: 修改领域
  DOMAIN_DELETE_CHECK: 删除领域校验
  DOMAIN_DELETE: 删除领域
  DOMAIN_DATA_BOARD_DEFAULT: 已知的业务处理
  DOMAIN_DATA_BOARD_DATA: 获取看板数据
  DOMAIN_DATA_BOARD_SUMMARY: 资源概览
  DOMAIN_DATA_BOARD_DATA_BOARD: 数据看板/概览
  DOMAIN_TEAM_DEFAULT: 已知的业务处理
  DOMAIN_TEAM_QUERY_USER: 查找用户
  DOMAIN_TEAM_ADD: 添加成员并分配角色
  DOMAIN_TEAM_LIST: 查询领域团队成员
  DOMAIN_TEAM_CONFIG_ROLE: 设置成员角色
  DOMAIN_TEAM_REMOVE: 移除团队成员
  DOMAIN_TEAM_REMOVE_CHECK: 移除团队成员校验
  DOMAIN_RESOURCE_DEFAULT: 已知的业务处理
  DOMAIN_RESOURCE_LIST: 获取资源列表
  DOMAIN_RESOURCE_SAVE_ROLE_PERMISSION: 保存角色权限
  DOMAIN_RESOURCE_ROLE_ADD: 添加领域角色
  DOMAIN_RESOURCE_ROLE_LIST: 查找领域的角色
  DOMAIN_RESOURCE_ROLE_RENAME: 重命名领域角色
  DOMAIN_RESOURCE_ROLE_COPY: 复制指定领域角色
  DOMAIN_RESOURCE_ROLE_DELETE: 删除指定领域角色
  DOMAIN_DATA_MODEL_DEFAULT: 已知的业务处理
  DOMAIN_DATA_MODEL_ADD: 数据模型-添加
  DOMAIN_DATA_MODEL_UPDATE: 数据模型-修改
  DOMAIN_DATA_MODEL_UPDATE_CHECK: 数据模型-修改校验
  DOMAIN_DATA_MODEL_DETELE: 数据模型-删除
  DOMAIN_DATA_MODEL_DETELE_CHECK: 数据模型-删除校验
  DOMAIN_DATA_MODEL_COPY: 数据模型-复制
  DOMAIN_DATA_MODEL_IMPORT: 数据模型-导入
  DOMAIN_DATA_MODEL_EXPORT_TMP: 数据模型-导出模板
  DOMAIN_DATA_MODEL_EXPORT: 数据模型-导出
  DOMAIN_DATA_MODEL_LIST: 数据模型-列表
  DOMAIN_DATA_MODEL_DETAIL: 数据模型-根据数据模型ID查询
  DOMAIN_DATA_MODEL_LIST_BY_DOMAIN_ID: 数据模型-根据领域ID查询
  DOMAIN_DATA_MODEL_LIST_EXCLUDE: 数据模型-根据领域ID查询不包含当前数据模型ID
  DOMAIN_DATA_MODEL_VARIABLE: 数据模型-获取数据变量树
  DOMAIN_DATA_MODEL_INPUT: 数据模型-获取input变量树
  DOMAIN_DATA_MODEL_VERISION: 数据模型-获取新版本号
  DOMAIN_DATA_MODEL_DIFF: 数据模型-版本对比
  DOMAIN_DATA_MODEL_REF: 数据模型-策略引用
  DOMAIN_DATA_DICT_DEFAULT: 已知的业务处理
  DOMAIN_DATA_DICT_ADD: 字典类别-新增
  DOMAIN_DATA_DICT_LIST: 字典类别-列表
  DOMAIN_DATA_DICT_LIST_BY_NAME: 字典类别-根据中文名获取
  DOMAIN_DATA_DICT_LIST_BY_CODE: 字典类别-根据编码获取
  DOMAIN_DATA_DICT_DELETE: 字典类别-删除
  DOMAIN_DATA_DICT_DELETE_CHECK: 字典类别-删除校验
  DOMAIN_DATA_DICT_IS_USE: 字典类别-是否被使用
  DOMAIN_DATA_DICT_DETAIL_ADD: 字典项-新增修改
  DOMAIN_DATA_DICT_DETAIL_PARENT: 字典项-获取上级字典项
  DOMAIN_DATA_DICT_DETAIL_DELETE: 字典项-删除
  DOMAIN_DATA_DICT_DETAIL_IMPORT: 字典项-导入
  DOMAIN_DATA_DICT_DETAIL_EXPORT: 字典项-导出
  DOMAIN_OUTSIDE_DEFAULT: 已知的业务处理
  DOMAIN_OUTSIDE_LIST: 领域下的外部服务列表查询
  DOMAIN_OUTSIDE_REF: 领域下的外部服务列表中的引入信息
  AB_TEST_DEFAULT: 已知的业务处理
  AB_TEST_RANDOM_PAGE_LIST: 随机数定义-列表-分页
  AB_TEST_RANDOM_LIST: 随机数定义-列表-不分页
  AB_TEST_RANDOM_ADD: 随机数定义-新增
  AB_TEST_RANDOM_UPDATE: 随机数定义-修改
  AB_TEST_RANDOM_DELETE_CHECK: 随机数定义-删除校验
  AB_TEST_RANDOM_DELETE: 随机数定义-删除
  AB_TEST_GROUP_LIST: 测试分组-获取分组和区间
  AB_TEST_GROUP_SAVE: 测试分组-保存
  AB_TEST_PLAN_LIST: 测试方案-列表
  AB_TEST_PLAN_VARIABLE: 测试方案-变量树
  AB_TEST_PLAN_SAVE: 测试方案-保存
  AB_TEST_PLAN_COPY: 测试方案-复制
  AB_TEST_PLAN_UPDATE: 测试方案-修改
  AB_TEST_PLAN_UPDATE_CHECK: 测试方案-修改校验
  AB_TEST_PLAN_DELETE: 测试方案-删除
  AB_TEST_PLAN_DELETE_CHECK: 测试方案-删除校验
  AB_TEST_PLAN_DETAILS: 测试方案-方案信息
  DOMAIN_ROSTER_DEFAULT: 已知的业务处理
  DOMAIN_ROSTER_TYPE_LIST: 名单类型-列表
  DOMAIN_ROSTER_TYPE_PAGE_LIST: 名单类型-分页查询列表
  DOMAIN_ROSTER_TYPE_ADD: 名单类型-添加修改
  DOMAIN_ROSTER_TYPE_UPDATE_CHECK: 名单类型-修改校验
  DOMAIN_ROSTER_TYPE_DELETE: 名单类型-删除
  DOMAIN_ROSTER_TYPE_DELETE_CHECK: 名单类型-删除校验
  DOMAIN_ROSTER_DETAILS_ADD: 名单数据-添加修改
  DOMAIN_ROSTER_DETAILS_LIST: 名单数据-列表
  DOMAIN_ROSTER_DETAILS_IN: 名单数据-恢复入库
  DOMAIN_ROSTER_DETAILS_OUT: 名单数据-出库
  DOMAIN_ROSTER_DETAILS_IMPORT: 名单数据-导入
  DOMAIN_ROSTER_DETAILS_IMPORT_TMP: 名单数据-导入模板
  DOMAIN_ROSTER_DETAILS_EXPORT: 名单数据-导出
  DOMAIN_ROSTER_DETAILS_DELETE: 名单数据-删除
  DOMAIN_ROSTER_DETAILS_DELETE_CHECK: 名单数据-删除校验
  STRATEGY_PARAM_DEFAULT: 已知的业务处理
  STRATEGY_PARAM_ADD: 策略参数-新增修改
  STRATEGY_PARAM_LIST: 策略参数-列表-不分页
  STRATEGY_PARAM_PAGE_LIST: 策略参数-列表-分页
  STRATEGY_PARAM_UPDATE_CHECK: 策略参数-修改校验
  STRATEGY_PARAM_DELETE_CHECK: 策略参数-删除校验
  STRATEGY_PARAM_DELETE: 策略参数-删除
  STRATEGY_PARAM_SETTING: 策略参数-设置初始值
  STRATEGY_PARAM_REFRESH_CURRENT: 策略参数-刷新当前值
  STRATEGY_PARAM_REFRESH_DYNAMIC: 策略参数-刷新动态参数
  SERVICE_API_DEFAULT: 已知的业务处理
  SERVICE_API_LIST: 决策服务接口-列表-不分页
  SERVICE_API_PAGE_LIST: 决策服务接口-列表-分页
  SERVICE_API_DETAIL: 决策服务接口-获取详情
  SERVICE_API_ADD: 决策服务接口-添加修改
  SERVICE_API_DELETE: 决策服务接口-删除
  SERVICE_API_PUBLISH: 决策服务接口-发布
  SERVICE_API_STOP: 决策服务接口-停用
  SERVICE_API_CHECK: 决策服务接口-校验
  SERVICE_API_CODE: 决策服务接口-生成接口编码
  SERVICE_API_LIFECYCLE: 决策服务接口-获取生命周期
  SERVICE_API_DATA_QUERY: 决策数据查询
  SERVICE_API_DATA_REQ: 决策数据报文查询
  SERVICE_API_DATA_VAR: 决策数据变量表
  SERVICE_API_DATA_MAIN_TRACE: 查看生产, 灰度, 陪跑决策数据主流程trace
  SERVICE_API_DATA_NODE_TRACE: 查看生产, 灰度, 陪跑决策数据节点组件trace
  SERVICE_API_DATA_TRACE: 查看生产, 灰度, 陪跑决策数据trace报文
  SERVICE_API_DATA_COMMON_MAIN_TRACE: 查看公共模块决策数据主流程trace
  SERVICE_API_DATA_COMMON_NODE_TRACE: 查看公共模块决策数据节点组件trace
  SERVICE_API_DATA_COMMON_TRACE: 查看公共模块决策数据trace报文
  SERVICE_DEFAULT: 已知的业务处理
  SERVICE_LIST: 决策服务列表
  SERVICE_LIST_BY_DOMAIN: 根据多个领域查询服务列表
  SERVICE_TREE_LIST: 获取决策服务树列表
  SERVICE_ATT: 属性
  SERVICE_CHECK: 校验决策服务
  SERVICE_ADD_LIST: 新增决策服务/公共决策模块
  SERVICE_ADD: 新增单个决策服务/公共决策模块
  SERVICE_UPDATE: 修改决策服务
  SERVICE_DELETE: 删除决策服务
  SERVICE_START_STOP: 决策服务启用/停用
  SERVICE_REF_BUCKET: 决策服务-引用决策细分
  SERVICE_BUCKET_START_STOP: 决策细分启用/停用
  SERVICE_BUCKET_DELETE: 决策细分删除
  COMMON_SERVICE_DEFAULT: 已知的业务处理
  COMMON_SERVICE_DATA_MODEL_LIST: 公共决策模块-数据模型-查询
  COMMON_SERVICE_DATA_MODEL_UPDATE: 公共决策模块-数据模型-修改
  COMMON_SERVICE_DATA_MODEL_IMPORT: 公共决策模块-数据模型-导入
  COMMON_SERVICE_DATA_MODEL_EXPORT_TMP: 公共决策模块-数据模型-导出模板
  COMMON_SERVICE_DATA_MODEL_EXPORT: 公共决策模块-数据模型-导出
  COMMON_SERVICE_DATA_MODEL_VARIABLE: 公共决策模块-数据模型-获取数据变量树
  COMMON_SERVICE_DATA_MODEL_REF: 公共决策模块-数据模型-策略引用
  COMMON_SERVICE_TREE_LIST: 公共决策模块-服务树
  COMMON_SERVICE_ATTRIBUTE: 公共决策模块-属性
  COMMON_SERVICE_ADD: 公共决策模块-新增
  COMMON_SERVICE_COPY: 公共决策模块-复制
  COMMON_SERVICE_QUERY_VERSION: 公共决策模块-查询版本分配
  COMMON_SERVICE_ASSIGN_VERSION: 公共决策模块-版本分配
  COMMON_SERVICE_ADD_BUCKET: 公共决策模块-添加细分
  COMMON_SERVICE_UPDATE_BUCKET: 公共决策模块-修改细分
  COMMON_SERVICE_ADD_STRATEGY: 公共决策模块-新建策略
  COMMON_SERVICE_UPDATE_STRATEGY: 公共决策模块-修改策略
  BUCKET_SETTING_DEFAULT: 已知的业务处理
  BUCKET_SETTING_ADD: 添加细分
  BUCKET_SETTING_LIST: 细分列表
  BUCKET_DEFAULT: 已知的业务处理
  BUCKET_LIST_BY_SERVICE: 根据服务查询决策细分
  BUCKET_LIST_NO_BUILD: 查询服务未绑定的决策细分列表
  BUCKET_LIST_FLOW_CONFIG: 查询服务分流配置列表
  BUCKET_FLOW_CONFIG: 查询服务分流配置
  BUCKET_FLOW_CONFIG_UPDATE: 修改服务分流配置列表
  BUCKET_CHECK: 校验决策细分
  STRATEGY_DEFAULT: 已知的业务处理
  STRATEGY_ALL_LIST: 查询所有策略
  STRATEGY_LIST: 策略列表
  STRATEGY_AVAILAB_DEFAULT_LIST: 可用的策略复制列表(自有服务)
  STRATEGY_AVAILAB_COMMON_LIST: 可用的策略复制列表(公共决策)
  STRATEGY_ONLINE_APPLY: 获取上线申请信息
  STRATEGY_DETAIL: 获取策略详细信息
  STRATEGY_ATT: 获取策略显示信息
  STRATEGY_APPLY_RANDOM: 策略申请上线随机信息
  STRATEGY_BACK_VERSION: 获取回退版本
  STRATEGY_CHECK: 校验策略
  STRATEGY_ADD: 新建策略
  STRATEGY_COPY: 复制策略
  STRATEGY_UPDATE: 编辑策略
  STRATEGY_SUBMIT_TEST: 提交测试
  STRATEGY_UPDATE_INFO: 修改策略属性
  STRATEGY_VERIFY: 策略审核
  STRATEGY_PUBLISH: 发布上线
  STRATEGY_APPROVAL: 上线审批
  STRATEGY_ENGINE_VARS_UPDATE: 引擎变量修改
  STRATEGY_VERSION: 策略版本
  STRATEGY_PUBLISH_RECORDS: 发布记录
  STRATEGY_DEPLOY_APPLY_DATA_PREPARE: 上线申请-上线数据准备
  STRATEGY_DEPLOY_APPLY_DATA_PREPARE_STATUS: 上线申请-查询数据准备进度
  STRATEGY_DEPLOY_APPLY_CANCLE: 上线申请-取消发布
  STRATEGY_DEPLOY_APPLY_DOWN: 上线申请-下载上线包
  STRATEGY_DEPLOY_APPLY_ONLINE: 上线申请-申请上线
  STRATEGY_DEPLOY_APPLY_ONLINE_STATUS: 上线申请-查询上线申请进度
  STRATEGY_DEPLOY_APPLY_PUBLISH_STATUS: 上线申请-获取发布状态
  STRATEGY_DEPLOY_APPLY_AGAIN: 上线申请-再次发布
  STRATEGY_DEPLOY_PARSE_DOMAIN_IMPORT: 生产发布-导入领域上线包
  STRATEGY_DEPLOY_PARSE_STRATEGY_IMPORT: 生产发布-导入策略上线包
  STRATEGY_DEPLOY_PARSE_CHECK: 生产发布-上线申请校验(生产)
  STRATEGY_DEPLOY_PARSE_APPLY: 生产发布-上线申请(生产)
  STRATEGY_DEPLOY_PARSE_APPLY_STATUS: 生产发布-查询上线申请进度(生产)
  STRATEGY_DEPLOY_PARSE_APPLY_RESULT: 生产发布-查询上线申请结果(生产)
  STRATEGY_DOC_PAGE_LIST: 策略文档-分页查询列表
  STRATEGY_DOC_LIST: 策略文档-列表
  STRATEGY_DOC_DETAIL: 策略文档-详情
  STRATEGY_DOC_ADD: 策略文档-添加
  STRATEGY_DOC_UPDATE: 策略文档-修改
  STRATEGY_DOC_DELETE: 策略文档-删除
  STRATEGY_DOC_DOWN: 策略文档-下载
  STRATEGY_DOC_FILE_VIEW: 策略文档-文件预览
  STRATEGY_DOC_UPLOADFILE: 策略文档-上传文件
  STRATEGY_DEPLOY_APPLY_DATA_PREPARE_CHECK: 上线申请-校验上线申请
  STRATEGY_TEST_DEFAULT: 已知的业务处理
  STRATEGY_TEST_COMMON_RULE: 获取生成规则配置
  STRATEGY_TEST_COMMON_SAVE_RULE: 保存规则配置
  STRATEGY_TEST_COMMON_CUSTOM: 获取自定义列表
  STRATEGY_TEST_LIST: 获取数据集
  STRATEGY_TEST_DETAIL: 获取数据明细
  STRATEGY_TEST_UPDATE_DETAIL: 修改数据明细
  STRATEGY_TEST_EXPORT_DETAIL: 导出数据明细
  STRATEGY_TEST_DELETE: 删除数据集
  STRATEGY_TEST_UPDATE: 修改数据集
  STRATEGY_TEST_COPY: 复制数据集
  STRATEGY_TEST_MERGE: 合并数据集
  STRATEGY_TEST_EXECUTE: 执行测试
  STRATEGY_TEST_RESULT: 测试结果查看
  STRATEGY_TEST_EXPORT_RESULT: 导出测试结果
  STRATEGY_TEST_TRANFER: 转换为预期结果
  STRATEGY_TEST_FORM: 获取在线表单
  STRATEGY_TEST_EXPECT_VARS: 获取预期变量树
  STRATEGY_TEST_TRANFER_EXPECT: 转换预期表单数据
  STRATEGY_TEST_SAVE_FORM: 保存在线表单
  STRATEGY_TEST_RULE_OBJ: 获取规则配置数据对象
  STRATEGY_TEST_GEN_SIMPLE: 生成样例数据
  STRATEGY_TEST_SAVE_DATA: 生成并保存测试数据
  STRATEGY_TEST_SIMPLE_LIST: 样本数据分页查询
  STRATEGY_TEST_TEMPLATE: 获取模板变量
  STRATEGY_TEST_EXPORT_TEMPLATE: 导出模板
  STRATEGY_TEST_IMPORT: 导入数据
  STRATEGY_TEST_SAVE_SIMPLE: 保存Excel样本数据
  STRATEGY_TEST_PRODUCT: 获取生产数据
  STRATEGY_TEST_IMPORT_PRODUCT: 导入生产数据
  STRATEGY_TEST_MAIN_TRACE: 查看测试结果主流程Trace
  STRATEGY_TEST_NODE_TRACE: 查看测试结果节点组件Trace
  STRATEGY_TEST_TRACE_REQ: 查看测试结果Trace报文
  STRATEGY_TEST_EXECUTE_INIT: 策略测试执行初始化
  STRATEGY_DIRECTORY_DEFAULT: 已知的业务处理
  STRATEGY_DIRECTORY_ADD: 保存决策文件夹
  STRATEGY_DIRECTORY_UPDATE: 修改决策文件夹
  STRATEGY_DIRECTORY_TREE: 决策文件夹树
  STRATEGY_DIRECTORY_LIST: 决策文件夹列表
  STRATEGY_DIRECTORY_DELETE: 删除决策文件夹
  STRATEGY_DIRECTORY_CHECK: 验证文件夹是否存在
  STRATEGY_DIRECTORY_ATT: 决策文件夹属性
  STRATEGY_COMPONENT_DEFAULT: 已知的业务处理
  STRATEGY_COMPONENT_ADD: 保存决策组件
  STRATEGY_COMPONENT_UP_PMML: 上传PMML文件
  STRATEGY_COMPONENT_UP_PKL: 上传PKL文件
  STRATEGY_COMPONENT_UP_PKL_PREDICT: 上传PKL预测脚本文件
  STRATEGY_COMPONENT_UP_FEATURE: 上传PKL特征文件
  STRATEGY_COMPONENT_UP_MODEL: 上传Model文件
  STRATEGY_COMPONENT_UP_MODEL_PREDICT: 上传Model预测脚本文件
  STRATEGY_COMPONENT_UP_MODEL_FEATURE: 上传Model特征文件
  STRATEGY_COMPONENT_MODEL_VERSION: 获取模型算法库与版本号
  STRATEGY_COMPONENT_HOLD: 手动/自动保存决策组件
  STRATEGY_COMPONENT_TEMP_LIST: 分页查询临时表
  STRATEGY_COMPONENT_COPY: 复制决策组件
  STRATEGY_COMPONENT_LIST: 决策组件列表
  STRATEGY_COMPONENT_TREE: 决策组件树列表
  STRATEGY_COMPONENT_MAIN: 获取主流程
  STRATEGY_COMPONENT_MAIN_BY_ID: 按策略ID获取主流程
  STRATEGY_COMPONENT_UPDATE: 修改组件状态
  STRATEGY_COMPONENT_CHECK_OUT: 检出组件
  STRATEGY_COMPONENT_CANCLE_CHECK: 取消检出组件
  STRATEGY_COMPONENT_CHECK_IN: 检入组件
  STRATEGY_COMPONENT_FORCE_CHECK: 强制检出组件
  STRATEGY_COMPONENT_DETAIL: 获取组件详情
  STRATEGY_COMPONENT_PROPERT: 获取组件属性
  STRATEGY_COMPONENT_SIMPLE_LIST: 决策组件简单列表
  STRATEGY_COMPONENT_CHECK: 组件验证
  STRATEGY_COMPONENT_RECOREY: 恢复版本
  STRATEGY_COMPONENT_COMPAER: 内容是否更改
  STRATEGY_COMPONENT_DOWN_PMML: 下载PMML文件
  STRATEGY_COMPONENT_DOWN_MODEL: 下载模型文件
  STRATEGY_COMPONENT_SAVE_CACHE: 临时储存参数/本地变量
  STRATEGY_COMPONENT_EXPORT: 导出组件模板
  STRATEGY_COMPONENT_IMPORT: 导入组件内容
  STRATEGY_COMPONENT_EXPORT_PARAM: 导出参数表内容
  STRATEGY_COMPONENT_FUNCTION: 获取函数列表
  STRATEGY_COMPONENT_CREATE_LIST: 获取创建人列表
  STRATEGY_COMPONENT_UPDATE_DIR: 修改组件文件夹
  STRATEGY_COMPONENT_MAIN_PROPERT: 获取主流程属性
  COMPONENT_TEST_DEFAULT: 已知的业务处理
  COMPONENT_TEST_LIST: 获取数据集
  COMPONENT_TEST_DETAIL: 获取数据明细
  COMPONENT_TEST_UPDATE_DETAIL: 修改数据明细
  COMPONENT_TEST_EXPORT_DETAIL: 导出数据明细
  COMPONENT_TEST_DELETE: 删除数据集
  COMPONENT_TEST_UPDATE: 修改数据集
  COMPONENT_TEST_COPY: 复制数据集
  COMPONENT_TEST_MERGE: 合并数据集
  COMPONENT_TEST_EXECUTE: 执行测试
  COMPONENT_TEST_RESULT: 测试结果查看
  COMPONENT_TEST_EXPORT_RESULT: 导出测试结果
  COMPONENT_TEST_TRANFER: 转换为预期结果
  COMPONENT_TEST_FORM: 获取在线表单
  COMPONENT_TEST_EXPECT_VARS: 获取预期变量树
  COMPONENT_TEST_TRANFER_EXPECT: 转换预期表单数据
  COMPONENT_TEST_SAVE_FORM: 保存在线表单
  COMPONENT_TEST_RULE_OBJ: 获取规则配置数据对象
  COMPONENT_TEST_GEN_SIMPLE: 生成样例数据
  COMPONENT_TEST_SAVE_DATA: 生成并保存测试数据
  COMPONENT_TEST_SIMPLE_LIST: 样本数据分页查询
  COMPONENT_TEST_TEMPLATE: 获取模板变量
  COMPONENT_TEST_EXPORT_TEMPLATE: 导出模板
  COMPONENT_TEST_IMPORT: 导入数据
  COMPONENT_TEST_SAVE_SIMPLE: 保存Excel样本数据
  COMPONENT_TEST_CHECK: 验证组件测试
  COMPONENT_TEST_FLOW_TRACE: 查看测试结果决策流Trace
  COMPONENT_TEST_NODE_TRACE: 查看测试结果节点组件Trace
  COMPONENT_TEST_TRACE: 查看测试结果组件Trace
  COMPONENT_TEST_TRACE_REQ: 查看测试结果Trace报文
  COMPONENT_TEST_EXECUTE_INIT: 组件测试执行初始化
  STRATEGY_DATA_VAR_DEFAULT: 已知的业务处理
  STRATEGY_DATA_VAR_IMPORT: 导入变量
  STRATEGY_DATA_VAR_EXPORT_TMP: 导出变量模板
  STRATEGY_DATA_VAR_EXPORT: 导出变量Excel文件
  STRATEGY_DATA_VAR_LIST: 获取输入输出引擎变量列表
  STRATEGY_DATA_VAR_VARIABLE: 获取数据变量
  STRATEGY_DATA_VAR_STRATEGY: 获取策略数据与变量
  STRATEGY_DATA_VAR_ENGINE: 获取策略的引擎变量
  STRATEGY_DATA_VAR_TYPE: 获取数据变量类型集合
  STRATEGY_DATA_VAR_REF: 查询策略下的数据模型变量和引擎变量引用
  STRATEGY_DATA_DICT_DEFAULT: 已知的业务处理
  STRATEGY_DATA_DICT_ADD: 字典类别-新增
  STRATEGY_DATA_DICT_LIST: 字典类别-列表
  STRATEGY_DATA_DICT_LIST_BY_NAME: 字典类别-根据中文名获取
  STRATEGY_DATA_DICT_LIST_BY_CODE: 字典类别-根据编码获取
  STRATEGY_DATA_DICT_DELETE: 字典类别-删除
  STRATEGY_DATA_DICT_DELETE_CHECK: 字典类别-删除校验
  STRATEGY_DATA_DICT_IS_USE: 字典类别-是否被使用
  STRATEGY_DATA_DICT_DETAIL_ADD: 字典项-新增修改
  STRATEGY_DATA_DICT_DETAIL_PARENT: 字典项-获取上级字典项
  STRATEGY_DATA_DICT_DETAIL_DELETE: 字典项-删除
  STRATEGY_DATA_DICT_DETAIL_IMPORT: 字典项-导入
  STRATEGY_DATA_DICT_DETAIL_EXPORT: 字典项-导出
  STRATEGY_OUTSIDE_DEFAULT: 已知的业务处理
  STRATEGY_OUTSIDE_REF: 服务引入保存/修改
  STRATEGY_OUTSIDE_UPDATE: 修改和删除编辑引入服务
  STRATEGY_OUTSIDE_DELETE: 删除服务引入
  STRATEGY_OUTSIDE_CHECK: 校验服务引入
  STRATEGY_OUTSIDE_LIST: 服务引入列表
  STRATEGY_OUTSIDE_ALL_LIST: 策略下的所有外部服务引入信息
  STRATEGY_OUTSIDE_DETAIL: 策略下的外部服务信息详情
  STRATEGY_COMMON_DEFAULT: 已知的业务处理
  STRATEGY_COMMON_PAGE_LIST: 公共决策模块列表
  STRATEGY_COMMON_REF: 服务引入保存/修改(服务引入)
  STRATEGY_COMMON_DELETE: 删除服务引入
  STRATEGY_COMMON_REF_CHECK: 校验服务引入
  STRATEGY_COMMON_DETAILS: 获取公共服务详情
  STRATEGY_COMMON_REF_LIST: 决策模块引用列表
  STRATEGY_COMMON_BUCKET: 查询细分列表
  STRATEGY_COMMON_LIST: 公共服务简单列表
  STRATEGY_COMMON_INPUT_VAR: 获取服务Input树列表
  PERMISSION_DEFAULT: 已知的业务处理
  PERMISSION_LIST_BY_DOMAIN_ID: 根据领域ID获取权限
  PERMISSION_LIST_BY_EDIT: 获取领域编辑权限
  PERMISSION_USER_LIST: 获取用户所有领域权限
  OUTSIDE_DEFAULT: 已知的业务处理
  OUTSIDE_COM_LIST: 外数列表
  OUTSIDE_LIST_BY_NAME: 根据服务名称查询外部服务列表
  OUTSIDE_LIST: 外部服务列表
  OUTSIDE_ADD: 外部服务添加或修改
  OUTSIDE_DETAIL: 外部服务详情
  OUTSIDE_OAUTH: 查询外部服务授权信息
  OUTSIDE_OAUTH_VAR_SPACE: 查询可以被授权的变量空间
  OUTSIDE_ADD_DOMAIN: 新增外部服务-领域服务授权关系
  OUTSIDE_ADD_VAR: 新增外部服务-变量空间服务授权关系
  OUTSIDE_DELETE_OAUTH_DOMAIN: 删除外部服务领域授权记录
  OUTSIDE_DELETE_OAUTH_VAR: 删除外部服务变量空间授权记录
  OUTSIDE_DELETE_CHECK: 删除外部服务校验
  OUTSIDE_DELETE: 删除外部服务
  OUTSIDE_UPDATE_STATUS: 更新外部服务状态
  OUTSIDE_STOP: 停用校验
  OUTSIDE_COPY: 复制外部服务
  OUTSIDE_TEST: 服务测试
  OUTSIDE_BASE_INFO: 获取外部服务基本信息
  OUTSIDE_CHECK: 校验外部服务
  OUTSIDE_REF_DOMAIN: 获取外部服务的领域引用情况
  OUTSIDE_LIFE_CYCLE: 查询外部服务生命周期
  OUTSIDE_DOMAIN_BY_ID: 根据外部服务id查询外部服务领域表
  OUTSIDE_VAR_BY_ID: 根据外部服务id查询外部服务变量
  OUTSIDE_DEPLOY_DEFAULT: 已知的业务处理
  OUTSIDE_DEPLOY_CREATED: 创建发布包
  OUTSIDE_DEPLOY_FAIL_CREATED: 基于失败的发布创建发布包
  OUTSIDE_DEPLOY_CANCLE: 取消一键发布
  OUTSIDE_DEPLOY_QUEREY_PROCESS: 查询发布进度信息
  OUTSIDE_DEPLOY_IMPORT: 数据导入
  OUTSIDE_DEPLOY_CHECK: 判断发布包是否存在
  OUTSIDE_DEPLOY_DOWN: 下载发布包
  OUTSIDE_DEPLOY_LIST: 分页查询发布记录
  OUTSIDE_DEPLOY_IMPORT_ZIP: 接收上线包
  OUTSIDE_DEPLOY_QUERY_STATUS: 查询无结果发布记录的状态
  OUTSIDE_PARAM_DEFAULT: 已知的业务处理
  OUTSIDE_PARAM_SAVE: 请求参数配置添加或修改
  OUTSIDE_PARAM_DETAIL: 请求参数配置详情
  OUTSIDE_PARAM_SYS_METHOD: 查询系统方法
  OUTSIDE_PARAM_SYS_PARAM: 查询系统参数
  OUTSIDE_PARAM_RESP_SAVE: 响应数据添加或修改
  OUTSIDE_PARAM_RESP_CHECK: 响应参数校验
  OUTSIDE_PARAM_RESP_CHECK_REF: 响应参数校验(校验是否引用)
  OUTSIDE_PARAM_RESP_ROOT: 获取响应数据结构根数据
  OUTSIDE_PARAM_RESP_DETAIL: 外部服务响应数据详情
  OUTSIDE_PARAM_RESP_IMPORT: 外部服务变量导入
  OUTSIDE_PARAM_RESP_EXPORT: 导出服务响应数据Excel文件
  OUTSIDE_RESPONSE_DEFAULT: 已知的业务处理
  OUTSIDE_RESPONSE_ADD: 响应码添加或修改
  OUTSIDE_RESPONSE_DETAIL: 响应码详情
  OUTSIDE_RESPONSE_DATA: 左值数据
  OUTSIDE_MOCK_DEFAULT: 已知的业务处理
  OUTSIDE_MOCK_SAVE: 添加或修改
  OUTSIDE_MOCK_SAVE_CHECK: 添加或修改操作校验
  OUTSIDE_MOCK_COPY: 复制外部服务mock配置
  OUTSIDE_MOCK_LIST: 外部服务mock配置列表
  OUTSIDE_MOCK_LIST_BY_ID: 根据服务id查询已开启的外部服务mock配置列表
  OUTSIDE_MOCK_DELETE: 外部服务mock配置删除
  OUTSIDE_MOCK_DELETE_CHECK: 外部服务mock配置删除校验
  OUTSIDE_MOCK_DETAIL_SAVE: 新增或修改外部服务mock明细
  OUTSIDE_MOCK_DETAIL_LIST: 外部服务mock明细列表
  OUTSIDE_MOCK_DETAIL_DELETE: 删除外部服务mock明细
  OUTSIDE_MOCK_DETAIL_IMPORT: 外部服务mock明细数据导入
  OUTSIDE_MOCK_DETAIL_EXPORT: 外部服务mock明细下载模板
  OUTSIDE_CALL_RECORD_DEFAULT: 已知的业务处理
  OUTSIDE_CALL_RECORD_LIST: 调用记录表列表
  OUTSIDE_CALL_RECORD_DETAIL: 查询调用记录报文详情
  OUTSIDE_CALL_RECORD_DOMAIN: 调用记录中的所有领域名称
  VARIABLE_SPACE_DEFAULT: 已知的业务处理
  VARIABLE_SPACE_LIST: 获取空间列表
  VARIABLE_SPACE_LIST_BY_TYPE: 根据类型获取空间列表
  VARIABLE_SPACE_DETAIL: 查看变量空间属性
  VARIABLE_SPACE_ATTRIBUTE: 资源概览
  VARIABLE_SPACE_ADD: 添加空间
  VARIABLE_SPACE_UPDATE: 修改空间
  VARIABLE_SPACE_DELETE_CHECK: 删除空间校验
  VARIABLE_SPACE_DELETE: 删除空间
  VARIABLE_SPACE_CONFIG_DEFAULT: 已知的业务处理
  VARIABLE_SPACE_CONFIG_CATE_TREE: 变量分类-分类数
  VARIABLE_SPACE_CONFIG_CATE_LIST: 变量分类-列表
  VARIABLE_SPACE_CONFIG_CATE_ADD: 变量分类-添加
  VARIABLE_SPACE_CONFIG_CATE_DEL_CHECK: 变量分类-删除校验
  VARIABLE_SPACE_CONFIG_DEL: 变量分类-删除
  VARIABLE_SPACE_CONFIG_TAG_TREE: 变量标签-标签树
  VARIABLE_SPACE_CONFIG_TAG_LIST: 变量标签-列表
  VARIABLE_SPACE_CONFIG_TAG_GROUP_SAVE: 变量标签-添加或修改标签组
  VARIABLE_SPACE_CONFIG_TAG_GROUP_DEL: 变量标签-删除标签组
  VARIABLE_SPACE_CONFIG_TAG_ADD: 变量标签-添加标签
  VARIABLE_SPACE_CONFIG_TAG_DEL: 变量标签-删除标签
  VARIABLE_SPACE_CONFIG_DEFAULT_LIST: 缺失值-列表
  VARIABLE_SPACE_CONFIG_DEFAULT_EDIT: 缺失值-编辑
  VARIABLE_SPACE_CONFIG_EX_LIST: 异常值-列表
  VARIABLE_SPACE_CONFIG_EX_SAVE: 异常值-保存
  VARIABLE_SPACE_CONFIG_EX_DEL_CHECK: 异常值-删除校验
  VARIABLE_SPACE_CONFIG_EX_DEL: 异常值-删除
  VARIABLE_COMMON_DEFAULT: 已知的业务处理
  VARIABLE_COMMON_DATA_MODEL_TREE: 查询变量空间数据模型树形结构统一接口
  VARIABLE_COMMON_VAR_TREE: 变量匹配树接口
  VARIABLE_COMMON_TEMPLATE_STATIC: 变量加工模板配置+所有dataProvider数据
  VARIABLE_COMMON_TEMPLATE_VAR_BY_PATH: dataProvider追加数据接口
  VARIABLE_COMMON_TEMPLATE_PROVIDEER_ADD: 只有this_dataProvider追加数据接口
  VARIABLE_COMMON_TEMPLATE_PROVIDER_THIS: 根据变量路径（对象）获取一级基本属性
  VARIABLE_COMMON_TEMPLATE_PROVIDER_BY_PATH: 根据变量路径（对象）动态获取相对象
  VARIABLE_COMMON_TEMPLATE_BUCKET: 根据变量路径（对象）获取相对象数组
  VARIABLE_COMMON_TEMPLATE_OUTSIDE: 根据变量路径（对象）一级属性对比
  VARIABLE_COMMON_TEMPLATE_PARENT_ATT: 获取数据变量类型集合
  VARIABLE_COMMON_TEMPLATE_SAME_TYPE_OBJ: 获取数据provider集合
  VARIABLE_COMMON_TEMPLATE_DICT_TYPE_OBJ: 获取字典数据provider集合
  VARIABLE_ADMIN_DEFAULT: 已知的业务处理
  VARIABLE_ADMIN_LIST: 变量列表
  VARIABLE_ADMIN_DETAILS: 变量详情
  VARIABLE_ADMIN_PROPERTIES: 变量属性
  VARIABLE_ADMIN_SAVE: 保存变量
  VARIABLE_ADMIN_COPY_UP_DOWN: 复制上架或下架的变量
  VARIABLE_ADMIN_COPY: 复制变量
  VARIABLE_ADMIN_CHECK: 验证变量
  VARIABLE_ADMIN_UP: 上架
  VARIABLE_ADMIN_UPDATE: 修改状态
  VARIABLE_ADMIN_COMPARE_CONTENT: 内容比较
  VARIABLE_ADMIN_CACHE: 临时缓存内容
  VARIABLE_ADMIN_RESTORE: 恢复版本
  VARIABLE_ADMIN_DELETE_CHECK: 删除变量校验
  VARIABLE_ADMIN_DELETE: 删除变量
  VARIABLE_ADMIN_CREATE_CATEGARY: 创建变量分类
  VARIABLE_ADMIN_QUERY_CATEGARY: 查询变量分类
  VARIABLE_ADMIN_APPROVE: 审核通过或拒绝
  VARIABLE_ADMIN_COMPARE: 变量对比
  VARIABLE_ADMIN_COMPARE_VERSION: 变量对比信息
  VARIABLE_ADMIN_VERSION_LIST: 变量版本信息列表
  VARIABLE_FUNCTION_DEFAULT: 已知的业务处理
  VARIABLE_FUNCTION_LIST: 公共函数列表
  VARIABLE_FUNCTION_USE_VAR: 使用变量列表
  VARIABLE_FUNCTION_DETAILS: 公共函数详情
  VARIABLE_FUNCTION_PROPERTIES: 公共函数属性
  VARIABLE_FUNCTION_USE: 公共函数使用列表
  VARIABLE_FUNCTION_SAVE: 保存公共函数
  VARIABLE_FUNCTION_COPY: 复制公共函数
  VARIABLE_FUNCTION_VAILD: 验证公共函数
  VARIABLE_FUNCTION_UP: 上架
  VARIABLE_FUNCTION_UPDATE: 修改状态
  VARIABLE_FUNCTION_COMPARE: 内容比较
  VARIABLE_FUNCTION_CACHE: 临时缓存内容
  VARIABLE_FUNCTION_RESTORE: 恢复版本
  VARIABLE_FUNCTION_CHECK: 公共函数校验
  VARIABLE_FUNCTION_DELETE: 删除公共函数
  VARIABLE_INTERNAL_DATA_DEFAULT: 已知的业务处理
  VARIABLE_INTERNAL_DATA_REF_LIST: 内部数据引入-列表
  VARIABLE_INTERNAL_DATA_REF_DETAIL: 内部数据引入-详情
  VARIABLE_INTERNAL_DATA_REF_CREATE: 内部数据引入-创建
  VARIABLE_INTERNAL_DATA_REF_CREATE_CHECK: 内部数据引入-创建校验
  VARIABLE_INTERNAL_DATA_REF_SAVE: 内部数据引入-保存
  VARIABLE_INTERNAL_DATA_REF_SAVE_CHECK: 内部数据引入-保存校验
  VARIABLE_INTERNAL_DATA_REF_TABLES: 内部数据引入-获取所有表结构
  VARIABLE_INTERNAL_DATA_REF_VIEW: 内部数据引入-数据预览
  VARIABLE_INTERNAL_DATA_REF_DELETE_CHECK: 内部数据引入-验证删除
  VARIABLE_INTERNAL_DATA_REF_DELETE: 内部数据引入-删除
  VARIABLE_INTERNAL_DATA_TABLE_DELETE: 内部数据表管理-删除数据表
  VARIABLE_INTERNAL_DATA_TABLE_CREATE: 内部数据表管理-创建数据表
  VARIABLE_INTERNAL_DATA_TABLE_QUERY: 内部数据表管理-查询数据表
  VARIABLE_INTERNAL_DATA_TABLE_DATA: 内部数据表管理-查询数据表结构
  VARIABLE_INTERNAL_DATA_TABLE_UPDATE: 内部数据表管理-更新数据表信息
  VARIABLE_INTERNAL_DATA_LIST: 内部数据管理-查询表数据
  VARIABLE_INTERNAL_DATA_ADD: 内部数据管理-添加数据
  VARIABLE_INTERNAL_DATA_EDIT: 内部数据管理-修改数据
  VARIABLE_INTERNAL_DATA_EXPORT: 内部数据管理-导出数据
  VARIABLE_INTERNAL_DATA_EXPORT_ALL: 内部数据管理-导出所有数据
  VARIABLE_INTERNAL_DATA_IMPORT: 内部数据管理-导入数据
  VARIABLE_INTERNAL_DATA_BATCH_DELETE: 内部数据管理-批量删除数据
  VARIABLE_OUTSIDE_REF_DEFAULT: 已知的业务处理
  VARIABLE_OUTSIDE_REF_LIST: 分页获取外部服务列表
  VARIABLE_OUTSIDE_REF_DETAIL: 获取外部服务引入详情
  VARIABLE_OUTSIDE_REF_CANCE: 取消外部服务引入
  VARIABLE_OUTSIDE_REF_REQUEST: 查询变量引入的外部服务变量传入请求参数
  VARIABLE_OUTSIDE_REF_OUTSIDE: 引入外部服务
  VARIABLE_OUTSIDE_REF_USE_ROOT: 使用外部根对象
  VARIABLE_SEERVICE_DEFAULT: 已知的业务处理
  VARIABLE_SEERVICE_TREE: 变量服务和变量清单查询
  VARIABLE_SEERVICE_DETAIL: 变量服务详情查询
  VARIABLE_SEERVICE_ADD: 添加变量服务
  VARIABLE_SEERVICE_UPDATE: 编辑变量服务
  VARIABLE_SEERVICE_OAUTH: 服务授权查询
  VARIABLE_SEERVICE_OAUTH_DOMAIN: 查询可以被授权的领域
  VARIABLE_SEERVICE_SAVE_OAUTH: 保存服务授权
  VARIABLE_SEERVICE_REMOVE_OAUTH: 移除服务授权
  VARIABLE_SEERVICE_DELETE_CHECK: 删除变量服务校验
  VARIABLE_SEERVICE_DELETE: 删除变量服务
  VARIABLE_SEERVICE_ATT: 获取变量服务属性信息
  VARIABLE_SEERVICE_API: 获取接口文档
  VARIABLE_SEERVICE_LIST: 获取空间下简单格式服务列表
  VARIABLE_SEERVICE_INTERFACE_LIST: 查询服务下所有未删除变量清单
  VARIABLE_SEERVICE_INTERFACE_ADD: 添加变量清单
  VARIABLE_SEERVICE_INTERFACE_DELETE: 删除变量清单
  VARIABLE_SEERVICE_INTERFACE_CONFIG: 获取变量清单配置
  VARIABLE_SEERVICE_INTERFACE_SAVE: 保存变量清单配置
  VARIABLE_SEERVICE_INTERFACE_MAX_VARS: 查询所有变量最大已上架版本记录
  VARIABLE_SEERVICE_INTERFACE_VARS: 查询指定变量所有已上架版本
  VARIABLE_SEERVICE_INTERFACE_REF_OUTSIDE: 查询变量空间引入的外部服务接收对象
  VARIABLE_SEERVICE_INTERFACE_CHECK: 校验内部/外部服务响应参数
  VARIABLE_SEERVICE_INTERFACE_MAPPING: 获取外部服务入参映射对象
  VARIABLE_SEERVICE_INTERFACE_BUIDING: 获取可用于变量清单调用流水号绑定的数据结构
  VARIABLE_SEERVICE_REF_INTERFACE_CHECK: 校验清单状态更新
  VARIABLE_SEERVICE_INTERFACE_UPDATE: 更新变量清单状态
  VARIABLE_SEERVICE_INTERFACE_VERIFY: 提交审核
  VARIABLE_SEERVICE_INTERFACE_ATT: 获取变量清单属性信息
  VARIABLE_SEERVICE_DEPLOY_APPLY_DATA_PREPARE: 上线申请-上线数据准备
  VARIABLE_SEERVICE_DEPLOY_APPLY_DATA_PREPARE_STATUS: 上线申请-查询数据准备进度
  VARIABLE_SEERVICE_DEPLOY_APPLY_CANCLE: 上线申请-取消发布
  VARIABLE_SEERVICE_DEPLOY_APPLY_DOWN: 上线申请-下载上线包
  VARIABLE_SEERVICE_DEPLOY_APPLY_ONLINE: 上线申请-申请上线
  VARIABLE_SEERVICE_DEPLOY_APPLY_ONLINE_STATUS: 上线申请-查询上线申请进度
  VARIABLE_SEERVICE_DEPLOY_APPLY_PUBLISH_STATUS: 上线申请-获取发布状态
  VARIABLE_SEERVICE_DEPLOY_APPLY_AGAIN: 上线申请-再次发布
  VARIABLE_SEERVICE_DEPLOY_PARSE_DOMAIN_IMPORT: 生产发布-导入领域上线包
  VARIABLE_SEERVICE_DEPLOY_PARSE_VARIABLE_SEERVICE_IMPORT: 生产发布-导入策略上线包
  VARIABLE_SEERVICE_DEPLOY_PARSE_CHECK: 生产发布-上线申请校验(生产)
  VARIABLE_SEERVICE_DEPLOY_PARSE_APPLY: 生产发布-上线申请(生产)
  VARIABLE_SEERVICE_DEPLOY_PARSE_APPLY_STATUS: 生产发布-查询上线申请进度(生产)
  VARIABLE_SEERVICE_DEPLOY_PARSE_APPLY_RESULT: 生产发布-查询上线申请结果(生产)
  VARIABLE_SEERVICE_DOC_PAGE_LIST: 策略文档-分页查询列表
  VARIABLE_SEERVICE_DOC_LIST: 策略文档-列表
  VARIABLE_SEERVICE_DOC_DETAIL: 策略文档-详情
  VARIABLE_SEERVICE_DOC_ADD: 策略文档-添加
  VARIABLE_SEERVICE_DOC_UPDATE: 策略文档-修改
  VARIABLE_SEERVICE_DOC_DELETE: 策略文档-删除
  VARIABLE_SEERVICE_DOC_DOWN: 策略文档-下载
  VARIABLE_SEERVICE_DOC_FILE_VIEW: 策略文档-文件预览
  VARIABLE_SEERVICE_DOC_UPLOADFILE: 策略文档-上传文件
  VARIABLE_SEERVICE_DEPLOY_APPLY_DATA_PREPARE_CHECK: 上线申请-校验上线申请
CacheEventEnum:
  STRATEGY_COMMON_SAVE_COMMON_SERVICE_REF: 引入策略与公共决策模块
  DOMAIN_COMMON_SERVICE_VERSION: 公共决策模块分配版本
  STRATEGY_COMMON_DELETE_COMMON_SERVICE_REF: 删除策略与公共决策模块
  ABTEST_GROUP_UPDATE_ABTEST_GROUP: 修改测试分组和区间信息
  ABTEST_PLAN_UPDATE_ALL_ABTEST_GROUP: 修改方案信息含有分组和区间信息
  ABTEST_PLAN_UPDATE_ABTEST_GROUP: 修改方案信息
  ABTEST_PLAN_DELETE_ABTEST_GROUP: 删除方案信息
  ABTEST_RANDOM_DEFINITION_UPDATE: 更新随机数
  ABTEST_RANDOM_DEFINITION_DELETE: 删除随机数
  OUTSIDE_SERVICE_UPDATE: 更新外部服务
  OUTSIDE_SERVICE_ALL_UPDATE: 外部服务以及外部服务的附属信息更新
  OUTSIDE_SERVICE_STOP_OR_START_UPDATE: 停用启用外部服务
  OUTSIDE_SERVICE_DELETE: 删除外部服务
  OUTSIDE_SERVICE_REQ_UPDATE: 外部服务请求参数配置修改
  OUTSIDE_SERVICE_RESP_UPDATE: 外部服务响应数据修改
  OUTSIDE_SERVICE_RESP_CODE_UPDATE: 外部服务响应码数据修改
  SYS_PARAM_UPDATE: 修改系统参数
  SYS_PARAM_DELETE: 删除系统参数
  DOMAIN_DELETE_DOMAIN: 领域删除
  DOMAIN_UPDATE_DOMAIN: 领域修改
  DS_SERVICE_UPDATE_DSSERVICE: 服务修改
  DS_SERVICE_DELETE_DSSERVICE: 服务删除
  DS_SERVICE_UPDATE_STATE: 服务状态修改
  DS_SERVICE_INTERFACE_UPDATE: 服务接口修改
  DS_SERVICE_INTERFACE_DELETE: 服务接口删除
  DS_SERVICE_INTERFACE_START: 服务接口发布
  DS_SERVICE_INTERFACE_STOP: 服务接口停用
  DOMAIN_BUCKET_BUCKET_USER: 细分引用
  DOMAIN_BUCKET_UPDATE_BUCKET_USE_STATE: 细分停用/启用
  DOMAIN_BUCKET_USE_DELETE: 细分删除
  DOMAIN_BUCKET_UPDATE_BUCKET_ROUTE_LIST: 细分分流
  DOMAIN_BUCKET_UPDATE: 细分修改
  DS_STRATEGY_UPDATE_STATUS: 策略状态修改
  DS_STRATEGY_UPDATE: 策略信息修改
  DS_STRATEGY_PARAM_UPDATE: 策略参数修改
  DS_STRATEGY_PARAM_DELETE: 策略参数删除
  DS_STRATEGY_PARAM_UPDATE_INIT: 策略参数修改初始值
  DS_STRATEGY_PARAM_UPDATE_CURRENT: 策略参数修改当前值
  DS_STRATEGY_PARAM_REFRESH_CURRENT: 策略参数自动刷新当前值
  DS_STRATEGY_PARAM_SERVICE_UPDATE: 策略参数SERVICE更新
  DOMAIN_ROSTER_DETAIL_INSERT: 名单集新增
  DOMAIN_ROSTER_DETAIL_OUTTREAS: 名单集出库
  DOMAIN_ROSTER_DETAIL_DELETE: 名单集删除
  DOMAIN_ROSTER_DETAIL_AUTOWIRE: 名单集自动出库
  DOMAIN_ROSTER_DETAIL_IMPORT_SAVE: 名单文件导入
  DOMAIN_ROSTER_DETAIL_SERVICE_SAVE: 名单service新增
  STATISTICAL_SAVECONFIG: 保存统计配置

CacheServerEnum:
  ADMIN: admin服务
  REST: rest服务
  SERVICE: service服务
CodeBaseResourceTypeEnum:
  STRATEGY: 领域策略
  VARIABLE: 空间变量
CompileExceptionTypeEnum:
  FUNCTION_RET_VALUE_NOT_ASSIGN: 函数返回值中使用的本地变量【{0}】未赋值
ComponentCheckStateEnum:
  CHECK_OUT: 检出
  CHECK_IN: 检入
ComponentRefChangeStateEnum:
  NEW: 新建
  NO_CHANGE: 继承(无变更)
  CHANGE: 继承(有变更)
ComponentStateEnum:
  RECYCLED: 已回收
  USABLE: 可用
DataModelRefTypeEnum:
  DIRECT: 直接引用
  PARAM: 参数引用
  LOCAL: 本地变量引用
DataTypeEnum:
  INPUT_TYPE.message: 输入数据
  OUTPUT_TYPE.message: 输出数据
  ENGINE_TYPE.message: 引擎变量
  PRODUCT: 生产数据 desc
  PERFORMANCE: 表现数据
DecisionFlowNodeTypeEnum:
  TASK: 任务节点
  SPLIT: 条件分支节点
  PARALLEL: 并行分支节点
  ABTEST: AB 测试分支节点
  SERVICE: 外部服务节点
  BLAZE: Blaze决策模块节点
  VARIABLE: 变量服务节点
  LOOP: 循环节点
  FOLDER: 决策包节点
  COMMON: 公共决策模块节点
DefaultFlagEnum:
  DEFAULT: 默认
  NOT_DEFAULT: 非默认
DeployApplyStatusEnum:
  NOT_START: 未开始
  PROCESSING: 进行中
  SUCCESS: 成功
  FAIL: 失败
  WARN: 预警
DeployApplyTypeEnum:
  DOMAIN.label: 领域基本信息
  DOMAIN_DATA_MODEL.label: 数据模型
  DS_SERVICE.label: 决策服务
  COMMON_SERVICE.label: 公共决策模块
  BLAZE_SERVICE.label: blaze决策模块
  ABTEST.label: AB测试方案
  STRATEGY_PARAM.label: 策略参数
  DOMAIN_ROSTER.label: 名单信息
  DS_SERVICE_INTERFACE.label: 决策服务接口信息
  DATA_FILE.label: 生成文件
  DATA_SHARE_DIRECTORY_FILE.label: 文件保存成功,保存路径
  DOMAIN.desc: 导出成功
  DOMAIN_DATA_MODEL.desc: '{0}个数据模型'
  DS_SERVICE.desc: '{0}个决策服务（包括{1}个细分、{2}个策略、{3}个决策组件）'
  COMMON_SERVICE.desc: '{0}个公共决策模块（包括{1}个细分、{2}个策略、{3}个决策组件）'
  BLAZE_SERVICE.desc: '{0}个blaze决策模块（包括{1}个策略）'
  ABTEST.desc: '{0}个AB测试方案、{1}个随机数定义'
  STRATEGY_PARAM.desc: '{0}个静态参数、{1}个动态参数'
  DOMAIN_ROSTER.desc: '{0}个名单类型定义'
  DS_SERVICE_INTERFACE.desc: '{0}个决策服务接口'
  DATA_FILE.desc: 成功
  DATA_SHARE_DIRECTORY_FILE.desc: 成功
DeployParseTypeEnum:
  FILE_SYNC.label: 上线文件同步
  FILE_CHECK.label: 文件验证
  FILE_SYNC.desc: 成功
  FILE_CHECK.desc: 通过
DeployStateEnum:
  EXECUTING: 发布中
  SUCCESS: 发布成功
  FAIL: 发布失败
  CANCEL: 已取消
DeployTypeEnum:
  DEPLOY: 一键发布
  EXPORT: 导出发布
  MIGRATION: 全量发布
  IMPORTER: 导入记录
  SHAREDIRECTORY: 共享目录发布
DictStateFrontAbtestPlanTypeEnum:
  EDIT_DELETE_OPERATE: 编辑删除操作
DictStateFrontDataModelTypeEnum:
  EDIT_LOOK_UP_OPERATE: 编辑查看操作
  LOOK_UP_OPERATE: 查看操作
DictStateFrontTypeEnum:
  DATA_MODEL: 数据模型
  DS_SERVICE: 决策服务
  BUCKET: 决策细分
  STRATEGY: 策略
  COMMON_SERVICE: 公共决策模块
  COMMON_BUCKET: 公共决策模块-细分
  COMMON_STRATEGY: 公共决策模块-策略
  DS_SERVICE_INTERFACE: 决策服务接口
  ABTEST_PLAN: ABtest测试方案
  ABTEST_RANDOM_DEFINITION: ABtest随机数定义
  DS_STRATEGY_REF_SERVICE: 策略中的外部/变量服务列表
  OUTSIDE_SERVICE_REF_OBJECT: 外部服务引入对象
  DOMAIN_DICT: 字典类型
  DOMAIN_DICT_DETAILS: 字典项
  STRATEGY_ENGINE_DICT: 策略引擎字典类型
  STRATEGY_ENGINE_DICT_DETAILS: 策略引擎字典项
  SYS_PARAM: 系统参数
  OUTSIDE_SERVICE_DOMAIN: 外部服务-可用领域
  STRATEGY_PULISH_RECORDS: 策略发布记录
  VARIABLE_ADMIN: 变量管理
  VARIABLE_FUNCTION: 公共函数
  VARIABLE_SERVICE_INTERFACE: 变量服务接口
  BLAZE_SERVICE: blaze决策模块
  BLAZE_STRATEGY: blaze决策模块-策略
  STRATEGY_SIMULATION_DATASET: 数据集管理
  STRATEGY_SIMULATION_MOCK: 模拟策略管理
  STRATEGY_SIMULATION_TASK: 模拟任务管理
DomainChangeWarnTypeEnum:
  DATA_MODEL: 数据模型
  AB_PLAN: A/B测试
  ENGINE_VARS: 引擎变量
  STRATEGY_PARAM: 策略参数
  ROSTER_TYPE: 名单类型
  OUTSIDE_REF: 外部服务引入对象删除
  COMMON_SERVICE_REF: 公共决策模块引入对象删除
  COMPONENT: 组件

DomainDataModelContentEnum:
  INPUT_CONTENT: "{\"title\": \"input\",\"description\": \"输入信息\",\"type\": \"object\"}"
  OUTPUT_CONTENT: "{\"title\": \"output\",\"description\": \"输出信息\",\"type\": \"object\"}"
  ENGINE_VARS_CONTENT: "{\"title\":\"engineVars\",\"description\":\"引擎变量\",\"type\":\"object\",\"properties\":{\"ScoreResult\":{\"description\":\"评分卡结果\",\"type\":\"array\",\"items\":{\"type\":\"object\",\"properties\":{\"modelName\":{\"description\":\"评分卡名称\",\"type\":\"string\"},\"modelCode\":{\"description\":\"评分卡编码\",\"type\":\"string\"},\"score\":{\"description\":\"评分结果\",\"type\":\"double\"},\"initialScore\":{\"description\":\"初始分数\",\"type\":\"double\"},\"unexpectedCount\":{\"description\":\"非预期数量\",\"type\":\"int\"},\"characteristicCount\":{\"description\":\"特征项个数\",\"type\":\"int\"},\"reasonCodesSort\":{\"description\":\"原因码排序方式\",\"type\":\"string\"},\"charBins\":{\"description\":\"评分分箱结果列表\",\"type\":\"array\",\"items\":{\"type\":\"object\",\"properties\":{\"dataField\":{\"description\":\"变量名称\",\"type\":\"string\"},\"binLabel\":{\"description\":\"分箱名称\",\"type\":\"string\"},\"binIndex\":{\"description\":\"分箱序号\",\"type\":\"int\"},\"baseScore\":{\"description\":\"基准分\",\"type\":\"double\"},\"coef\":{\"description\":\"系数\",\"type\":\"double\"},\"partialScore\":{\"description\":\"子分\",\"type\":\"double\"},\"maxBinScore\":{\"description\":\"变量最大子分\",\"type\":\"double\"},\"minBinScore\":{\"description\":\"变量最小子分\",\"type\":\"double\"},\"reasonCode\":{\"description\":\"原因码\",\"type\":\"string\"},\"unexpected\":{\"description\":\"是否非预期\",\"type\":\"boolean\"}}}},\"reasonCodes\":{\"description\":\"原因码数组\",\"type\":\"array\",\"items\":{\"type\":\"object\",\"properties\":{\"reasonCode\":{\"description\":\"原因码编码\",\"type\":\"string\"},\"reasonDesc\":{\"description\":\"原因码说明\",\"type\":\"string\"}}}}}}}}}"
  EXTERNAL_VARS_CONTENT: "{\"title\":\"externalVars\",\"description\":\"外部变量\",\"type\":\"object\"}"
DomainDataModelEnum:
  NOT_ONLINE_STATE: 编辑中
  ONLINE_STATE: 启用
DomainDataModelVersionTypeEnum:
  MASTER_VERSION: 主版本
  MIN_VERSION: 小版本升级
DomainDictDetailExcelHeadEnum:
  TYPE_CODE: 字典类型编码
  CODE: 字典编码
  NAME: 字典名称
  PARENT_CODE: 上级字典编码
DomainDictDetailHeadEnum:
  OBJECT_HEAD: 对象
  PROPERTY_HEAD: 属性
  VALUE_HEAD: 值
  NAME_HEAD: 描述
DomainModeHeadEnum:
  ROOT_OBJECT_HEAD: 根对象
  SON_OBJECT_HEAD: 子对象
  ATTRIBUTE_NAME_HEAD: 属性名
  DESCRIPTION_HEAD: 中文描述
  TYPE_HEAD: 类型
  REQUIRED_HEAD: 必填
  ARRAY_HEAD: 数组
  DICTIONARIES_HEAD: 字典
DomainModelArrEnum:
  NO: 否
  YES: 是
DomainRosterDetailEntryMethodEnum:
  PERSON_WRITE: 人工填写
  FILE_IMPORT: 文件导入
  STRATEGY_TRIGGER_ENTER_DEPOT: 策略触发入库
DomainRosterDetailExcelHeadEnum:
  VALUE: 名单值
  INVALID_TIME: 到期时间
  DESCRIBE: 描述
DomainRosterDetailInventoryStateEnum:
  LISTED: 在库
  REMOVED: 已出库
DomainRosterDetailRemovalMethodEnum:
  ARTIFICIAL_OUT_DEPOT: 人工出库
  AUTOWIRED_OUT_DEPOT: 自动出库
DomainSheetNameEnum:
  INPUT_DOMAIN_SHEET_NAME: 输入数据
  OUTPUT_DOMAIN_SHEET_NAME: 输出数据
  ENGINE_DOMAIN_MODE_TYPE: 引擎变量
  ARRAY_DOMAIN_MODE_TYPE: 字典
DomainTypeEnum:
  DECISION_TYPE: 决策领域
  TOOL_TYPE: 公共工具库
DsBucketUseStateEnum:
  INIT: 启用(默认细分专用)
  START: 启用
  STOP: 停用
  DELETE: 删除
DsInterfaceStateEnum:
  WAIT: 待发布
  START: 发布中
  RELEASE: 已发布
  FAIL: 发布失败
  STOP: 已停用
DsServiceCommonTypeEnum:
  DEFAULT: 自有决策服务
  COMMON: 公共决策模块
  BLAZE: Blaze决策模块
DsServiceInterfaceActionTypeEnum:
  CREATE: 创建接口
  START_SUCCESS: 发布接口
  START_FAIL: 发布接口
  STOP: 停用接口
  RESTART_SUCCESS: 重新发布接口
  RESTART_FAIL: 重新发布接口
  DELETE: 删除接口
DsStrategyActionTypeEnum:
  ADD_NEW: 新建
  SUB_TEST: 提交测试
  APPRO_ONLINE: 申请上线
  APPRO_REJECT: 审批拒绝
  APPRO_PASS: 审批通过
  TO_TEST: 退回开发
  BACK_OFFLINE: 上线回退
  OFF_LINE: 下线
  DELETE: 删除
  ONLINE: 完整上线
  AUTO_PRODUCT: 自动上线
  AUTO_OFFLINE: 自动下线
  CANCEL_ONLINE: 取消上线
  APPLY_RELEASE: 提交审批
  APPRO_RELEASE: 审批通过
  DEACTIVATE: 停用
  REPUBLISH: 启用
  AUTO_TERMINAL: 自动终止
  SUBMIT_PRODUCT: 重新上线
  BACK_ONLINE: 回退
  START_TEST: 开始测试
  ONLINE_APPROVING: 上线审批
  RELEASE_APPROVING: 发布审批
  ONLINE_FAIL: 上线失败
  APPRO_PASS_ONLINE: 审批通过
  ONLINE_PUBLISH: 发布
  RELEASE_DELETE: 删除
  COMMON_AUTO_REPUBLISH: 上线发布
  SUB_VERIFY: 提交审核
  SUB_VERIFY_PASS: 审核通过
  SUB_VERIFY_REJECT: 审核拒绝
  SIM_ONLINE: 模拟上线
  PUBLISH_IMPORT: 导入
  SUBMIT_EFFECTIVE_PRODUCT: 立即生效
  SIM_ONLINE_OFFLINE: 自动下线
DsStrategyOnlineApplyTypeEnum:
  APPLY_PRODUCT: 完整上线
  APPLY_TEST_RUN: 灰度
  APPLY_RUN_WITH: 陪跑
DsStrategyOnlineStateEnum:
  INIT: 初始化（待审批）
  NOT_EFFECTIVE: 未生效（等待扫描）
  EFFECTIVE: 已生效
  OVERDUE: 过期（删除）
DsStrategyParamDynamicRefreshRateEnum:
  DAY_REFRESH: 每日刷新
  MONTH_REFRESH: 每月刷新
  YEAR_REFRESH: 每年刷新
  NOT_REFRESH: 不刷新
DsStrategyParamTypeEnum:
  STATIC_PARAM: 静态参数
  DYNAMIC_PARAM: 动态参数
DsStrategyStatusEnum:
  DEV: 开发
  TEST: 测试中
  APPROVING: 待审批
  REJECT: 审批拒绝
  PRODUCT: 生产
  TEST_RUN: 灰度
  ONLINE_RELEASED: 待上线
  OFF_LINE: 已下线
  DISCARD: 已删除
  RUN_WITH: 陪跑
  WAIT_PRODUCT: 待生效
  RELEASED: 启用
  DEACTIVATED: 停用
  RELEASE_FAIL: 上线失败
  CANCEL_ONLINE_RELEASED: 上线取消
  WAIT_VERIFY: 待审核
  VERIFY_PASS: 启用
  VERIFY_REJECT: 审核拒绝

DsStrategyTerminalTypeEnum:
  SELECT_CONDITION: 选择条件
  TERMINAL_TIME: 指定回滚/终止时间
EditTypeEnum:
  CURRENT: 当天
  TRID: 最近3天
  SEVEN: 最近7天
  MONTH: 最近1个月
  YEAR: 最近1年
  CURRENT_MONTH: 当月
  MONTH3: 最近3个月
  MONTH6: 最近6个月
  YEAR2: 最近24个月/2年
FieldValueTypeEnum:
  INPUT: 输入值
  VARIABLE: 选择的变量
FlowTypeEnum:
  NEW: 新建（新建流程的时候一定要传）
  SELF_SAVE: 手动保存
  AUTO_SAVE: 自动保存
FrontPannelTypeEnum:
  DESC: 基本信息
  DOC: 文档信息
  TABLE: 版本信息
  CELL: 策略引用
  CURRENT_TAG: 当前标签
  TAGS: 可用标签
  ADD_TAG: 新建标签
  LOG: 当前版本保存记录
  REMARK: 备注
  GROUP: A/B测试分组
  LIST: 引用信息
  LIFECYCLE: 生命周期
GenerateCustomEnum:
  NAME: 生成姓名
  IDNO: 生成身份证号
  MOBILE: 生成手机号
  EMAIL: 生成邮箱
  COMPANY: 生成公司名称
  SERIAL_NUMBER: 生成流水号
  ADDRESS: 生成详细地址
GenerateTypeEnum:
  ENUM: 枚举
  RANDOM: 随机
  LOGIC: 逻辑依赖
  CUSTOM: 自定义
IconEnum:
  ERROR: 错误
  WARNING: 警告
  SUCCESS: 成功
InputExpectTypeEnum:
  INPUT: 输入
  EXPECT: 预期结果
  RESULTS: 实际结果
InputOutputTypeEnum:
  REAL_TIME: 实时
  BATCH: 批量
K8sApiStatus:
  OK: 部署成功
  CREATED: 创建类的请求完全成功
  NO_CONTENT: 请求无内容。
  TEMPORARY_REDIRECT: 请求资源的地址被改变。
  BAD_REQUEST: 非法请求
  UNAUTHORIZED: 认证失败。
  FORBIDDEN: 请求被拒绝访问。
  NOT_FOUND: 请求资源不存在。
  METHOD_NOT_ALLOWED: 资源不支持的方法。
  CONFLICT: 创建的资源已经存在
  UNPROCESSABL_EENTITY: 请求的部分数据非法
  TOO_MANY_REQUESTS: 访问太频繁。
  INTEMAL_SERVER_ERROR: 未知错误
  SERVICE_UNAVAILABLE: 请求的服务无效。
  SERVER_TIMEOUT: 请求超时。
  EXCEPTION: 资源部署失败。
MetricsFuncEnum:
  DISTINCT: 去重
  SUM: 求和
  COUNT: 统计数量
  AVG: 平均值
  MAX: 最大值
  MIN: 最小值
MetricsScopeEnum:
  GROUPING: 分组
  GLOBAL: 全局
MetricsTypeEnum:
  BASE: 基础指标
  DERIVATION: 衍生指标
ModelFileTypeEnum:
  MODEL_FILE: 模型文件
  MODEL_PREDICT_SCRIPT_FILE: 模型预测脚本文件
  MODEL_FEATURE_FILE: 模型特征文件
ModelTypeEnum:
  PMML_SCORE_CARD: PMML_评分卡模型
  PMML_NON_SCORE_CARD: PMML_非评分卡模型
  PKL: PKL模型
  MODEL: MODEL模型
OperateSourceEnum:
  DECISION_SERVICE: 决策服务
  SIM_STRATEGY: 策略模拟
OutsideServiceDataCacheTypeEnum:
  DAY: 天
  HOUR: 小时
  SECOND: 秒
OutsideServiceDeployTypeEnum:
  DATA_FILE.label: 生成文件
  DATA_FILE.desc: 成功
  DATA_SHARE_DIRECTORY_FILE.label: 文件保存成功,保存路径
  DATA_SHARE_DIRECTORY_FILE.desc: 成功
  OUTSIDE_SERVICE.label: 基本信息
  OUTSIDE_SERVICE_DOMAIN.label: 领域授权
  OUTSIDE_SERVICE_REQ.label: 请求参数
  OUTSIDE_SERVICE_RESP.label: 返回参数
  OUTSIDE_SERVICE_RESP_CODE.label: 响应码定义
  OUTSIDE_SERVICE_VAR.label: 参数信息
  OUTSIDE_SERVICE_SYS_PARAM_USE.label: 系统参数引用
  OUTSIDE_SERVICE_SYS_PARAM.label: 系统参数
  FILE_ANALYSE.label: 文件验证
  OUTSIDE_SERVICE_DATA_IMPORT.label: 基本信息
  OUTSIDE_SERVICE_RESPCODE_DATA_IMPORT.label: 响应码定义
  SYS_PARAM_DATA_IMPORT.label: 系统参数

OutsideServiceMockDetailDesiredStateEnum:
  FAIL: 失败
  SUCCESS: 成功
OutsideServiceMockDetailExcelHeadEnum:
  MOCK_TYPE: Mock类型
  DESIRED_STATE: 预期状态
  QUERY_RESULT: 是否查得
  RESP_MESSAGE: 响应报文
OutsideServiceMockDetailMockTypeEnum:
  MATE_INPUT_PARAMETER_RETURN: 匹配入参返回
  DEFAULT_RETURN: 默认返回
OutsideServiceMockDetailQueryResultEnum:
  NO: 否
  YES: 是
OutsideServiceMockDetailSourceEnum:
  ON_LINE_INPUT: 在线输入
  FILE_IMPORT: 文件导入
OutsideServiceMockSpaceTypeEnum:
  domain: 领域空间
  variable: 变量空间
OutsideServiceMockStatusEnum:
  STOP: 停用
  START: 启用
OutsideServiceOperationEnum:
  CREATE: 创建
  ENABLE: 启用
  DISABLE: 停用
OutsideServiceQueryTypeEnum:
  MOCK_TEST: Mock测试
  OUTSIDE_SERVICE_INTERFACE_TEST: 外部服务接口测试
OutsideServiceRequestTypeEnum:
  HEADER_PARAM: header参数
  BODY_PARAM: body参数
  URL_PARAM: url参数
OutsideServiceRespCodeLeftValueEnum:
  INTERFACE_QUERY_STATE: 接口状态
  INTERFACE_QUERY_RESULT: 是否查得
OutsideServiceRespCodeTypeEnum:
  INTERFACE_STATE: 接口状态
  WHETHER_SELECT_GAIN: 是否查得
  CHARGE_CODE: 是否计费
  PROCESSING: 处理中
OutsideServiceSimpleSourceTypeEnum:
  DECISION: 决策领域
  SERVICETEST: 外数测试
  VAR: 变量空间
  SIMULATE_STRATEGY: 策略模拟
  VAR_BACKTRACKING: 变量回溯
  SIM_STRATEGY: 策略模拟
OutsideServiceStateEnum:
  EDITING: 编辑中
  ENABLED: 启用
  DISABLED: 停用
OutsideServiceValueTypeEnum:
  VAR_INCOM: 变量传入
  FIXED_VALUE: 固定值
  SYS_PARAM: 系统参数
  SYS_METHOD: 系统方法
PermissionConfigCommonStrategyPermissionEnum:
  READ: 查看
  EDIT: 编辑
  APPROVAL: 审批
  RELEASE: 发布
PermissionResourceConfigCodeEnum:
  SYS_LOG: 系统日志
  EXTERNAL_MAIN: 外部服务
  DOMAIN_MAIN: 领域
  DOMAIN_INDEX: 领域首页
  DOMAIN_SUMMARY: 概述
  DOMAIN_TEAM: 团队管理
  DOMAIN_RESOURCE: 资源管理
  DOMAIN_SERVICE: 决策服务
  DOMAIN_COMMON_SERVICE: 公共决策模块
  DOMAIN_DATA_MODEL: 数据模型
  DOMAIN_BLAZE_SERVICE: blaze决策模块
  DOMAIN_AB: A/B测试
  DOMAIN_SERVICE_API: 决策服务接口
  DOMAIN_DATA_QUERY: 决策数据查询
  DOMAIN_OUTSIDE: 外部服务
  DOMAIN_STRATEGY_PARAM: 策略参数
  DOMAIN_ROSTER: 名单管理
  DOMAIN_ROSTER_IN: 在库名单
  DOMAIN_ROSTER_OUT: 出库名单
  DOMAIN_ROSTER_TYPE: 名单类型
  SERVICE_MAIN: 决策服务
  BUCKET_MAIN: 决策细分
  STRATEGY_MAIN: 策略
  STRATEGY_INDEX: 策略首页
  STRATEGY_SUMMARY: 概述
  STRATEGY_DATA_BOARD: 数据看板
  STRATEGY_PROCESS: 主流程
  STRATEGY_COMPONENT: 决策组件
  STRATEGY_DATA_VAR: 数据与变量
  STRATEGY_OUTSIDE: 外部服务
  STRATEGY_VARIABLE: 变量服务
  STRATEGY_COMMON: 公共决策模块
  DIRECTORY_MAIN: 文件夹
  COMPONENT_MAIN: 组件
  VARIABLE_MAIN: 变量空间
  VARIABLE_INDEX: 空间首页
  VARIABLE_SUMMARY: 概述
  VARIABLE_TEAM: 团队管理
  VARIABLE_RESOURCE: 资源管理
  VARIABLE_ADMIN: 变量管理
  VARIABLE_DATA_MODEL: 数据模型
  VARIABLE_OUTSIDE: 外部服务引入
  VARIABLE_INTERNAL: 内部数据管理
  VARIABLE_PREP: 数据预处理
  VARIABLE_FUNCTION: 公共函数
  VARIABLE_ROSTER: 名单管理
  VARIABLE_ROSTER_IN: 在库名单
  VARIABLE_ROSTER_OUT: 出库名单
  VARIABLE_ROSTER_TYPE: 名单类型
  VARIABLE_SERVICE_API: 变量发布
  VARIABLE_DATA_QUERY: 结果查询
  BLAZE_MAIN: blaze模块
PermissionResourceConfigEnableEnum:
  NO_ENABLE: 不可用
  ENABLE: 可用
PermissionResourceConfigPermissionEnum:
  READ: 查看
  EDIT: 编辑
  VERIFY: 审核
  RELEASE: 发布
  APPROVAL: 上线审批
PermissionResourceConfigPropertyEnum:
  READ: 查看
  EDIT: 编辑
PermissionResourceConfigSourceEnum:
  INITIALIZE: 初始化
  CONFIG: 手动配置
  EXTENDS: 继承
PermissionResourceConfigStrategyEnum:
  STRATEGY_PROCESS: 外部服务
  STRATEGY_VARIABLE: 变量服务
  STRATEGY_COMPONENT: 公共决策模块
  STRATEGY_BLAZE: blaze决策模块
PermissionResourceConfigTypeEnum:
  DOMAIN: 决策领域
  SERVICE: 决策服务
  BUCKET: 决策细分
  STRATEGY: 策略
  DIRECTORY: 文件夹
  COMPONENT: 组件
  VARIABLE: 变量空间
  BLAZE: blaze模块
ProPanelEnum:
  PRO_TAB: 属性信息
  VERSION_TAB: 版本信息
  REF_TAB: 引用信息
  DESC_TAG_TAB: 备注与标签
  BASE: 基本信息
  WORD: 文档信息
  CURRENT_SAVE: 当前版本保存记录
  VERSION: 当前版本保存记录
  USE_OTHER_REF: 被其他策略引用
  USE_CURRENT_REF: 被本策略内组件引用
  DATA_VAR: 使用的数据与变量
  CURENT_TAG: 当前标签
  ABLE_ADD_TAG: 可添加标签
  NEW_TAG: 新建标签
  DESC: 备注
PropertyPanelTitleEnum:
  DOMAIN_BASIC_TITLE: 基本信息
  ABTEST_PLAN_USE_TITLE: 使用的随机数信息
RecycleTypeEnum:
  DOMAIN: 领域
  EXTERNAL: 外部服务
  SERVICE: 决策服务
  DOMAIN_DATA_MODEL: 数据模型
  COMMON_SERVICE: 公共决策模块
  BUCKET: 决策细分
  STRATEGY: 策略
  COMPONENT: 组件
  DOMAIN_AB: A/B测试
  DOMAIN_STRATEGY_PARAM: 策略参数
  DOMAIN_ROSTER_IN: 名单值
  DOMAIN_ROSTER_TYPE: 名单类型
ReleaseVersionTypeEnum:
  RELEASE_VERSION: 主版本
  PATCH_VERSION: 补丁版本
RollbackIndexTypeEnum:
  ALL: 近五分钟失败率
  ANY: 近五分钟审批通过率
RollbackTypeEnum:
  ALL: 满足以下所有条件
  ANY: 满足以下任意条件
RuleConditionLinkTypeEnum:
  ALL: 满足以下所有条件
  ANY: 满足以下任意条件
  NONE: 所有条件都不满足
SelectTimeTypeEnum:
  CURRENT: 当天
  SEVEN: 最近7天
  MONTH: 最近30天
  YEAR: 最近1年
  ALL: 所有
  ASSIGN: 指定日期
ServiceBucketUseEnum:
  USE_YES: 已使用
  USE_NO: 未使用
SimOnlineStatusEnum:
  ONLINE: 已上线
  OFFLINE: 已下线
SimTaskStartModeEnum:
  START_NOW: 保存后立即启动
  SCHEDULE_TIME: 指定启动时间

SimTaskStatusEnum:
  NOT_STARTED: 未开始
  IN_PROGRESS: 进行中
  SUCCESS: 成功
  FAIL: 失败
StaticTemplateEnum:
  statement_provider: 添加语句根节点
  multi_statement_template: 可选多行添加语句
  action_statement_template: 动作添加语句
  varprocess_statement_provider: 变量模板的添加语句
  while_statement_provider: 条件循环下的添加语句
  if_then_while_statement_provider: 如果那么的添加语句
  assign_template: 变量赋值
  object_assign_template: 对象赋值
  array_assign_template: 数组赋值
  update_param_template: 更新策略参数
  array_add_template: 向数组添加对象
  array_loop_template: 数组循环
  while_do_template: 条件循环
  if_then_template: 如果。。。那么。。。
  if_then_else_template: 如果。。。那么。。。否则
  if_then_while_template: 循环内的如果那么
  if_then_else_while_template: 循环内的如果那么否则
  logic_provider: 逻辑表达式
  number_provider: 数值
  string_provider: 字符串
  date_provider: 时间
  function_common_template: 公共函数
  function_common_string_template: 字符串公共函数
  function_common_number_template: 数值公共函数
  function_common_date_template: 日期公共函数
  function_common_bool_template: 布尔公共函数
  data_provider_exception_value: 异常值provider
  data_provider_dict_value: 字典provider
StrategyEffectTypeEnum:
  AUTO_ONLINE: 审批通过自动上线
  SPECIFIED_TIME: 指定生效时间
StrategyRandomTypeEnum:
  COMPLETE_RANDOM: 完全随机
  HASH_EXTERNAL_RANDOM: hash(外部传入字段)
StrComPmmlModelTypeEnum:
  SCORE_CARD: 评分卡
  NON_SCORE_CARD: 非评分卡
StrComPmmlReasonCodeImportFlagEnum:
  FALSE: 否
  TRUE: 是
StrComponentCompileMessageEnum:
  VAR_DEL: 组件使用的{0}[{1}]已删除
  VAR_ATTR_DISACCORD: 组件使用的{0}[{1}]的属性不一致
  VAR_REF_DEL: 组件定义的{0}{1}引用的对象[{2}]已删除
  VAR_REF_ATTR_DISACCORD: 组件定义的{0}{1}引用的对象[{2}]的属性不一致
  VAR_NOT_USE: 定义的{0}[{1}]未被使用
  VAR_NOT_INIT: 本地变量[{0}]没有初始化就使用
  VAR_CHECK_FAIL: 组件[{0}]检入失败
  COMPONENT_REF_DEL: 引用的{0}[{1}]已删除
  COMPONENT_REF_CHECK: 引用的{0}[{1}]为检出状态
  COMPONENT_REF_VERSION_CHECK: 引用的{0}[{1}]不是最新版本
  ABTEST_REF_DEL: 引用的AB测试方案[{0}]已删除
  ABTEST_REF_DISACCORD: 引用的AB测试方案[{0}]的分组与方案定义的分组不一致
  STRATEGY_PARAM_DEL: 组件查询的策略参数[{0}]已删除
  STRATEGY_PARAM_ATTR_DISACCORD: 组件更新的策略参数[{0}]属性已变更
  ROSTER_DEL: 组件引用的名单类型[{0}]已删除
  OUTSIDE_REF_STRATEGY_DEL: 该策略没有引用的外部服务或外部服务已删除
  OUTSIDE_REF_DEL: 外部服务[{0}]已删除
  OUTSIDE_REF_DISACCORD: 引入的外部服务[{0}]对象[{1}]已删除
  COMMON_REF_STRATEGY_DISACCORD: 该策略没有引用的公共决策模块或已删除
  COMMON_REF_SERVICE_DISACCORD: 公共决策模块[{0}]已删除
  COMMON_REF_DISACCORD: 引入的公共决策模块对象[{0}]已删除
  BLAZE_REF_STRATEGY_DISACCORD: 该策略没有引用的blaze决策模块或已删除
  BLAZE_REF_SERVICE_DISACCORD: blaze决策模块[{0}]已删除
  BLAZE_REF_DISACCORD: 引入的blaze决策模块对象[{0}]已删除
  FOLDER_REF_PARAM: 引入的决策包中组件[{0}]存在入参或出参
StrComponentScoreExcelEnum:
  VAR_NAME: 变量名
  VAR_LABEL: 变量中文名
  DATA_TYPE: 数据类型
  BENCHMARK_SCORE: 基准分
  COEF: 系数
  BOX_VALUE: 分箱值
  BOX_DESC: 分箱描述
  SCORE: 分值
  REASON_CODE: 原因码
SysDynamicBusinessBucketEnum:
  SYS_USER_LOGIN:
  SYS_USER_LOGIN_OUT:
  SYS_USER_REGISTER:
  EXTERNAL_MAIN_DESC: 外部服务
  EXTERNAL_MAIN_INFO: 基本信息
  EXTERNAL_MAIN_PARAMS: 参数信息
  EXTERNAL_MAIN_RESP: 响应码定义
  EXTERNAL_MAIN_AUTH: 服务授权
  DOMAIN_MAIN_DESC: 领域
  DOMAIN_MAIN_ATTR: 领域属性
  SERVICE_MAIN_SERVICE: 决策服务
  SERVICE_MAIN_BUCKET: 决策细分
  SERVICE_MAIN_STRATEGY: 策略
  SERVICE_MAIN_CONFIG:
  DOMAIN_DATA_MODEL: 数据模型
  DOMAIN_COMMON_SERVICE_MODULE: 公共模块
  DOMAIN_COMMON_SERVICE_BUCKET: 公共决策细分
  DOMAIN_COMMON_SERVICE_STRATEGY: 公共策略
  DOMAIN_COMMON_SERVICE_VERSION:
  DOMAIN_COMMON_SERVICE_DATA_MODEL:
  DOMAIN_COMMON_SERVICE_CONFIG:
  DOMAIN_BLAZE_SERVICE_MODULE: blaze决策模块
  DOMAIN_AB_PLAN: AB测试方案
  DOMAIN_AB_RANDOM: 随机数
  DOMAIN_SERVICE_API: 接口
  DOMAIN_STRATEGY_PARAM_PARAM: 参数
  DOMAIN_STRATEGY_PARAM_INIT: 初始值
  DOMAIN_STRATEGY_PARAM_CURRENT: 当前值
  DOMAIN_ROSTER_TYPE: 名单类型
  DOMAIN_ROSTER_VALUE: 名单值
  DOMAIN_TEAM_USER: 团队成员
  STRATEGY_COMPONENT_DIRECTORY: 文件夹
  COMPONENT_MAIN_MAINFLOW: 主流程
  COMPONENT_MAIN_RULE: 规则集
  COMPONENT_MAIN_TABLE: 决策表
  COMPONENT_MAIN_TREE: 决策树
  COMPONENT_MAIN_MODEL: 评分卡
  COMPONENT_MAIN_FUNCTION: 自定义函数
  COMPONENT_MAIN_SCRIPT: 自定义脚本
  COMPONENT_MAIN_SUBFLOW: 决策流
  COMPONENT_MAIN_PMML: PMML模型
  COMPONENT_MAIN_LOOKUP: 参数表
  COMPONENT_MAIN_PYTHON_PKL: PKL模型
  COMPONENT_MAIN_PYTHON_MODEL: MODEL模型
  COMPONENT_MAIN_SINGLE_RULE: 规则
  STRATEGY_DATA_VAR_ENGINE_VARS: 引擎变量
  STRATEGY_OUTSIDE_IN_OBJECT: 外部服务引入对象
  STRATEGY_COMMON_IN_OBJECT: 公共决策模块引入对象
  STRATEGY_VARIABLE_IN_OBJECT: 变量服务引入对象
  VARIABLE_MAIN_DESC: 变量空间
  VARIABLE_MAIN_ATTR: 变量空间属性
  VARIABLE_ADMIN: 变量版本
  VARIABLE_DATA_MODEL: 数据模型对象
  VARIABLE_OUTSIDE_IN_OBJECT: 外部服务
  VARIABLE_FUNCTION: 公共函数
  VARIABLE_INTERNAL: 内部数据
  VARIABLE_SERVICE: 变量服务
  VARIABLE_INTERFACE: 变量清单
  VARIABLE_ROSTER_TYPE: 名单类型
  VARIABLE_ROSTER_VALUE: 名单值
  VARIABLE_TEAM_USER: 团队成员
SysDynamicOperateTypeEnum:
  CREATE: 创建
  ADD: 添加
  EDIT: 编辑
  DELETE: 删除
  REMOVE: 移除
  COPY: 复制
  SET: 设置
  REFRESH: 刷新
  VERSION_ASSIGN: 版本分配
  INTRODUCE: 引入
  CANCEL_INTRODUCE: 取消引入
  START: 启动
  STOP: 停用
  RELEASE: 发布
  CANCEL_RELEASE: 取消发布
  REPUBLISH: 重新发布
  IN_WAREHOUSE: 入库
  EX_WAREHOUSE: 出库
  AUTO_EX_WAREHOUSE: 自动出库
  CHECK_IN: 检入
  CHECK_OUT: 检出
  FORCE_CHECK_OUT: 强制检出
  CANCEL_CHECK_OUT: 取消检出
  RESTORE_VERSION: 恢复版本
  VAR_APPLY_UP: 申请上架
  VAR_UP: 上架
  VAR_DOWN: 下架
  ENABLE: 启用
  IMPORT: 导入
  PROGRESS: 进行
  LOGIN: 登录
  LOGIN_OUT: 退出
  USER_REGISTER: 注册
  APPROVED: 审核通过
  REFUSE: 审核拒绝
  RETURN_EDIT: 退回编辑
SysDynamicSpaceTypeEnum:
  DOMAIN: 领域
  EXTERNAL_SERVICE: 外部服务
  VARIABLE: 变量空间
  SYSLOG: 系统日志
SysMethodTypeEnum:
  OUTSIDE_SERVICE: 外数请求参数
  DESENSITIZATION: 数据脱敏
  TEST_DATA_GENERATION: 测试数据生成
SysParamNameEnum:
  DATA_CACHE: 外部服务数据缓存天数
  RETRY_COUNT: 外部服务调用失败重试次数
  SINGLE_TIMEOUT: 外部服务单笔调用超时时间 (ms)
  TOTAL_TIMEOUT: 外部服务调用总超时时间 (ms)
  GATEWAY_URL: rest服务网关Url
  K8S: 是否调用k8s接口部署服务，1是0否
  NACOS: 是否调用nacos接口动态更新策略组件，1是0否
  TRACE_TEST: 策略测试&组件测试trace开关，1开0关
  ENVIRONMENT: 当前环境1-配置环境,2-运行环境,3-分析环境
  RELEASE_TYPE: 发布方式设置, 详见Wiki
  DEALING_RETRY_TIMES: 处理中重试次数
  DEALING_RETRY_INTERVAL: 处理中重试间隔时间(ms)
SysParamTypeEnum:
  INSIDE_PARAM: 内置参数
  CUSTOM_PARAM: 自定义参数
SysParamUseBusinessTypeEnum:
  OUTSIDE_SERVICE_TYPE: 外部服务
SysParamUsedTypeEnum:
  INSIDE_NOT_USE: 内置类型不显示
  NOT_USE: 未使用
  ALREADY_USE: 已使用
TargetTypeEnum:
  DEV: 开发
  TEST: 测试
  PROD: 生产
TemplateFunctionTypeEnum:
  TEMPLATE: 变量模板
  FUNCTION: 公共方法
  PREP: 数据预处理
TemplateUnitTypeEnum:
  STRATEGY_COMPONENT: 策略组件
  SPACE_VARIABLE: 空间变量
  SPACE_COMMON_FUNCTION: 空间公共函数
TestDataSourceEnum:
  AUTO: 在线自动生成
  INPUT: 在线输入
  FILE: 文件导入
  PROD: 生产数据导入
TestDataTypeEnum:
  TEST_DETAIL: 测试集明细数据
  EXECUTE_RESULT: 测试执行结果
TestDataUpdateTypeEnum:
  ADD: 新增
  UPDATE: 修改
  DELETE: 删除
  COPY: 复制
TestDetailDataFieldsEnum:
  ID: 主信息编号
  PARENT_ID: 父级信息编号
  INDEX: 索引
  NAME: 字段名
  LABEL: 中文描述
  TYPE: 数据类型
  IS_ARR: 是否数组
  FIELD_TYPE: 字段类型
TestExcelFileEnum:
  ID: 编号
  PARENT_ID: 主信息编号
TestExecStatusEnum:
  EXCEPTION: 异常
  NORMAL: 正常
TestHeaderValueEnum:
  HEADER: 表头
  VALUE: 数据
TestResultDiffStatusEnum:
  INCONSISTENT: 不一致
  CONSISTENT: 一致
  NO_RESULT: 无比对结果
TestResultsQueryTypeEnum:
  ALL: 全部
  NORMAL: 正常
  EXCEPTION: 异常
  CONSISTENT: 预期一致
  INCONSISTENT: 预期不一致
TestTableEnum:
  MASTER.message: 输入数据
  INPUT.message: 输入数据
  TEST_RESULT.message: 测试结果
  EXPECT.message: 预期结果
  CURRENT_EXPECT.message: 当前预期结果
  OUTPUT.message: 输出结果
  RET.message: 预期结果
  RESULTS.message: 实际结果
  MASTER.shortmsg: 输入
  INPUT.shortmsg: 输入
  TEST_RESULT.shortmsg: 测试
  EXPECT.shortmsg: 预期
  CURRENT_EXPECT.shortmsg: 预期
  OUTPUT.shortmsg: 输出
  RET.shortmsg: 预期
  RESULTS.shortmsg: 实际

UseableFlagEnum:
  USABLE: 可用
  UAVAILABLE: 不可用
VarProcessInterfaceStateEnum:
  EDITING: 编辑中
  TESTING: 测试中
  PENDING_REVIEW: 待审核
  REJECTED: 审核拒绝
  ENABLING: 启用中
  ENABLED: 启用
  DISABLED: 停用
  FAILED: 启用失败
  WAIN_ONLINE: 待上线
  WAIN_APPROVAL: 待审批
  APPROVAL_REJECTED: 审批拒绝
VarProcessInterfaceVariableOutputFlagEnum:
  UNCHECKED: 未勾选
  CHECKED: 勾选
VarTemplateTypeEnum:
  VAR_PROCESS: 变量管理
  COMMON_FUNCTION: 公共函数
  SERVICE_INTERFACE: 服务接口发布
LocalDataTypeEnum:
  desc: 基本信息
  doc: 文档信息
  table: 版本信息
  cell: 策略引用
  currentTag: 当前标签
  tags: 可用标签
  addTag: 新建标签
  log: 当前版本保存记录
  remark: 备注
  thumbnail: 缩略图
  editDetail: 跳出循环条件
  LIFECYCLE: 生命周期
DeployParseMethodEnum:
  DOMAIN_IMPORT: 领域导入
  STRATEGY_IMPORT: 策略导入
  AUTO_DEPLOY: 自动部署
DsServiceInterfaceDocumentStatusCodeEnum:
  REST_200: 成功
  REST_400: 客户端错误，请求包含语法错误或无法完成请求
  REST_404: 网络异常
  REST_02_207_B_001: 领域不可用
  REST_02_207_B_002: 服务已停用
  REST_02_207_B_003: 服务没有可用的决策服务接口
  REST_02_207_B_004: 服务下没有可用的细分
  REST_02_207_B_005: 服务下没有可用的配置信息
  REST_02_207_B_006: 细分下没有可用的策略
  REST_02_207_B_007: 策略ID不存在
  REST_02_207_B_008: 策略id和服务code不匹配
  REST_02_207_G_001: 服务配置没有配置流水号/决策结果映射字段
  REST_02_207_G_002: 保存数据异常
  REST_02_206_A_001: 服务调用流水号未传值
  REST_02_209_A_002: 组装input类数据异常
  REST_02_209_A_003: input类没有实现类！
  REST_02_209_Y_001: 分流脚本执行异常
  REST_02_211_Y_001: 陪跑策略执行异常
  REST_02_010_Z_001: 未知异常
StrategyComparisonContentComponentItemEnum:
  SINGLE_RULE: 规则
  RULE: 规则集
  TABLE: 决策表
  LOOKUP: 参数表
  TREE: 决策树
  FUNCTION: 自定义函数
  MODEL: 评分卡
  PMML: PMML模型
  PYTHON_PKL: PKL模型
  PYTHON_MODEL: Model模型
  SUBFLOW: 决策流
StrategyComparisonContentItemEnum:
  USE_COMPONENT: 使用的组件
  USE_VARS: 使用的数据与变量
  AB_TEST: AB测试方案
  OUTSIDE_SERVICE: 外部服务
  COMMON_MODULE: 公共模块
  BLAZE_MODULE: Blaze 决策模块
  VARIABLE_SERVICE: 变量服务
  ENGINE_VARS_NUM: 变量数
  REF_SERVICE_OBJECT: 引入对象
  REF_COMMON_OBJECT: 引入对象
  REF_BLAZE_OBJECT: 引入对象
  REF_VARIABLE_OBJECT: 引入对象
StrategyComparisonContentTypeEnum:
  MAIN_FLOW: 主流程
  COMPONENT: 决策组件
  ENGINE_VARS: 引擎变量
  OUTSIDE_SERVICE_REF: 引入外部服务
  COMMON_MODULE_REF: 引入公共模块
  BLAZE_MODULE_REF: 引入 Blaze 决策模块
  VARIABLE_SERVICE_REF: 引入变量服务
StrategyComparisonPropertyItemEnum:
  VERSION: 版本号
  STATUS: 版本状态
  VERSION_SOURCE: 版本来源
  DATA_MODEL: 引用数据模型
GroupTypeEnum:
  DAY: 按日统计
  MONTH: 按月统计
ServiceCallReportTypeEnum:
  CALLCOUNT: 调用量
  SUCCESSRATE: 成功率
  QUERYRATE: 查得率
  AVGRESPTIME: 平均响应时间
  MAXRESPTIME: 最大响应时间
ConnectorEnum:
  AND: 与
  OR: 或
DbJobGroupEnum:
  CLICKHOUSESQLCONSUMER: ck数据库sql消费补偿组
  KAFKAPRODUCER: kafka消息生产者补偿组
  KAFKACONSUMER: kafka消息消费者补偿组
DbType:
  MYSQL: MySql数据库
  CLICK_HOUSE: clickhouse 数据库
  OTHER: 其他数据库
KafkaTopicEnum:
  ERROR_EVENT: 错误事件
OperatorEnum:
  EQUAL: 等于
  NOT_EQUAL: 不等于
  LESS_THAN: 小于
  LESS_OR_EQUAL: 小于或等于
  GREATER_THAN: 大于
  GREATER_OR_EQUAL: 大于或等于
  INCLUDE: 包含
  NOT_INCLUDE: 不包含
  BEGIN_WITH: 开始于
  END_WITH: 结束于
  IS_NULL: 为 null
  IS_NOT_NULL: 不为 null
  IS_EMPTY: 为空
  IS_NOT_EMPTY: 不为空
  IN: 在列表中
  NOT_IN: 不在列表中
  BETWEEN_AND: 在范围
  NOT_BETWEEN_AND: 不在范围
OperatorTypeEnum:
  EQUALS: 等值
  LIKE: 模糊
  RANGE: 范围
  LIST: 列表
  NONE_VALUE: 无值

YesNoEnum:
  NO: 否
  YES: 是
SFtpFlagEnum:
  WISECO: 睿智本身
DomainModelHeadEnum:
  ROOT_OBJECT_HEAD: 根对象
  SON_OBJECT_HEAD: 子对象
  ATTRIBUTE_NAME_HEAD: 属性名
  DESCRIPTION_HEAD: 中文描述
  TYPE_HEAD: 类型
  ARRAY_HEAD: 是否数组
  EXTEND_HEAD: 是否扩展
  DICTIONARIES_HEAD: 字典编码
DomainModelSheetNameEnum:
  INPUT: 输入信息
  OUTPUT: 输出信息
  ENGINE_VARS: 引擎变量
  INTERNAL_DATA: 内部数据变量
  EXTERNAL_DATA: 外部服务变量
  COMMON_DATA: 公共模块变量
  BLAZE_DATA: blaze模块变量
  EXTERNAL_VARS: 变量清单模块变量
  RAW_DATA: 原始数据
  VARS: 变量清单
DsServiceInterfaceDataModelHeadEnum:
  ROOT_OBJECT_HEAD: 对象
  SON_OBJECT_HEAD: 子对象
  ATTRIBUTE_NAME_HEAD: 属性名
  DESCRIPTION_HEAD: 中文描述
  TYPE_HEAD: 类型
  ARRAY_HEAD: 是否数组
  DICTIONARIES_HEAD: 数据字典
DsServiceInterfaceSheetNameEnum:
  BASIC_INFO_VO: 基本信息
  REQUEST_STRUCTURE: 请求参数
  REQUEST_SAMPLE: 请求示例
  RESPONSE_STRUCTURE: 返回结果
  RESPONSE_SAMPLE: 返回示例
  RESPONSE_CODE: 返回状态码

EngineVarsModelHeadEnum:
  ROOT_OBJECT_HEAD: 根对象
  SON_OBJECT_HEAD: 子对象
  ATTRIBUTE_NAME_HEAD: 属性名
  DESCRIPTION_HEAD: 中文描述
  TYPE_HEAD: 类型
  ARRAY_HEAD: 是否数组
  DICTIONARIES_HEAD: 字典编码
ExcelExportTypeEnum:
  DOMAIN_MODE: 领域数据模型
  EXTERNAL_DATA: 外部服务变量
  VARIABLE_DATA_MODE: 变量空间数据模型
  ENGINE_VARS_DATA_MODE: 引擎变量数据模型
ExcelExportTypeEnum4DsServiceInterface:
  INTERFACE_REQUESTSTRUCTURE: 决策领域接口文档请求参数
  INTERFACE_RESPONSESTRUCTURE: 决策领域接口文档返回结果
OutsideServiceRespHeadEnum:
  OBJECT_HEAD: 对象
  SON_OBJECT_HEAD: 子对象
  ATTRIBUTE_NAME_HEAD: 属性名
  DESCRIPTION_HEAD: 中文描述
  TYPE_HEAD: 类型
  ARRAY_HEAD: 是否数组
  REMARKS_HEAD: 备注
OutsideServiceRespHeadRootEnum:
  ROOT_OBJECT_HEAD: 根对象
VariableModelHeadEnum:
  ROOT_OBJECT_HEAD: 根对象
  SON_OBJECT_HEAD: 子对象
  ATTRIBUTE_NAME_HEAD: 属性名
  DESCRIPTION_HEAD: 中文描述
  TYPE_HEAD: 类型
  ARRAY_HEAD: 是否数组
  EXTEND_HEAD: 是否扩展
  DICTIONARIES_HEAD: 字典编码
FunctionActionTypeEnum:
  ADD: 新建
  APPLY_UP: 申请上架
  DOWN: 下架
  UP: 上架
  APPROVED: 审核通过
  REFUSE: 审核拒绝
  RETURN_EDIT: 退回编辑
  DELETE: 删除
FunctionStatusEnum:
  EDIT: 编辑中
  UP: 已上架
  DOWN: 已下架
  UNAPPROVED: 待审核
  REFUSE: 审核拒绝
InterfaceFlowNodeTypeEnum:
  FLOW: 流程信息
  SPLIT: 条件分支
  PARALLEL: 并行分支
  SERVICE: 外数调用
  VAR: 变量加工
  PRE_PROCESS: 数据预处理
  INTERNAL_DATA: 内部数据获取
  INTERNAL_LOGIC: 内部逻辑计算
TestVariableTypeEnum:
  VAR: 变量
  FUNCTION: 公共函数
  INTERFACE: 变量服务接口
VariableActionTypeEnum:
  ADD: 新建
  APPLY_UP: 申请上架
  DOWN: 下架
  UP: 上架
  APPROVED: 审核通过
  REFUSE: 审核拒绝
  RETURN_EDIT: 退回编辑
VariableCompileMessageEnum:
  VAR_DEL: 使用的{0}[{1}]已删除
  VAR_ATTR_DISACCORD: 使用的{0}[{1}]的属性不一致
  VAR_REF_DEL: 定义的{0}{1}引用的对象[{2}]已删除
  VAR_REF_ATTR_DISACCORD: 定义的{0}{1}引用的对象[{2}]的属性不一致
  VAR_NOT_USE: 定义的{0}[{1}]未被使用
  VAR_NOT_INIT: 本地变量[{0}]没有初始化就使用
  FUNCTION_REF_NO_FOUND: 引用的{0}[{1}]已删除
  FUNCTION_REF_DEL: 引用的{0}[{1}]已删除
  VARIABLE_REF_DEL: 引用的变量[{0}]已删除
  VARIABLE_REF_OFF: 引用的变量[{0}]已下架
  VARIABLE_REF_CHECK: 流程缺少加工变量[{0}]
  VARIABLE_REF_List: 变量[{0}]已从变量清单移除
  PRE_REF_DEL: 引用的预处理逻辑[{0}]已删除
  PRE_REF_OFF: 引用的预处理逻辑[{0}]已下架
  INTERNAL_DATA_REF_DEL: 引用的内部数据[{0}]已删除
  ROSTER_DEL: 组件引用的名单类型[{0}]已删除
  OUTSIDE_REF_STRATEGY_DEL: 外部服务已删除
  OUTSIDE_REF_DISACCORD: 引入的外部服务[{0}]已删除
  OUTSIDE_REF_RECEIVING_OBJECT_MISSING: 引入的外部服务接收对象[{0}]不存在
VariableInterfaceCreationApproachEnum:
  NEW: 新建
  DUPLICATE: 复制已有
VariableInterfaceInputDataSourceTypeEnum:
  REQUEST: 外部传入
  OUTSIDE_SERVICE: 外数调用
  INTERNAL_DATA: 内部数据获取
  INTERNAL_LOGIC: 内部逻辑计算
VariableServiceInvokeMethodEnum:
  REST: 外部调用
  DOMAIN: 决策领域调用
  SIMULATE_STRATEGY: 策略模拟
VariableStatusEnum:
  EDIT: 编辑中
  UP: 已上架
  DOWN: 已下架
  UNAPPROVED: 待审核
  REFUSE: 审核拒绝
VarProcessExportNodeEnum:
  VARIABLE.label: 变量信息
  DATA_MODEL.label: 数据模型
  PREP.label: 预处理逻辑
  TEMPLATE.label: 变量模板
  FUNCTION.label: 公共方法
  OUTSIDE_SERVICE.label: 外部数据
  INTERNAL_DATA.label: 内部数据
  VARIABLE_SERVICE.label: 变量服务
  SPACE_CONFIG.label: 其他：变量分类、标签、缺失值、异常值配置
  DATA_FILE.label: 生成文件
  VARIABLE.desc: '{0}个变量，{1}个变量版本'
  DATA_MODEL.desc: '{0}个对象，{1}个对象版本'
  PREP.desc: '{0}个预处理逻辑'
  TEMPLATE.desc: '{0}个变量模板'
  FUNCTION.desc: '{0}个公共方法'
  OUTSIDE_SERVICE.desc: '{0}个引入的外部数据'
  INTERNAL_DATA.desc: '{0}个引入的内部数据'
  VARIABLE_SERVICE.desc: '{0}个变量服务，{1}个变量清单'
  SPACE_CONFIG.desc: 导出成功
  DATA_FILE.desc: 成功
VarProcessDataModelBoilerplateEnum:
  RAW_DATA: '{ "title": "rawData","description": "原始数据","type": "object" }'
  INTERNAL_DATA: '{ "title": "internalData","description": "内部数据","type": "object" }'
  EXTERNAL_DATA: '{ "title": "externalData","description": "外部服务数据","type": "object" }'
VarProcessInterfaceActionTypeEnum:
  CREATE: 新建
  APPLY_FOR_PUBLISH: 提交审核
  STEP_BACK: 退回编辑
  APPROVE: 审核通过
  REJECT: 审核拒绝
  DISABLE: 停用
  RE_ENABLE: 启用
  PUBLISH_IMPORT: 导入
  APPRO_ONLINE: 申请上线
  APPRO_REJECT: 审批拒绝
  APPRO_PASS: 审批通过
VarProcessInterfaceDeployApplyTypeEnum:
  SPACE.label: 变量空间
  SERVICE.label: 变量服务
  INTERFACE.label: 变量清单
  DATA_MODEL.label: 数据模型
  PREP.label: 预处理逻辑
  TEMPLATE.label: 变量模板
  FUNCTION.label: 公共方法
  DATA_FILE.label: 生成文件
  DATA_SHARE_DIRECTORY_FILE.label: 文件保存成功,保存路径
  SPACE.desc: 导出成功
  SERVICE.desc: '{0}个变量服务'
  INTERFACE.desc: '{0}个变量，1个调用流程图'
  DATA_MODEL.desc: '{0}个对象'
  PREP.desc: '{0}个预处理逻辑'
  TEMPLATE.desc: '{0}个变量模板'
  FUNCTION.desc: '{0}个公共方法'
  DATA_FILE.desc: 成功
  DATA_SHARE_DIRECTORY_FILE.desc: 成功
VarProcessInterfaceDeployParseImportLocationEnum:
  VARIABLE_SPACE: 空间导入
  VARIABLE_SERVICE: 服务导入
  AUTOMATIC: 自动部署
VarProcessInterfaceDocumentStatusCodeEnum:
  REST_200: 成功
  REST_400: 客户端错误，请求包含语法错误或无法完成请求
  REST_404: 网络异常
  REST_02_207_B_001: 服务调用流水号使用的变量没有传值
  REST_02_207_B_002: 空间不可用
  REST_02_207_B_003: 服务下无可用的变量清单接口
  REST_02_207_B_004: 无可用的服务信息
  REST_02_207_B_006: 服务调用流水号未传值
  REST_02_207_B_007: 组装input类数据异常
  REST_02_207_B_008: 保存数据异常
  REST_02_207_G_001: 未知异常
VarProcessLogResultStatusEnum:
  FAIL: 失败
  SUCCESS: 成功
VarProcessServiceTypeEnum:
  REAL_TIME: 实时
  BATCH: 批量
VarRestModuleErrorCode:
  SPACE_NOT_AVAILABLE: 空间不可用
  INTERFACE_NOT_AVAILABLE: 服务下无可用的变量清单接口
  SERVICE_NOT_AVAILABLE: 无可用的服务信息
  SERIALNO_ISNULL_ERROR: 服务调用流水号未传值
  INPUTJSON_FORMAT_ERROR: 组装input类数据异常
  DATA_SAVE_EXCEPTION: 保存数据异常
  UNKNOW_ERROR: 未知异常
RestModuleErrorCode:
  DOMAIN_NOT_AVAILABLE: 领域不可用
  SERVICE_NOT_AVAILABLE: 服务已停用
  SERVICE_INTERFACE_NOT_AVAILABLE: 服务没有可用的决策服务接口
  BUCKET_NOT_AVAILABLE: 服务下没有可用的细分
  SERVICE_REPORTCONFIG_NOT_AVAILABLE: 服务下没有可用的配置信息
  STRATEGY_NOT_AVAILABLE: 细分下没有可用的策略
  STRATEGYID_NOT_AVAILABLE: 策略ID不存在
  STRATEGYID_SERVICECODE_NOT_MATCH: 策略id和服务code不匹配
  STRATEGYID_SERVICECODE_MATCH_MORE: 细分下多条可用的策略，请联系相关人员
  SERVICE_CONFIG_NOT_MAPPING: 服务配置没有配置流水号/决策结果映射字段
  SERIALNO_ISNULL_ERROR: 服务调用流水号未传值
  REQUESTBODY_ISNULL_ERROR: 请求报文为空
  INPUTJSON_FORMAT_ERROR: 请求数据格式不正确，
  INPUTCLASS_NOT_IMPLEMENT: input类没有实现类！
  BUCKET_ROUTE_MATCH_EXCEPTION: 分流脚本执行异常
  DATA_SAVE_EXCEPTION: 保存数据异常
  DATA_SEARCH_EXCEPTION: 查询决策数据结果
  DATA_NOT_SEARCH_EXCEPTION: 未查询到决策数据结果
  DATA_PROCESSING_EXCEPTION: 查询流水号关联数据正在处理中
  RUNWITH_EXECUT_EXCEPTION: 陪跑策略执行异常
  UNKNOW_ERROR: 未知异常
ApiResultErrorEnum:
  DB_NOT_EXIST_ERR: db配置不存在
  CLICK_COMPLETE_ERR: 当前模型已经上线监测，存在数据，不能点击<完成并生效>按钮
  CLICK_SAVE_ERR: 当前模型需要重新建表，只能点击<完成并生效>按钮
  UPDATE_ERR: 当前模型已经上线监测，存在数据，只能修改<中文描述>和<目标变量定义>
  MODEL_NOT_EXIST_ERR: 模型不存在，请刷新
JoinEnum:
  INNER_JOIN: 内连接
  LEFT_JOIN: 左连接
  RIGHT_JOIN: 右连接
  FULL_JOIN: 全连接
  CROSS_JOIN: 交叉连接
LastExecuteStateEnum:
  SUCCESS: 成功
  FAIL: 失败
  EXECUTING: 执行中
ContainsTypeEnum:
  RIGHT_CONTAINS: 右包含
  LEFT_CONTAINS: 左包含
DatasetTypeEnum:
  PRODUCT: 生产
  PRODUCT_PERFORMANCE: 生产+表现
DataSourceEnum:
  LOCAL_PRODUCT: 本地生产库
  REMOTE_PRODUCT: 远程生产库
GlobalEnum:
  SUCCESS: 成功
  BAD_REQUEST: 请求错误
  NOT_FOUND: 资源不存在
  REQ_TIME_OUT: 请求超时
  FAILURE: 系统异常
  CONFIRM: 确认是否操作？
  WARN: 业务提醒
  BIZ_EXCEPTION: 业务异常
  TEMPLATE_ARRAY_LOOP: 表达式双层数组循环
  LICENSE_INVALID: License授权已过期
GroupModeEnum:
  ENUM: 枚举
  INTERVAL: 区间
OperationButtonEnum:
  EDIT: 编辑
  DELETE: 删除
  ANALYSE: 分析
ReportDimensionTypeEnum:
  STRATEGY: 策略维度
  GENERAL: 一般维度
  DATETIME: 时间维度
SimDatasetImportStatusEnum:
  NOT_STARTED: 未开始
  IN_PROGRESS: 进行中
  FAIL: 失败
  SUCCESS: 成功
BizCodeMessageEnum:
  COMPONENT_VALIDATOR_CONFIRM: 组件验证警告
  CHANGE_NUM_UPDATED: 该组件版本已更新，是否自动获取新版本？
  CONFIRM_DELETE: 确认删除该组件？
  UNABLE_DELETE: 该组件已被使用，不允许删除！
  EDIT_UNABLE_DELETE: 该组件正在被其他用户编辑，不允许删除！
  COMPONENT_EDIT_CHECK_STRATEGY: 该策略不属于开发状态，不允许{0}！
  EXIST_COMPONENT: 该目录下已有组件，不允许删除！
  CONFIRM_DELETE_DIR: 确认删除该目录？
  EXIST_DIR: 该目录下已有下级目录，不允许删除！
  PERMISSION_TERM_DEL_EXIST_CONFIRM: 该用户在领域内存在未检入的组件，确认移除？
  PERMISSION_TERM_DEL_NO_EXIST_CONFIRM: 确认移除该用户？
  DS_SERVICE_STOP_EXIST_CONFIRM: 该服务下存在策略，确认停用？
  DS_SERVICE_STOP_NO_EXIST_CONFIRM: 确认停用该服务？
  DS_SERVICE_DEL_EXIST_REJECT: 该服务下存在非开发状态的策略，不允许删除!
  DS_SERVICE_DEL_EXIST_CONFIRM: 该服务下存在开发状态的策略，确认删除？
  DS_SERVICE_DEL_NO_EXIST_CONFIRM: 确认删除该服务？
  BUCKET_USE_STOP_EXIST_CONFIRM: 该细分下存在策略，确认停用？
  BUCKET_USE_STOP_NO_EXIST_CONFIRM: 确认停用该细分？
  BUCKET_USE_DEL_EXIST_REJECT: 该细分下存在非开发状态的策略，不允许删除!
  BUCKET_USE_DEL_EXIST_CONFIRM: 该细分下存在开发状态的策略，确认删除？
  BUCKET_USE_DEL_NO_EXIST_CONFIRM: 确认删除该细分？
  ABTEST_PLAN_DEL_EXIST_REJECT: 该方案已被策略使用，不允许删除!
  ABTEST_PLAN_DEL_NO_EXIST_CONFIRM: 确认删除该方案？
  ABTEST_PLAN_DEV_DEL_NO_EXIST_CONFIRM: 该方案已被开发状态的策略使用，确认删除？
  ABTEST_PLAN_UP_NO_EXIST_CONFIRM: 该方案已被策略使用，将限制可编辑的内容，确认编辑？
  ABTEST_PLAN_UP_CODE_EXIST_REJECT: 方案编码不能修改！
  ABTEST_PLAN_UP_RANDOM_EXIST_REJECT: 引用的随机数不能修改！
  ABTEST_PLAN_NOT_FOUND_REJECT: 方案 ID 不存在。
  ABTEST_GROUP_UP_CODE_EXIST_REJECT: 分组编码不能修改！
  ABTEST_GROUP_UP_NAME_EXIST_REJECT: 分组名称不能修改！
  ABTEST_GROUP_UP_TYPE_EXIST_REJECT: 分组类型不能修改！
  ABTEST_GROUP_UP_DESC_EXIST_REJECT: 分组描述不能修改！
  ABTEST_RANDOM_DEFINITION_DEL_NO_EXIST_CONFIRM: 确认删除该随机数定义？
  ABTEST_RANDOM_DEFINITION_DEL_EXIST_REJECT: 该随机数已被A/B测试方案使用，不允许删除!
  ABTEST_RANDOM_DEFINITION_UP_EXIST_REJECT: 该随机数已被A/B测试方案使用，不允许编辑!
  STRATEGY_PARAM_UP_EXIST_REJECT: 该策略参数已被策略使用，不可编辑!
  STRATEGY_PARAM_DEL_EXIST_REJECT: 该策略参数已被策略使用，不允许删除!
  STRATEGY_PARAM_STATUS_DEV_DEL_EXIST_CONFIRM: 该策略参数已被开发状态的策略使用，确认删除？
  STRATEGY_PARAM_DEL_EXIST_CONFIRM: 确认删除该策略参数？
  DOMAIN_ROSTER_UP_EXIST_REJECT: 该策略参数已被策略使用，不允许编辑!
  DOMAIN_ROSTER_DEL_EXIST_REJECT: 该名单类型下已有名单数据，不允许删除!
  DOMAIN_ROSTER_DEL_EXIST_CONFIRM: 确认删除该名单类型？
  DOMAIN_ROSTER_DEL_STATUS_DEV_EXIST_CONFIRM: 该名单类型已被开发状态的策略使用，确认删除？
  DOMAIN_ROSTER_DEL_STATUS_EXIST_REJECT: 该名单类型已被策略使用，不允许删除!
  DOMAIN_ROSTER_DEL_OUT_TREAS_CONFIRM: 删除后不可恢复，确认删除？
  DOMAIN_DEL_EXIST_REJECT: 该领域下存在决策服务，不允许删除!
  DOMAIN_DEL_STATUS_DEV_EXIST_CONFIRM: 确认删除该领域？
  DOMAIN_DATA_MODEL_UP_EXIST_REJECT: 该数据模型已被非开发状态的策略使用，不允许编辑!
  DOMAIN_DATA_MODEL_DEL_STATUS_DEV_EXIST_CONFIRM: 确认删除该数据模型？
  DOMAIN_DATA_MODEL_DEL_STATUS_EXIST_REJECT: 该数据模型已被策略使用，不允许删除!
  DOMAIN_DICT_DEL_USED_BY_MODEL_REJECT: 该字典类型已被使用，不允许删除。
  DOMAIN_DICT_DEL_DEV_EXIST_CONFIRM: 该字典类型下已有字典项，确认删除？
  DOMAIN_DICT_NOT_DETAIL_DEL_EXIST_CONFIRM: 确认删除该字典类型？
  STRATEGY_ENGINE_DICT_DEL_USED_BY_ENGINE_VARS_REJECT: 该字典类型已被使用，不允许删除。
  STRATEGY_ENGINE_DICT_DEL_DEV_EXIST_CONFIRM: 该字典类型下已有字典项，确认删除？
  STRATEGY_ENGINE_NOT_DETAIL_DEL_EXIST_CONFIRM: 确认删除该字典类型？
  OUTSIDE_SERVICE_RELEASED_CONFIG: 确认启用该外部服务？
  OUTSIDE_SERVICE_DEL_EXIST_REJECT: 该外数已被使用，不允许删除。
  OUTSIDE_SERVICE_STOP_DEL_EXIST_CONFIRM: 该外部服务已经被领域引用，确认停用？
  OUTSIDE_SERVICE_STOP_CANCEL_DEL_EXIST_CONFIRM: 确认停用该外部服务？
  OUTSIDE_SERVICE_REQ_IN_REQUEST_URL_EXIST_REJECT: 接口地址参数不可用！
  OUTSIDE_SERVICE_REQ_UPSERT_REQ_DATA_EXIST_REJECT: 该服务中已经存在请求数据了。
  OUTSIDE_SERVICE_REQ_PARAM_NOT_FOUND_IN_BODY_PARAM_REJECT: 请求参数模板中的${参数名}必须为Body中定义的参数名，且必须属于Body定义的范围。
  OUTSIDE_SERVICE_REQ_PARAM_NOT_FOUND_IN_URL_PARAM_REJECT: 请求链接中的${参数名}必须为url参数中定义的参数名，且必须属于url参数定义的范围。
  OUTSIDE_SERVICE_RESP_UP_EXIST_CONFIRM: 修改响应参数可能造成已引用该外部服务的策略无法使用，确认保存？
  OUTSIDE_SERVICE_RESP_NOT_EXIST_REJECT: 响应参数未定义！
  OUTSIDE_SERVICE_RESP_UPSERT_RESP_STRUCT_EXIST_REJECT: 该服务中已经存在响应数据结构了。
  OUTSIDE_SERVICE_RESP_REF_UP_EXIST_CONFIRM: 修改响应参数可能造成已引用该外部服务的策略无法使用，确认保存？
  OUTSIDE_SERVICE_MOCK_INSERT_EXIST_REJECT: 该领域已经存在有效的数据集，不允许重复添加。
  OUTSIDE_SERVICE_MOCK_VARIABLE_INSERT_EXIST_REJECT: 该变量空间已经存在有效的数据集，不允许重复添加。
  OUTSIDE_SERVICE_MOCK_UPSERT_NAME_OCCUPIED_REJECT: Mock新名称已被占用。
  OUTSIDE_SERVICE_MOCK_UPDATE_EXIST_ENABLED_MOCK_REJECT: 当前作用领域下已存在启用的Mock。
  OUTSIDE_SERVICE_MOCK_HAVE_DETAIL_DEL_EXIST_CONFIRM: 该数据集下已有明细数据，确认删除？
  OUTSIDE_SERVICE_MOCK_DEL_EXIST_CONFIRM: 确认删除该数据集？
  SYS_PARAM_UP_EXIST_REJECT: 该参数已被使用，不可编辑！
  SYS_PARAM_DEL_EXIST_CONFIRM: 确认删除该参数？
  SYS_PARAM_DEL_EXIST_REJECT: 该参数已被使用，不可删除！
  SYS_PARAM_IN_PARAM_DEL_EXIST_REJECT: 参数类型为内置参数类型的系统参数不能删除！
  STRATEGY_DEL_REJECT: 策略是非开发状态，不允许删除
  STRATEGY_DEL_CONFIRM: 确认删除该策略？
  STRATEGY_TEST_CHECK_REJECT: 不允许提交测试！
  STRATEGY_TEST_CONFIRM: 提交测试后该策略将不可修改，确认提交？
  STRATEGY_START_TEST_CHECK_REJECT: 不允许进行测试！
  STRATEGY_APPLY_ONLINE_CHECK_REJECT: 不允许申请上线！
  STRATEGY_APPLY_ONLINE_RESOURCE_REJECT: 该策略归属的决策服务没有已发布的决策服务接口，不允许申请上线！
  STRATEGY_APPOVAL_ONLINE_CHECK_REJECT: 不允许上线！
  STRATEGY_APPOVAL_ONLINE_RESOURCE_REJECT: 该策略归属的决策服务没有已发布的决策服务接口，不允许上线！
  STRATEGY_BACK_USE_REJECT: 该策略找不到可以回退的历史版本，不允许回退！
  STRATEGY_BACK_RESOURCE_REJECT: 该策略归属的决策服务没有已发布的决策服务接口，不允许回退！
  STRATEGY_ALL_ONLINE_RESOURCE_CONFIRM: 该策略归属的决策服务没有已发布的决策服务接口，不允许完整上线！
  STRATEGY_ALL_ONLINE_CONFIRM: 完整上线后该策略将变为生产状态，原生产策略下线，确认操作？
  STRATEGY_OFFLINE_TEST_CONFIRM: 下线后该策略将不可用，确认操作？
  STRATEGY_OFFLINE_REJECT: 所在细分下存在”灰度/陪跑“状态的策略，不允许下线
  STRATEGY_OFFLINE_WAIT_REJECT: 所在细分下存在等待上线的”灰度/陪跑“的策略，不允许下线
  STRATEGY_OFFLINE_CONFIRM: 下线后该细分下将没有可用的生产策略，确认操作？
  STRATEGY_ONLINE_RESOURCE_REJECT: 该策略归属的决策服务没有已发布的决策服务接口，不允许上线！
  STRATEGY_ONLINE_CONFIRM: 确认将该策略进行上线部署？
  STRATEGY_ONLINE_PULISH_SERVICE_REJECT: 该策略归属的决策服务不为”启用“状态，无法上线发布！
  STRATEGY_ONLINE_PULISH_BUCKET_REJECT: 该策略归属的决策细分不为”启用“状态，无法上线发布！
  STRATEGY_ONLINE_PULISH_CONFIG_REJECT: 该策略归属的决策服务的数据入库配置不完整，无法上线发布！
  STRATEGY_ONLINE_PULISH_CONFIRM: 该策略归属的决策服务下还存在其他待上线的策略，将会同时发布，确定继续？
  STRATEGY_PUBLISH_RESOURCE_NOT_FOUND_REJECT: 该决策服务部署环境还未分配，不能申请上线。
  COMMON_SERVICE_STOP_REF_REJECT: 该模块已被决策服务下的策略引用，不允许停用！
  COMMON_SERVICE_STOP_EXIST_CONFIRM: 该模块下存在策略，确认停用？
  COMMON_SERVICE_STOP_NO_EXIST_CONFIRM: 确认停用该模块？
  COMMON_SERVICE_DEL_EXIST_REJECT: 该模块已被决策服务下的策略引用，不允许删除！
  COMMON_SERVICE_DEL_EXIST_CONFIRM: 该模块下存在策略，确认删除？
  COMMON_SERVICE_DEL_NO_EXIST_CONFIRM: 确认删除该模块？
  COMMON_BUCKET_USE_STOP_REF_REJECT: 该细分已被决策服务下的策略引用，不允许停用！
  COMMON_BUCKET_USE_DEL_REF_REJECT: 该细分已被决策服务下的策略引用，不允许删除!
  COMMON_BUCKET_USE_DEL_CONFIRM: 该细分下存在策略，确认删除？
  COMMON_STRATEGY_APPLY_CHECK_REJECT: 不允许申请发布！
  COMMON_STRATEGY_APPLY_APPOVAL_CONFIRM: 确认提交审批？
  COMMON_STRATEGY_APPOVAL_CHECK_REJECT: 不允许发布！
  COMMON_STRATEGY_STOP_CHECK_REJECT: 该策略已被决策服务下的策略引用，不允许停用！
  COMMON_STRATEGY_STOP_CHECK_CONFIRM: 停用后该策略将不可用，确认操作？
  COMMON_STRATEGY_REPUBLISH_CHECK_REJECT: 不允许重新发布！
  COMMON_STRATEGY_REPUBLISH_CHECK_CONFIRM: 确认将该策略重新发布？
  STRATEGY_OUTSIDE_COMMON_REF_DEL_CONFIRM: 确认删除该引用对象？
  STRATEGY_OUTSIDE_COMMON_REF_DEL_EXIST_CONFIRM: 该引用对象已被使用，确认删除？
  DS_SERVICE_INTERFACE_START_CONFIRM: 发布后将创建部署环境资源，确认发布？
  DS_SERVICE_INTERFACE_START_INTERFACE_CAUSED_REJECT: 该接口已发布，不允许再次发布
  DS_SERVICE_INTERFACE_START_SERVICE_CAUSED_REJECT: 该接口对应的决策服务已停用，不允许发布
  DS_SERVICE_INTERFACE_DELETE_CONFIRM: 确认删除该决策服务接口？
  DS_SERVICE_INTERFACE_STOP_CONFIRM: 确认停用该决策服务接口？
  DS_SERVICE_INTERFACE_STOP_PRODUCING_STRATEGY_CAUSED_REJECT: 该服务下存在生产策略，不允许停用
  DS_SERVICE_INTERFACE_STOP_RELEASING_STRATEGY_CAUSED_REJECT: 该服务下存在上线中的策略，不允许停用
  DS_SERVICE_INTERFACE_STOP_REQUIRED_ONE_RELEASED_INTERFACE_REJECT: 决策服务下要至少存在一个发布接口，不允许停用。
  DS_SERVICE_INTERFACE_RESTART_CONFIRM: 确认重新发布该决策服务接口？
  DS_SERVICE_INTERFACE_RESTART_REJECT: 该接口对应的决策服务已停用，不允许重新发布
  TEMPLATE_COMPARE_OBJECTPROPERTY: 没有可匹配的数据，不能赋值
  TEMPLATE_COMPARE_OBJECTPROPERTY_NOTMATCH: 对象下的数据[{0}}]的数据类型/是否数组属性不一致，不能赋值
  STATISTICAL_CONFIG_CONFIRM: 变更结果类型会清理历史结果数据，确认变更吗？
  TRACE_DATAGRAM_NOT_FOUND_REJECT: 未查询到 trace 报文。
  TRACE_VISUALIZATION_COMPONENT_CONTENT_NOT_FOUND_REJECT: 未找到 trace 记录时对应的组件内容。
  BLAZE_DATA_MODEL_REFERENCED_STRATEGY_ENABLE_MODIFY_DISABLE_REJECT: 策略启用之后，不允许修改
  BLAZE_DATA_MODEL_REFERENCED_STRATEGY_ENABLE_DELETE_DISABLE_REJECT: 策略启用之后，不允许删除
  BLAZE_STRATEGY_REFERENCED_BY_DS_STRATEGY_DELETE_DISABLE_REJECT: 该策略已被决策服务下的策略引用，不允许删除
  BLAZE_STRATEGY_REFERENCED_BY_DEV_STATUS_DS_STRATEGY_CONFIRM: 该策略已被开发状态的的策略使用，确认删除？
  BLAZE_STRATEGY_REFERENCED_DATA_MODEL_NOT_AVAILABLE_SUBMIT_TEST_REJECT: 该策略引用的数据模型不可用，不允许提交测试
  BLAZE_STRATEGY_SUBMIT_TEST_CONFIRM: 提交测试后该策略将不可修改，确认提交？
  BLAZE_STRATEGY_REFERENCED_DATA_MODEL_NOT_AVAILABLE_SUBMIT_VERIFY_REJECT: 该策略引用的数据模型不可用，不允许提交审核
  BLAZE_STRATEGY_REFERENCED_DATA_MODEL_NOT_AVAILABLE_SUBMIT_VERIFY_REFUSE: 该策略引用的数据模型不可用，不可提交审批
  BLAZE_STRATEGY_SUBMIT_CONFIRM: 确认提交该策略？
  BLAZE_STRATEGY_TO_TEST_CONFIRM: 确认将该策略退回开发状态？
  BLAZE_SERVICE_CONTAINS_ENABLE_STRATEGY_CONFIRM: 启用当前策略，该模块下的启用策略将被停用，是否确定？
  BLAZE_STRATEGY_REPUBLISH_CONFIRM: 确认将该策略重新发布？
  BLAZE_STRATEGY_DISABLE_CONFIRM: 确认停用该策略？
  BLAZE_STRATEGY_REFERENCED_BY_DS_STRATEGY_DISABLE_CONFIRM: 该策略已被开发状态的的策略使用，确认停用？
  BLAZE_STRATEGY_USED_BY_DS_STRATEGY_CONFIRM: 该策略已被策略使用，确认停用？
  ABTEST_GROUP_CODE_EXIST: 分组编码已存在
  ABTEST_GROUP_NAME_EXIST: 分组名称已存在
  ABTEST_GROUP_ID_NOT_EXIST: 分组ID不存在
  ABTEST_GROUP_INFO_EMPTY: 分组信息不能为空
  ABTEST_GROUP_NAME_EMPTY: 分组名称不能为空
  ABTEST_GROUP_CODE_EMPTY: 分组编码不能为空
  ABTEST_GROUP_NAME_RULE_INVALID: 分组名称不能超过50个字符
  ABTEST_GROUP_CODE_RULE_INVALID: 分组编码不能超过50个字符
  ABTEST_GROUP_DESC_RULE_INVALID: 分组描述不能超过100个字符
  ABTEST_GROUP_INTERVAL_EMPTY: 分组区间信息不能为空
  ABTEST_GROUP_MULTI_PLANS: 组中出现多个不同的测试方案ID
  ABTEST_GROUP_INTERVAL_MULTI_PLANS: 分组区间中出现多个不同的测试方案
  ABTEST_GROUP_INTERVAL_DIFFERENT_PLANS: 分组和分组区间中出现不同的方案
  ABTEST_GROUP_REFERENCED_BY_INTERVAL: 分组被区间配置引用不能删除，必须先删除被引用的区间
  ABTEST_GROUP_CHAMPION_MISS: 分组中缺少冠军分组
  ABTEST_GROUP_CHAMPION_ONLY: 分组中只能有一个冠军分组
  ABTEST_GROUP_CHALLENGE_MORE: 分组中最少有一个挑战者分组
  ABTEST_PLAN_ID_NOT_EXIST: 方案ID不存在
  ABTEST_PLAN_RANDOM_ID_NOT_EXIST: 方案中的随机数ID不存在
  ABTEST_PLAN_RANDOM_LEN_NOT_EXIST: 随机数长度不存在
  ABTEST_GROUP_INTERVAL_MIN_RULE: 分组区间开始最小值必须等于0
  ABTEST_GROUP_INTERVAL_MAX_RULE: 分组区间开始最小值必须等于0
  ABTEST_GROUP_NAME_SELECTED: 请选择[{}-{}]区间的分组名称
  ABTEST_GROUP_INTERVAL_ID_NOT_EXIST: '{0} 分组区间id不存在'
  ABTEST_GROUP_INTERVAL_VALUE_INTERMITTENT: '{0}分组{1}区间值与{2}出现间断'
  ABTEST_GROUP_INTERVAL_VALUE_OVERLAP: 分组{0}区间值与{1}出现重叠
  ABTEST_PLAN_DOMAIN_NO_DATA: 该领域下没有数据模型
  ABTEST_PLAN_RANDOM_NOT_EXIST: 随机数定义Id不存在
  ABTEST_PLAN_CODE_EXIST: 方案编码已经存在
  ABTEST_PLAN_NAME_EXIST: 方案名称已经存在
  ABTEST_PLAN_INFO_NOT_EXIST: 方案信息不存在
  ABTEST_PLAN_AB_NOT_EXIST: 未查询到ab方案
  ABTEST_PLAN_RANDOM_INFO_NOT_EXIST: 方案中的随机数信息不存在
  ABTEST_PLAN_RANDOM_TYPE_NOT_EXIST: 随机数类型不存在
  ABTEST_PLAN_RANDOM_LEN_TYPE_NOT_EXIST: 随机数长度类型不存在
  ABTEST_RANDOM_DOMAIN_MULTI: 随机数定义中不能出现多个领域id
  ABTEST_RANDOM_NAME_EXIST: 随机数名称已存在
  ABTEST_RANDOM_ID_NOT_EXIST: 随机数id不存在
  ABTEST_RANDOM_NOT_MODIFIED: 随机数已经被方案引用不能修改
  ABTEST_RANDOM_DOMAIN_ID_EMPTY: 领域ID不能为空
  ABTEST_RANDOM_NAME_EMPTY: 随机数名称不能为空
  ABTEST_RANDOM_NAME_RULE_INVALID: 随机数名称不能超过50个字符
  ABTEST_RANDOM_LEN_NOT_EXIST: 随机数长度不存在
  ABTEST_RANDOM_TYPE_NOT_EXIST: 随机数类型不存在
  ABTEST_RANDOM_DOMAIN_ID_NOT_EXIST: 领域id不存在
  BLAZE_MODEL_PARAM_EMPTY: 参数不能为空
  BLAZE_MODEL_READ_FILE_ERROR: 读取blaze xsd文件异常
  BLAZE_MODEL_PARSE_FILE_ERROR: 解析blaze xsd文件异常
  BLAZE_MODEL_NOT_EXIST: 数据模型状态不存在
  BLAZE_MODEL_VERSION_ERROR: 数据模型版本号不正确
  BLAZE_MODEL_VERSION_EMPTY: 版本号不能为空
  BLAZE_MODEL_DATA_NOT_EXIST: 数据模型id数据不存在
  BLAZE_MODEL_DATA_EXPORT_ERROR: 导出blaze数据模型Excel数据异常
  BLAZE_MODEL_SERVICE_NOT_EXIST: 决策服务不存在
  BLAZE_MODEL_SERVICE_ID_EMPTY: 决策服务ID不能为空
  BLAZE_MODEL_PERMISSION_CODE_EMPTY: 权限资源编码不能为空
  BLAZE_MODEL_REPLACE_FILE_ERROR: 替换xsd输入输出处理异常
  BLAZE_MODEL_TYPE_NOT_SUPPORTED: 不支持的操作类型
  BLAZE_MODEL_FILE_NAME_EMPTY: 文件名不能为空
  BLAZE_MODEL_FILE_NOT_SUPPORTED: 不支持除.xsd以外的其他文件扩展名
  BLAZE_SERVICE_ATTR_NOT_SUPPORTED: 不持支获取此类型的属性列表
  BLAZE_STRATEGY_NOT_EXIST: blaze策略ID不存在
  BLAZE_SERVICE_STATUS_ERROR: 状态不正确！1：启用 2：停用
  BLAZE_SERVICE_TYPE_ERROR: 决策服务类型不正确
  BLAZE_STRATEGY_OPERATE_ERROR: 策略操作类型传入错误
  BLAZE_STRATEGY_NOT_SUPPORTED: 暂不支持其他策略操作类型
  BLAZE_STRATEGY_DEVELOPING_DEL_ERROR: 不能删除非开发状态的策略
  BLAZE_STRATEGY_OBJECT_NAME_REPEAT: 同一个策略下接收对象名称不能重复
  BLAZE_STRATEGY_OBJECT_NAME_CN_REPEAT: 同一个策略下接收对象中文名称不能重复
  BLAZE_STRATEGY_ENABLE_NONE: blaze决策模块下不存在启用状态的blaze策略
  BLAZE_SERVICE_REFERENCE_ID_NOT_EXIST: blaze决策服务引入id不存在
  BLAZE_SERVICE_NAME_EMPTY: 服务名称不能为空
  BLAZE_SERVICE_CODE_EMPTY: 服务编码不能为空
  BLAZE_SERVICE_CODE_NAME_REPEAT: 编码或名称不允许重复
  BLAZE_DOMAIN_DEL: 领域已被删除
  BLAZE_SERVICE_INIT_ERROR: 初始化blaze决策服务权限时决策服务主键ID不能为空
  BLAZE_SERVICE_ADB_READ_ERROR: 读取ADB文件异常
  BLAZE_SERVICE_ADB_CONTENT_EMPTY: ADB文件内容为空
  BLAZE_DOMAIN_ID_EMPTY: 数据模型ID不能为空
  BLAZE_DOMAIN_NOT_EXIST: 数据模型不存在
  BLAZE_DOMAIN_LIST_NOT_EXIST: blaze数据模型关联关系不存在
  BLAZE_STRATEGY_SEND_FAIL: 发送blaze策略启用消息到channel负载解析失败
  BLAZE_SERVICE_UNABLE: 通知blaze-rest模块已停用/模块下的所有策略都已经停用了，服务ID不存在！
  BLAZE_STRATEGY_REF_ID_EMPTY: 引用ID不存在
  BLAZE_STRATEGY_START_FAIL: 启用blaze策略：策略ID不能为空
  BLAZE_SERVICE_ADB_DOWNLOAD_EXP: 下载blaze策略ADB文件异常
  BLAZE_MODEL_PERMISSION_DATA_EMPTY: 权限数据不能为空
  CODE_ANALYSIS_TASK_GEN_FAIL: Java 代码检查报告生成失败
  INPUT_PARAM_EMPTY: 入参数据类型不能为空
  COMMON_SERVICE_SAME_BUCKET: 进行版本编辑的策略应属于相同的细分
  ORIGIN_STRATEGY_DOMAIN_MODEL_NOT_EXIST: 未查询到原决策模块的数据模型
  ORIGIN_STRATEGY_DOMAIN_MODEL_NONE: 未查询到原数据模型
  COMPONENT_TEST_PARAM_FAIL: 测试参数处理出错
  DECISION_FLOW_TRACE_FAIL: 节点类型信息缺失, 请检查 trace 数据完整性
  DECISION_FLOW_TRACE_NODE_MISS: 未找到主流程或决策流节点 trace 信息
  DECISION_FLOW_COMP_TYPE_NOT_SUPPORT: 不支持的组件类型，请重新检查决策流配置
  DECISION_FLOW_TRACE_NOT_EXIST: 正在查看的节点未命中，trace 信息不存在
  DECISION_FLOW_COMP_TRACE_NONE: 未查询到组件测试 trace 日志信息
  DECISION_FLOW_TRACE_NOT_LOOP: 正在查看 trace 的节点不属于循环节点
  DECISION_FLOW_TRACE_NOT_PACKAGE: 正在查看 trace 的节点不属于决策包节点
  DECISION_FLOW_TRACE_NO_NEED_SNAPSHOT: 决策数据查询 trace 信息查看不需要查询快照
  DECISION_FLOW_TRACE_NODE_NOT_SUPPORT: 暂不支持其他类型节点 trace
  DECISION_FLOW_TRACE_PACKAGE_NOT_FOUND: 未找到决策包节点执行的 trace 信息
  DECISION_FLOW_TRACE_TABLE_NOT_FOUND: 不支持的决策表类型
  DECISION_FLOW_TRACE_RULE_NOT_SUPPORT: 暂不支持除规则集（单规则）、自定义函数、参数表、决策表、决策树、评分卡和 PMML 模型以外其他类型的组件
  DECISION_FLOW_TRACE_BIN_INCOMPLETE: 分箱值不完整, 请检查数据完整性
  DECISION_FLOW_TRACE_PAGE_NUM_ERR: 分页设置不正确：页码应大于 0
  DECISION_FLOW_TRACE_PAGE_COUNT_ERR: 分页设置不正确：页面记录数量应大于 0
  DECISION_FLOW_TRACE_NODE_TYPE_NOT_SUPPORT: 节点类型不支持，请检查trace信息
  STRATEGY_DEPLOYING_EXIST: 当前领域存在发布中的策略记录
  SYS_TARGET_NOT_EXIST: 目标服务器不存在或参数错误
  STRATEGY_NOT_FOUND: 未查询到策略
  STRATEGY_DEPLOY_FAIL: 发布失败
  STRATEGY_DEPLOY_RECORD_NOT_EXIST: 发布记录不存在
  STRATEGY_DEPLOY_RECORD_NOT_FOUND: 未查询到发布记录
  STRATEGY_FILE_DOWNLOAD_FAIL: 下载文件失败
  STRATEGY_DEPLOY_SYNC_FAIL: 不是一键发布，无法同步目标环境
  STRATEGY_DEPLOY_DATA_SYNC_FAIL: 数据同步失败
  STRATEGY_DEPLOY_SFTP_FAIL: 上传sftp失败
  STRATEGY_DEPLOY_WRITE_FILE_FAIL: 写入文件内容失败
  DEPLOY_ENV_NOT_SUPPORT: 目标环境不是分析/允许环境，无法上线发布
  DEPLOY_ENV_USER_NOT_CONFIG: 目标环境未配置当前用户，无法上线发布
  DEPLOY_ENV_STRATEGY_EXIST: 目前环境已存在该策略，不允许重复发布
  DEPLOY_ENV_ONGOING_WAIT: 该领域有用户正在发布上线，请稍后再试
  DEPLOY_ENV_FILE_SYNC_FAIL: 文件同步失败
  DEPLOY_ENV_RECORD_NOT_FOUND: 未查询到发布记录
  DOMAIN_NUM_REPEAT: 决策领域编号不允许重复
  DOMAIN_NAME_REPEAT: 决策领域名称不允许重复
  DOMAIN_NAME_NOT_EXIST: 修改决策领域编号不存在
  DOMAIN_NUM_NOT_MODIFIED: 只能修改领域名称和描述，不能修改领域编码
  DOMAIN_NAME_UPDATE_REPEAT: 修改决策领域名称不允许重复
  DOMAIN_ID_UPDATE_REPEAT: 修改决策领域id不存在
  DOMAIN_INFO_NOT_EXIST: 决策领域信息不存在
  DOMAIN_BUCKET_NAME_REPEAT: 细分名称已存在，不允许重复
  DOMAIN_BUCKET_NOT_SUBMIT: 已删除的细分正在被使用，不能提交
  DOMAIN_BUCKET_NOT_DELETED: 默认决策细分不能删除
  DOMAIN_BUCKET_COMPILE_FAIL: 细分分流实现类编译失败
  DOMAIN_BUCKET_GRAY_RATIO_HUNDRED: 灰度流量比例总和不等于100%，请重新分配
  DOMAIN_BUCKET_FLOW_RATIO_HUNDRED: 陪跑流量比例不等于100%，请重新分配
  DOMAIN_BUCKET_REFERENCING: 当前公共决策模块的决策细分正在被引用，无法停用
  DOMAIN_BUCKET_NEW_FAIL: 领域信息异常，无法新建决策服务
  DOMAIN_ENV_PARAM_NOT_CONFIG: 系统参数项”当前环境“未配置，请检查
  DOMAIN_MODEL_ID_NOT_EXIST: 数据模型id不存在
  DOMAIN_MODEL_VERSION_EXIST: 修改领域下的数据模型数据版本已经存在
  DOMAIN_MODEL_INPUT_EMPTY: 输入数据模型值不能为空
  DOMAIN_MODEL_OUTPUT_EMPTY: 输出数据模型值不能为空
  DOMAIN_MODEL_COPY_VERSION_NOT_EXIST: 选择复制的数据模型版本不存在
  DOMAIN_MODEL_VERSION_TYPE_EMPTY: 版本类型不能为空
  DOMAIN_MODEL_NOT_EXIST: 未找到数据模型
  DOMAIN_MODEL_STATUS_NOT_EXIST: 数据模型状态不存在
  DOMAIN_MODEL_SHEET_NAME_ERR: 读取决策数据模型excel的sheetName异常
  DOMAIN_MODEL_SHEET_NAME_EMPTY: excel中的sheetName为空
  DOMAIN_MODEL_SHEET_NAME_RULE: excel中的sheetName只能是input和output
  DOMAIN_MODEL_SHEET_NAME_VAR_RULE: excel中的sheetName只能是engineVars
  DOMAIN_MODEL_SHEET_TITLE_ERR: 读取决策数据模型excel的表头异常
  DOMAIN_MODEL_SHEET_TITLE_EMPTY: sheet页{0}的表头不能为空
  DOMAIN_MODEL_SHEET_DATA_ERR: 读取决策数据模型excel的数据异常
  DOMAIN_MODEL_SHEET_DATA_EMPTY: sheet页{0}的数据不能为空
  DOMAIN_MODEL_VAR_NOT_EXIST: 导变量类型不存在
  DOMAIN_MODEL_STRATEGY_ID_EMPTY: 数据模型 ID 和策略 ID 不能同时为空
  DOMAIN_STRATEGY_ID_NOT_EXIST: 策略id不存在
  DOMAIN_MODEL_VERSION_ERR: 版本类型错误
  DOMAIN_MODEL_COMPARE_VERSION_RULE: 对比版本的数据模型必须等于2个
  DOMAIN_MODEL_NO_DATA_EXIST: 存在没有数据的数据模型id
  DOMAIN_MODEL_IN_SERVICE_NOT_EXIST: 未查询到决策服务模块的数据模型
  DOMAIN_MODEL_VERSION_EMPTY: 数据模型版本号不能为空
  DOMAIN_DIC_PARENT_NOT_EXIST: 父级字典项不存在
  DOMAIN_DIC_CODE_REPEAT: 字典项编码不能重复
  DOMAIN_DIC_NAME_REPEAT: 字典项名称不能重复
  DOMAIN_DIC_ID_NOT_EXIST: 字典项id数据不存在
  DOMAIN_DIC_DATA_NOT_EXIST: 字典项数据不存在
  DOMAIN_ID_NOT_EXIST: 领域id不存在
  DOMAIN_DIC_EXCEL_TITLE_ERR: 读取字典项数据excel的表头异常
  DOMAIN_DIC_EXCEL_TITLE_EMPTY: 表头不能为空
  DOMAIN_DIC_EXCEL_TITLE_ORDER_ERR: 表头顺序错误，必须是
  DOMAIN_DIC_EXCEL_DATA_EMPTY: 字典项数据数据不能为空
  DOMAIN_DIC_UP_ERR: 字典项的上级字典不能为本身
  DOMAIN_LIST_NOT_EXIST: 根据路径寻找字典树异常
  DOMAIN_LIST_REPEAT: 同一类型下在库的名单值重复
  DOMAIN_PERMISSION_PARAM_INCOMPLETE: 入参不完整
  DOMAIN_PERMISSION_USER_DELETED: 选定成员无法被移除,该成员为领域唯一的超级管理员
  DOMAIN_LIST_CODE_REPEAT: 同一个领域下名单类型编码不能重复
  DOMAIN_LIST_TYPE_NOT_EXIST: 名单类型id不存在
  DOMAIN_LIST_NUM_NOT_EXIST: 名单类型编码不能修改
  DOMAIN_LIST_IMPORT_ERR: 生成名单数据导入模板出现异常
  DOMAIN_VAR_SERVICE_NOT_FOUND: 未查询到变量服务
  DOMAIN_VAR_SERVICE_ENABLE_NONE: 变量服务下无已启用的变量清单版本
  DOMAIN_SERVICE_CODE_RULE: 决策服务编码只能包含大小写字母、数字或中横线
  DOMAIN_SERVICE_NAME_REPEAT: 决策服务名称不允许重复
  DOMAIN_SERVICE_CODE_NOT_MODIFIED: 决策服务存在不处于“开发”状态的策略，服务编码无法修改
  DOMAIN_SERVICE_TYPE_NOT_MATCH: 待更新的决策服务类型和输入参数不匹配
  DOMAIN_SERVICE_PARAM_ERR: 参数错误
  DOMAIN_SERVICE_REFERENCED: 当前公共决策模块正在被引用，无法停用
  DOMAIN_SERVICE_CODE_EMPTY: 服务编码不允许为空
  DOMAIN_SERVICE_CODE_REPEAT: 服务编码不允许重复
  DOMAIN_SERVICE_NAME_EMPTY: 服务编码不允许重复
  DOMAIN_SERVICE_TEST_ERR: 未查询到策略最后测试结果对应的测试数据集，请检查数据完整性
  DOMAIN_SERVICE_INTERFACE_NOT_FOUND: 未查询到接口
  DOMAIN_SERVICE_INTERFACE_DELETED_ERR: 接口状态不处于“待发布”或“已停用”，不允许删除
  DOMAIN_PRODUCE_DATA_FAIL: 决策生产数据查询失败
  DOMAIN_PRODUCE_DATA_NOT_DELETED: 该接口已有产生生产调用数据，无法删除
  DOMAIN_INTERFACE_DATA_INCOMPLETE: 未查询到接口当前状态，请检查数据完整性
  DOMAIN_INTERFACE_DATA_MISS: 接口设定缺失，请检查数据完整性
  DOMAIN_INTERFACE_PUBLISH_FAIL: 接口发布失败
  DOMAIN_INTERFACE_SERVICE_NOT_MATCH: 未查询到接口对应的决策服务
  DOMAIN_INTERFACE_DELETED_FAIL: 资源删除失败
  DOMAIN_INTERFACE_TYPE_INVALID: 接口操作类型无法识别
  DOMAIN_INTERFACE_ID_EMPTY: 决策服务接口ID 不能为空
  DOMAIN_SERVICE_INTERFACE_DOC_NOT_FOUND: 未查询到接口文档
  DOMAIN_VAR_TYPE_NOT_SUPPORT: 暂时不支持变量类型{0}
  DOMAIN_SERVICE_ADD_FAIL: 添加/修改接口信息保存失败
  DOMAIN_SERVICE_NO_RECORD: 查询无记录
  DOMAIN_SERVICE_QUERY_ERR: 查询异常
  DOMAIN_SERVICE_FLOW_NO_EMPTY: 查询异常：查询流水号不能为空
  DOMAIN_SERVICE_LOG_NOT_FOUND: 未查询到决策数据
  DOMAIN_SERVICE_DATA_INCOMPLETE: 未查询到数据模型，请检查系统数据完整性
  DOMAIN_STRATEGY_DATA_INCOMPLETE: 未查询到策略信息，请检查系统数据完整性
  DOMAIN_BLAZE_FLOW_NO_EMPTY: 查询异常：Blaze流水号不能为空
  DOMAIN_BLAZE_ID_EMPTY: Blaze数据模型Id不能为空
  DOMAIN_BLAZE_LOG_NOT_FOUND: 未查询到Blaze决策数据
  DOMAIN_JAR_DOWNLOAD_FAIL: 离线JAR文件下载异常
  DOMAIN_STRATEGY_PACKAGE_NOT_FOUND: 未查询到策略部署包
  DOMAIN_STRATEGY_TYPE_NOT_SUPPORT: 不受支持的策略类型
  DOMAIN_STRATEGY_DEPLOY_NOT_ALLOWED: 该策略非“审核通过或生产状态”，不允许发布
  DOMAIN_BUCKET_DEPLOY_NOT_ALLOWED: 该细分下缺少生产状态的策略，不允许上线 {0}
  DOMAIN_BUCKET_DEPLOY_MOCK_FAIL: 策略模拟上线失败
  PMML_REASON_CODE_NOT_FOUND: 未找到 PMML 评分卡文件原因码，请检查导入设置和文件内容
  REPORT_PLAN_ID_EMPTY: 计划id为空
  REPORT_DECISION_ID_EMPTY: decisionId为空
  REPORT_SERVICE_PK_EMPTY: 服务主键为空
  REPORT_FIELD_ERR: 字段错误或已更新，请刷新重试
  COMPONENT_CONTENT_NO_FIELD: 组件内容里没有specific_data字段
  STRATEGY_OR_COMPONENT_ERR: 策略或组件id错误
  ABTEST_ID_ERR: AB测试id错误，请检查后重试
  ABTEST_GROUP_EMPTY: AB测试没有分组信息
  ABTEST_PARAM_ERR: 参数错误或策略不存在
  ABTEST_PARAM_EMPTY: 策略参数为空
  ABTEST_NOT_EXIST: 方案id错误或ab方案不存在
  ABTEST_FLOW_NODE_ERR: 流程分支下没有分支或节点id错误
  ABTEST_RESULT_FIELD_ERR: 决策服务主结果字段未配置或入参错误
  REPORT_STATISTIC_FAIL: 决策表-命中统计失败
  COMP_NOT_EXIST: 组件不存在或参数错误
  COMP_TYPE_ERR: 组件类型错误或参数错误
  STATISTIC_RANGE_INVALID: 区间范围不符合规范
  STATISTIC_RANGE_JOIN: 区间范围重合
  STATISTIC_FIELD_NAME_EMPTY: 字段名称不能为空
  STRATEGY_COMPARE_VERSION_NOT_FOUND: 未查询到正在对比的策略版本
  STRATEGY_COMPARE_VERSION_MISS: 正在对比的策略版本信息缺失
  STRATEGY_COMPARE_STATUS_INVALID: 正在对比的策略状态不受支持，请检查数据完整性
  DOMAIN_MODEL_COMPARE_VERSION_NOT_FOUND: 未查询到正在对比的数据模型版本
  ENGINE_VAR_ERR: 引擎变量字段值对比异常
  COMP_COPY_CONFIRM: 确认复制？
  REFERENCE_OBJ_DEL: 确定删除引用对象？
  REFERENCE_OBJ_USE_DEL: 该引用对象已被使用，确认删除？
  COMP_NOT_FOUND_DEL: 组件不存在或已删除
  DOMAIN_MODEL_MISS: 未查询到对比策略使用的数据模型
  STRATEGY_COMP_TYPE_VIEW_NOT_SUPPORT: 策略版本对比不支持当前决策组件类型的查看
  STRATEGY_COMPARE_VAR_NOT_FOUND: 未查询到对比策略使用的引擎变量
  COMP_COPY_STATUS_INVALID: 目标策略不处于“编辑中”状态，不允许复制组件
  COMP_COPY_NOT_FOUND: 被复制组件不存在，不允许复制组件
  COMP_COPY_EXIST: 名称/编码已存在，不允许复制组件
  COMP_DIC_NOT_FOUND: 未查询到被复制组件当前所属目录，请检查数据完整性
  COMP_ROOT_DIC_NOT_FOUND: 未查询到目标策略根目录，请检查数据完整性
  DIC_NAME_CN_REPEAT: 字典类型中文名称不能重复
  DIC_NAME_REPEAT: 字典项名称不能重复
  DIC_CODE_REPEAT: 字典类型编码不能重复
  DIC_ID_NOT_FOUND: 字典类型id不存在
  DIC_TREE_PATH_ERR: 字典树路径异常
  DIC_PARENT_INVALID: 字典项的上级字典不能为本身
  DIC_PARENT_EMPTY: 父级字典项不存在
  DIC_EXCEL_TITLE_ERR: 表读取字典项数据excel的头异常
  STRATEGY_EXPORT_NOT_FOUND: 待导出的策略不存在
  STRATEGY_EXPORT_FAIL: 策略导出失败，策略类型不属于决策服务策略或公共决策模块策略
  STRATEGY_DEPLOY_NOT_FOUND: 无法发布
  STRATEGY_INDEX_FAIL: 当前策略不属于决策服务策略，无法构建索引
  STRATEGY_MAIN_EMPTY: 该策略下没有查询到主流程组件
  STRATEGY_DEPLOY_NOT_EXIST: 策略不存在，无法发布
  STRATEGY_COMMON_INDEX_FAIL: 当前策略不属于公共决策服务策略，无法构建索引
  DOMAIN_PARAM_REPEAT: 同一个领域下参数名不能重复
  DOMAIN_PARAM_CN_REPEAT: 同一个领域下参数中文名不能重复
  STRATEGY_PARAM_NOT_FOUND: 未查询策略参数信息
  STRATEGY_PARAM_ID_NOT_FOUND: 策略参数id数据不存在
  STRATEGY_PARAM_NOT_REFRESH: 静态参数没有当前值，不支持刷新
  STRATEGY_INTERFACE_PUBLISH_FAIL: 策略所属决策服务资源尚未分配，无法上线
  STRATEGY_UPLOAD_FAIL: 策略上传异常
  STRATEGY_DEPLOY_STATUS_ERR: 策略发布状态查询异常
  STRATEGY_LOAD_FAIL: 卸载发布失败的策略异常
  STRATEGY_VAR_IMPORT_INCOMPLETE: 策略-变量服务引入数据结构不完整：接收对象{0}数据结构缺失
  VAR_INTERFACE_USE_NOT_FOUND: 变量服务不存在已启用的接口
  COMP_VERSION_NOT_FOUND: 未查询到组件版本记录
  COMP_CONTENT_NOT_FOUND: 未查询到组件版本内容
  COMP_NOT_FOUND: 未查询到该组件
  COMP_PMML_NOT_FOUND: 未找到 PMML 模型文件
  FILE_PMML_OBTAIN_FAIL: 无法从服务器获取 PMML 文件
  FILE_PMML_READ_FAIL: 无法读取 PMML 文件，请检查文件完整性
  FILE_PMML_PARSE_FAIL: PMML 文件解析异常
  FILE_PMML_READ_ERR: PMML 文件读取失败
  FILE_PMML_SCORE_MODEL_ERR: 上传的 PMML 文件不属于评分卡模型，请重新上传文件
  FILE_PMML_SCORE_MODEL_RULE: 上传的 PMML 文件属于评分卡模型，请重新选择文件类型后再次上传
  FILE_PKL_EMPTY: PKL模型文件不能为空
  FILE_PKL_UPLOAD_ERR: 上传PKL模型文件异常
  FILE_PKL_SCRIPT_EMPTY: PKL模型预测脚本文件不能为空
  FILE_PKL_SCRIPT_UPLOAD_ERR: 上传PKL模型预测脚本文件异常
  FILE_PKL_MODEL_EMPTY: PKL模型特征数据文件不能为空
  FILE_PKL_MODEL_UPLOAD_ERR: 上传PKL模型特征文件异常
  FILE_MODEL_EMPTY: MODEL模型文件不能为空
  FILE_MODEL_UPLOAD_ERR: 上传MODEL模型文件异常
  FILE_MODEL_SCRIPT_EMPTY: MODEL模型预测脚本文件不能为空
  FILE_MODEL_SCRIPT_UPLOAD_ERR: 上传MODEL模型预测脚本文件异常
  FILE_MODEL_DATA_EMPTY: 上传MODEL模型预测脚本文件异常
  COMP_FILE_FOLDER_FAIL: 未获取到文件夹
  FILE_PMML_NOT_FOUND: 未找到组件对应的 PMML 文件
  COMP_ID_NOT_FOUND: 组件ID不存在
  COMP_EXCEL_EXPORT_ERR: 导出Excel文件异常
  COMP_EXCEL_SUFFIX_ERR: 文件格式不正确，请导入后缀为xlsx的Excel文件
  NUM_TYPE_NOT_MATCH: 数字类型的参数必须填写数值
  COMP_REFERENCE_RELATION_EMPTY: 获取规则组件详情：组件引用关系记录不存在
  COMP_ID_RULE_EMPTY: 获取规则组件详情：组件ID不存在
  DATA_GENERATE_RULE_INVALID: 数据自动生成数量设定不能超过{0}条
  DATA_GENERATE_INPUT_EMPTY: 没有输入信息，不能在线自动生成数据
  DATA_GENERATE_CONFIG_EMPTY: 请先配置自动生成规则
  DATA_TEMP_NOT_FOUND: 临时表数据已被清除，无法继续查询
  COMPONENT_CONTENT_EMPTY: 组件没有查到内容信息
  TEST_FILE_IMPORT_ERR: 测试数据集文件导入异常
  COMP_USING_ERR: 当前组件正在被【{0}】编辑，请勿重复操作
  COMP_TYPE_EMPTY: 保存类型为空
  PMML_GENERATE_ERR: PMML 文件生成错误
  FILE_MODEL_DOWNLOAD_ERR: 模型文件下载异常
  FILE_VAR_DOWNLOAD_ERR: 下载特征变量模板文件异常
  COMP_CONTENT_EMPTY: 组件内容不能为空
  PARAM_DEFINE_ERR: 导入文档表头与参数定义不一致
  COMP_IMPORT_CONVERT_ERR: 组件导入数据转换失败：无法将数据{0}转换为{1}类型
  EXCEL_PARSE_FAIL: Excel 文件解析失败
  TEMPLATE_TITLE_ERR: 模板错误：标题第{0}列内容应为”条件“
  TEMPLATE_COL_ERR: 模板错误：标题最后一列内容应为”结果“
  PMML_FILE_NOT_EXIST: 组件 ID 对应的 PMML 文件不存在
  COMP_COL_CONDITION_MISS: 缺失条件列
  COMP_INPUT_MISS: 没有输入值
  COMP_COL_CONDITION_REPEAT: 条件列的组合不允许重复
  COMP_PARAM_ERR: 组件不是双轴决策表或参数错误
  DATA_MODEL_PREPARE_OBJ_NOT_INCLUDE: 数据模型未包含预处理对象
  DATA_MODEL_CONTENT_EMPTY: 模型文件内容为空
  AB_TEST_ID_NOT_EXIST: AB测试方案id不存在
  BUCKET_USABLE_NONE: 无可用细分
  RANDOM_MIN_VALUE_EMPTY: 范围最小值不能为空，请重新设置并保存生成规则
  RANDOM_MAX_VALUE_EMPTY: 范围最大值不能为空，请重新设置并保存生成规则
  TEST_DATA_AUTO_GEN_FAIL: 自动生成获取表头失败
  TEST_DATA_DEPENDENCY_AUTO_GEN_FAIL: 暂时不支持逻辑依赖的测试数据自动生成
  TEST_TABLE_HEADER_NOT_FOUND: 未找到
  TEST_TABLE_MODEL_NOT_FOUND: 数据模型未找到
  VAR_SPACE_REMOVE_CONFIRM: 确认删除该变量空间？
  VAR_CATEGORY_REMOVE_CONFIRM: 确认删除?
  VAR_CATEGORY_EDIT_CONFIRM: 该分类已被使用，确认编辑?
  VAR_PROCESS_OUTSIDE_SERVICE_CANCEL_REFERENCE_USING_BY_VARIABLES_REJECT: 外部服务正在被变量服务使用，无法取消引入。
  VAR_INTERFACE_SUBMIT_FOR_TESTING_VARIABLE_NOT_LISTED_DELETE: 该服务下的变量%s处于已删除状态，不允许提交。
  VAR_INTERFACE_SUBMIT_FOR_TESTING_VARIABLE_NOT_LISTED_REJECT: 该服务下的变量%s不处于已上架状态，不允许提交。
  VAR_INTERFACE_SUBMIT_FOR_TESTING_DATA_MODEL_BINDING_MISSING_REJECT: 变量清单下所有的数据模型绑定的数据来源配置不完善。
  VAR_INTERFACE_SUBMIT_FOR_TESTING_EXTENDED_DATA_NOT_PRE_PROCESSED_REJECT: 该服务依赖的扩展数据%s未定义预处理逻辑，不可提交。
  VAR_INTERFACE_SUBMIT_FOR_TESTING_PRE_PROCESS_LOGIC_NOT_LISTED_REJECT: 该服务依赖的预处理逻辑%s不处于已上架状态，不允许提交。
  VAR_INTERFACE_SUBMIT_FOR_TESTING_VARIABLE_TEMPLATE_NOT_LISTED_REJECT: 该服务依赖的变量模板%s不处于已上架状态，不允许提交。
  VAR_INTERFACE_SUBMIT_FOR_TESTING_COMMON_METHOD_NOT_LISTED_REJECT: 该服务依赖的公共方法%s不处于已上架状态，不允许提交。
  VAR_INTERFACE_SUBMIT_FOR_TESTING_CONFIRM: 提交审核后该服务将不可修改，确认提交？
  VAR_INTERFACE_APPLY_FOR_VERIFY_CONFIRM: 确认提交审核？
  VAR_INTERFACE_APPLY_FOR_PUBLISH_CONFIRM: 确认申请上线？
  VAR_INTERFACE_STEP_BACK_CONFIRM: 确认将该变量服务退回编辑状态？
  VAR_INTERFACE_APPROVE_CONFIRM: 审核通过后将自动启用该变量清单，确认审核通过？
  VAR_INTERFACE_DISABLE_CONFIRM: 确认停用该变量服务？
  VAR_INTERFACE_RE_ENABLE_CONFIRM: 一个服务下仅允许一个启用状态的变量清单，确认重新启用该变量清单？
  FUNC_TYPE_EMPTY: 函数类型不能为空
  VAR_NAME_EXIST: 名称已存在，不允许重复
  VAR_ONLINE_REPEAT: 已上架，不允许重复上架
  VAR_USE_ERR: 已被使用，不允许下架
  OPERATION_INPUT_ERR: 操作类型入参错误
  FUNC_CONFIRM_DEL: 是否确认{0}？
  PRE_USED_BY_VAR_ERR: 该预处理逻辑已被变量清单使用，不允许
  IS_CONFIRM_DEL: 是否确认删除？
  PRE_LOGIC_CONFIRM: 该预处理逻辑已被“编辑中/审核拒绝/停用”的变量服务接口使用，确认{0}？
  TEMPLATE_NOT_ALLOW: 该变量模板已被“待审核/已上架”状态的变量使用，不允许
  TEMPLATE_CONFIRM: 该变量模板已被“编辑中/审核拒绝/已下架”的变量使用，确认
  COMMON_METHOD_ERR: 该公共方法已被“待审核/已上架”状态的公共函数使用，不允许
  COMMON_METHOD_CONFIRM: 该公共方法已被“编辑中/审核拒绝/已下架”状态的变量模板/公共函数使用，确认{0}
  COMMON_FUNC_NOT_FOUND: 公共函数信息不存在或已删除
  TEST_RESULT_NOT_FOUND: 未查询到组件最后测试结果对应的测试数据集，请检查数据完整性
  VAR_NOT_FOUND: 未查询到变量信息
  VAR_SPACE_NOT_FOUND: 未查询到变量空间信息
  DATA_DETAIL_NOT_FOUND: 没有可执行的测试明细数据
  TEST_RUN_EXC: 测试执行异常
  TEST_RESULT_ID_NONE: 测试结果查询ID未传入
  TEST_RESULT_RECORD_NONE: 未查询到测试记录
  CONDITION_STATUS_ERR: 查询条件状态不正确
  EXCEL_EXPORT_ERR: 导出Excel文件异常
  RESULT_NORMAL_NONE: 未查询到正常执行的数据，不允许保存为预期结果
  PROCESS_CONTENT_EMPTY: 流程内容不能为空
  VAR_SPACE_CONVERTOR_ERR: 未查询到变量空间，无法将表单数据转换为 JSON 报文。
  JSON_FORMAT_ERR: 报文格式不正确，请输入标准 JSON 或人行征信中心 XML 格式报文。
  DATA_AUTO_GEN_RULE: 数据自动生成数量设定不能超过{0}条。
  VAR_INPUT_NONE: 没有输入变量，不能在线自动生成数据。
  AUTO_GEN_RULE_NOT_CONFIG: 请先配置自动生成规则
  TEMP_TABLE_DEL: 临时表数据已被清除，无法继续查询
  TEST_DATASET_IMPORT_ERR: 测试数据集文件导入异常
  TEST_MODEL_NOT_FOUND: 数据模型未找到
  TEST_TITLE_READ_FAIL: 数据明细下获取表头失败
  HISTORY_DATA_NOT_FOUND: 未查询到历史数据
  TEST_MODEL_NONE: 数据模型没有查询到
  VAR_LIST_NONE: 未查询到变量清单
  VAR_DEPLOY_LIST_NONE: 未查询到发布变量清单
  TEST_VAR_NAME_EXIST: 变量名已存在，不允许重复
  TEST_VAR_NAME_CN_EXIST: 变量名已存在，不允许重复
  TEST_VAR_UPGRADE_FAIL: 变量状态非上架或下架，不允许升级版本
  TEST_VAR_ONLINE_REPEAT: 变量已上架，不允许重复上架
  TEST_PARAM_ERR: 参数错误
  TEST_VAR_USE_NOT_OFF: 该变量已被使用，不允许下架
  TEST_VAR_USE_NOT_DEL: 该变量已被使用，不允许删除
  TEST_VAR_SPACE_CONFIRM_DEL: 确认删除该变量
  TEST_VAR_TYPE_EXIST: 变量类型已存在，不允许重复添加
  TEST_VALUE_EXIST: 该值已存在，不允许重复
  TEST_DEL_VALUE_ERR: 删除异常值参数错误
  TEST_BUILD_IN_ERR: 内置异常，不可删除
  TEST_VAR_EXIST: 变量信息不存在或已删除
  TEST_VAR_SPACE_ID_EMPTY: 变量空间ID不能为空
  TEST_VAR_ID_EMPTY: 变量ID不能为空
  OBJECT_NAME_EXIST: 对象名称或对象中文名在数据模型中已存在
  OBJECT_KEY_WORD_NOT_ALLOW: 对象名称不允许使用关键字
  DATA_MODEL_NOT_FOUND: 未查询到数据模型信息
  DATA_MODEL_EXCEL_READ_ERR: 读取数据模型excel的sheetName异常
  DATA_MODEL_SHEET_EMPTY: excel中的sheetName为空
  DATA_MODEL_SHEET_RULE: excel中的sheetName只能是
  DATA_MODEL_EXPORT_ERR: 导出数据模型Excel文件异常
  DATA_MODEL_RENEW_CONFIRM: 该数据模型已被非编辑状态的变量清单引用，不允许修改，是否创建新的可编辑版本？
  DATA_MODEL_REFERENCED_ERR: 该数据模型对象已被使用，不允许删除
  DATA_MODEL_DEL_CONFIRM: 删除后不可恢复，是否确认删除？
  OPERATOR_NOT_SUPPORT: 操作类型不符
  DOMAIN_MODEL_REFERENCED_VAR_NOT_DEL: 该数据模型已被非编辑或停用状态的变量清单引用，不允许删除
  OBJECT_NAME_CN_EXIST: 对象名称或对象中文名已存在
  DOMAIN_MODEL_COMPARE_NUM_ERR: 对比版本的数据模型必须等于2个
  DOMAIN_MODEL_ID_NONE: 数据模型id数据不存在
  DOMAIN_MODEL_DATA_NONE: 存在没有数据的数据模型id
  DOMAIN_MODEL_EXCEL_TITLE_EXC: 读取决策数据模型excel的表头异常
  DOMAIN_MODEL_SHEET_TITLE_EXC: sheet页：{0}的表头不能为空！
  DOMAIN_MODEL_EXCEL_DATA_EXC: 读取决策数据模型excel的数据异常
  DOMAIN_MODEL_ROOT_OBJECT_RULE: 根对象{0}对象名、中文描述、是否数组或是否扩展列数据错误。
  DOMAIN_MODEL_TITLE_NONE: 表头不存在
  VAR_SFTP_UPLOAD_FAIL: 上传sftp失败
  VAR_FILE_WRITE_FAIL: 写入文件内容失败
  VAR_PACKAGE_VALID_FAIL: 上线包验证不通过
  VAR_FILE_UNZIP_FAIL: 文件解压失败
  VAR_FILE_SIGN_FAIL: 签名文件解密异常
  VAR_SPACE_FILE_NOT_MATCH: 文件中的变量空间与生产上线变量空间不一致
  VAR_SPACE_ENV_DEPLOY_REPEAT: 目前环境已存在该变量空间，不允许重复发布
  VAR_LIST_ENV_EXIST: 目前环境已存在该变量清单版本，不允许重复发布
  VAR_FILE_DEC_FAIL: 数据文件解密异常
  VAR_TARGET_ENV_PRIVATE_KEY_NONE: 目标环境未初始化发布私钥
  VAR_DIC_PATH_EXC: 字典树路径异常
  VAR_DIC_PATH_FIND_EXC: 根据路径寻找字典树异常
  VAR_INTERFACE_ID_EMPTY: 被复制接口 ID 不能为空
  VAR_LIST_COPY_NONE: 未查询到被复制变量清单
  VAR_LIST_SOURCE_NOT_SUPPORT: 不支持的清单版本来源设置
  VAR_LIST_DEL_NOT_EXIST: 不支持的清单版本来源设置
  VAR_LIST_DEL_STATUS_NOT_EXIST: 待删除变量清单不处于“编辑中”、“审核拒绝”、“启动失败”或“审批拒绝”状态，无法被删除。
  VAR_SERVICE_NOT_FOUND: 待删除变量清单不处于“编辑中”、“审核拒绝”、“启动失败”或“审批拒绝”状态，无法被删除。
  VAR_LIST_EDIT_NOT_MODIFY: 待编辑变量清单不处于“编辑中”状态，无法被编辑
  VAR_LIST_EDIT_DEL: 待编辑变量清单已被删除，无法被编辑
  VAR_FLOW_NO_NOT_CONFIG: 调用流水号的绑定字段未设置
  VAR_FLOW_NO_INCORRECT: 调用流水号的绑定字段，需为外部传入对象下的属性，请调整
  VAR_DATA_MODEL_INCORRECT: 选择的数据模型来源非”内部传入“或”外部传入“
  VAR_OBJECT_NONE: 未查询到{0}对象
  VAR_OBJECT_BIND_NONE: 请选择绑定对象
  VAR_INNER_DATA_CONFIG_NONE: 未查询到内部数据配置
  VAR_OBJECT_ATTR_NONE: 对象下无属性
  VAR_OPERATION_NOT_MATCH: 未找到匹配的操作类型
  VAR_INTERFACE_NOT_FOUND: 未查询到接口信息
  VAR_LIST_OPERATION_NOT_FOUND: 不支持的变量清单操作类型
  VAR_SPACE_DATA_MODEL_NOT_FOUND: 未查询到变量空间的数据模型
  VAR_DATA_MODEL_USE_DEL: 使用的数据模型对象[{0}]已删除，请进入编辑页面先保存再提交。
  VAR_LIST_IN_SERVICE_NONE: 该服务版本下没有配置变量清单。
  VAR_PROCESS_IN_SERVICE_NONE: 该服务版本下没有配置流程信息。
  VAR_DATASOURCE_IN_SERVICE_NONE: 该服务版本下数据模型没有映射数据来源。
  VAR_LIST_DEPLOY_FAIL: 变量清单发布失败：REST 无法加载变量服务。
  VAR_LIST_NONE_DEPLOY_FAIL: 变量清单无法发布：未查询到变量清单。
  VAR_LIST_ENABLE_DEPLOY_FAIL: 变量清单无法发布：指定变量清单不处于'启用'状态。
  VAR_LIST_PUBLISH_DEPLOY_FAIL: 变量清单无法发布：当前变量空间存在状态为‘发布中’的记录。
  VAR_LIST_TARGET_NONE_DEPLOY_FAIL: 变量清单无法发布：目标服务器不存在或参数错误。
  STRATEGY_IN_DOMAIN_DEPLOYED: 当前领域存在发布中的策略记录。
  TARGET_SERVER_NOT_FOUND: 目标服务器不存在或参数错误。
  VAR_LIST_UNABLE_DEPLOY_NOT_ALLOW: 该变量清单非“启用状态”，不允许发布！
  VAR_LIST_PUBLISH_FAIL: 发布失败。
  VAR_LIST_PUBLISH_RECORD_NONE: 未查询到变量清单发布记录。
  VAR_LIST_SYNC_TARGET_ENV_ERR: 变量清单发布类型非“一键发布”，无法同步到目标环境。
  VAR_LIST_SYNC_TARGET_ENV_FAIL: 数据同步失败。
  VAR_LIST_DEPLOY_RECORD_NOT_EXIST: 发布记录不存在。
  VAR_FILE_DOWNLOAD_FAIL: 下载文件失败。
  VAR_FILE_SAVE_FAIL: 文件保存失败。
  VAR_TARGET_ENV_INCORRECT: 目标环境不是分析/运行环境，无法上线发布。
  VAR_TARGET_ENV_NOT_MATCH: 发布的环境类型与目标环境配置不一致，无法上线发布。
  VAR_TARGET_ENV_NO_USER: 目标环境未配置当前用户，无法上线发布。
  VAR_LIST_TARGET_ENV_EXIST: 目前环境已存在该变量清单，不允许重复发布。
  VAR_ONLINE_TRY_AGAIN: 该领域有用户正在发布上线，请稍后再试。
  VAR_FILE_SYNC_FAIL: 文件同步失败。
  VAR_DEPLOY_RECORD_NONE: 未查询到发布记录。
  VAR_TEMP_RECORD_NONE: 未查询到临时记录。
  VAR_PROCESS_NONE: 流程信息不能为空。
  VAR_LIST_BIND_INFO_NONE: 未查询到变量清单数据模型绑定信息。
  VAR_INNER_DATA_INFO_NONE: 未查询到内部数据信息。
  VAR_INNER_DATA_NAME_EXIST: 内部数据名称已存在，不允许重复。
  VAR_IMPORT_OBJECT_EXIST: 引入对象名已存在，不允许重复。
  VAR_IMPORT_OBJECT_CN_EXIST: 对象中文名已存在，不允许重复。
  VAR_INPUT_RETURN_EMPTY: 入参信息和返回数据不能为空。
  VAR_INPUT_PARAM_INCOMPLETE: 入参信息填写不完整。
  VAR_RETURN_OBJECT_INCOMPLETE: 返回数据中的对象填写不完整。
  VAR_RETURN_LEAF_OBJECT_INCOMPLETE: 返回数据中的叶子节点对象[{0}]没有映射表
  VAR_RETURN_OBJECT_NO_MAPPING: 返回数据中的对象[{0}]没有映射表
  VAR_RETURN_OBJECT_MAPPING_INCOMPLETE: 返回数据中的对象[{0}]映射表信息填写不完整。
  VAR_RETURN_OBJECT_INPUT_ERR: 返回数据中的对象[{0}]条件设置没有使用定义的入参
  VAR_RETURN_OBJECT_INPUT_NOT_SET: 返回数据中的对象[{0}]条件设置使用的入参[{1}]没有定义
  VAR_RETURN_FIELD_MAPPING_INCOMPLETE: 返回数据中的对象[{0}]字段映射信息填写不完整
  VAR_INNER_DATA_USE_NOT_DEL: 内部数据已被使用，不允许删除
  VAR_STR_TYPE_LEN_NOT_CONFIG: 字符串类型字段，需指定长度
  VAR_FIELD_ADD: 请至少增加一个字段
  VAR_TABLE_EXIST: 表名已存在
  VAR_TABLE_EMPTY: 表名不能为空
  VAR_TABLE_DEL_SUCCESS: 数据表删除成功
  VAR_TABLE_DEL_ERR: 数据表删除错误
  VAR_TABLE_CREATE_ERR: 创建数据表错误
  VAR_TABLE_INNER_QUERY_FAIL: 内部数据表查询失败
  VAR_TABLE_UPDATE_FAIL: 数据表更新失败
  VAR_SQL_SYNTAX_ERR: <li>SQL语法有错误：查询SQL需包含SELECT,FROM,TableName</li>
  VAR_SQL_INSERT_SYNTAX_ERR: <li>SQL语法有错误：查询SQL不能包括INSERT INTO 等</li>
  VAR_SQL_PK_SYNTAX_ERR: 主键不能为空
  VAR_FILE_HEADER_ERR: 请检查数据文件列头，与表定义不符
  VAR_QUERY_ERR: 数据查询错误
  VAR_DATA_ADD_ERR: 数据添加失败
  VAR_DATA_ADD_SUCCESS: 数据添加成功
  VAR_DATA_MODIFY_SUCCESS: 数据修改成功
  VAR_DATA_MODIFY_FAIL: 数据修改失败
  VAR_DATA_EXPORT_FAIL: 数据导出失败
  VAR_DATA_RECORD_EXPORT_NONE: 未查询到导出记录
  VAR_DATA_EXPORT_EMPTY: 导入数据文件不能为空
  VAR_DATA_FORMAT_CSV: 数据文件必须为CSV格式
  VAR_DATA_IMPORT_FAIL: 数据导入失败
  VAR_DATA_IMPORT_SUCCESS: 数据导入成功
  VAR_DATA_DEL_FAIL: 数据删除失败
  VAR_DATA_DEL_SUCCESS: 数据删除成功
  VAR_LIST_COMPILE_NONE: 待编译变量清单不存在。
  VAR_OUTSIDE_INFO_INCOMPLETE: 在系统参数中未查询到配置“外部服务数据缓存天数”，请检查数据完整性。
  VAR_SPACE_SPECIAL_NONE: 指定变量空间不存在。
  VAR_SPACE_OUTSIDE_REFERENCE_NONE: 未查询到变量空间和外部服务的引用关系。
  VAR_SPACE_OUTSIDE_REFERENCE_REPEAT: 外部服务在变量空间已被引入，无法重复引入。
  VAR_OUTSIDE_RES_NONE: 外部服务响应数据结构未定义。
  VAR_OUTSIDE_ROOT_NONE: 该外数没有根对象。
  VAR_MEMBER_NOT_DEL: 选定成员无法被移除： 该成员为变量空间唯一的超级管理员。
  VAR_LIST_ID_UPLOAD_EXC: 变量清单 ID 上传异常。
  VAR_LIST_DEPLOY_QUERY_EXC: 变量清单发布状态查询异常。
  VAR_LIST_DEPLOY_DEL_EXC: 变量清单 ID 移除异常。
  VAR_LIST_CODE_NOT_MODIFY: 变量服务存在不处于“编辑中”状态的清单，服务编码无法修改。
  VAR_LIST_AUTH_NOT_DEL: 已存在引入本变量服务的领域，无法删除授权。
  VAR_LIST_CODE_EXIST: 已存在编码为 {0} 的变量服务。
  VAR_LIST_NAME_EXIST: 已存在名称为 {0} 的变量服务。
  VAR_INTERFACE_EXIST: 服务下存在接口，不允许删除。
  VAR_SERVICE_TYPE_NOT_SUPPORT: 变量服务类型不支持。
  VAR_TYPE_NOT_SUPPORT: 暂时不支持变量类型{0}。
  VAR_CATALOG_NAME_REPEAT: 同一级别下，变量分类名称不能重复。
  VAR_CATALOG_SELECT_ERR: 不能选择分类自身
  VAR_CATALOG_SUB_NOT_DEL: 存在子类，不允许删除
  VAR_CATALOG_USE_NOT_DEL: 该分类已被使用，不允许删除
  VAR_CATALOG_SUB_NOT_SELECT: 不能选择自身下的子级
  VAR_GROUP_NONE: 未查询到标签组
  VAR_GROUP_EXIST: 标签组名称已存在
  VAR_GROUP_USE_NOT_DEL: 标签组已被使用，不允许删除
  VAR_MARK_NONE: 未查询到标签信息
  VAR_MARK_USE_NOT_DEL: 标签已被使用，不允许删除
  VAR_DIC_NAME_CN_REPEAT: 字典类型中文名称不能重复
  VAR_DIC_CODE_REPEAT: 字典类型编码不能重复
  VAR_DIC_ID_NONE: 字典类型id不存在
  VAR_DIC_PARENT_ERR: 字典项的上级字典不能为本身
  VAR_DIC_PARENT_NONE: 父级字典项不存在
  VAR_DIC_ITEM_CODE_REPEAT: 字典项编码不能重复
  VAR_DIC_ITEM_NAME_REPEAT: 字典项名称不能重复
  VAR_DIC_ITEM_ID_NONE: 字典项id数据不存在
  VAR_SPACE_ID_NONE: 空间id不存在
  VAR_DIC_ITEM_SHEET_NAME_ERR: 读取字典项数据excel的sheetName异常
  VAR_DIC_ITEM_TITLE_ERR: 读取字典项数据excel的表头异常
  VAR_DIC_ITEM_TITLE_EMPTY: 表头不能为空
  VAR_DIC_ITEM_TITLE_ORDER_ERR: 表头顺序错误，必须是
  VAR_DIC_ITEM_EXCEL_DATA_ERR: 读字典项数据excel的数据异常
  VAR_DIC_ITEM_EXCEL_DATA_EMPTY: 字典项数据数据不能为空
  VAR_DIC_ITEM_EXPORT_ERR: 导出字典项模板输入输出IO流读写异常
  VAR_DATA_PACKAGE_VALID_FAIL: 数据包验证不通过
  VAR_TABLE_SERVICE_NONE: 未获取到表名对应的服务
  VAR_SERVICE_NONE: 未获取到对应的服务
  VAR_TABLE_OBJ_NONE: 未获取到表对象
  VAR_SPACE_NAME_USE_FAIL: 无法创建变量空间：名称被占用
  VAR_SPACE_CODE_USE_FAIL: 无法创建变量空间：编码被占用
  VAR_SPACE_PACKAGE_IMPORTING_FAIL: 有用户正在导入空间数据包，请稍后再试
  VAR_SPACE_COPY_NONE: 无法创建变量空间：被复制变量空间不存在
  VAR_PARAM_TYPE_ERR: 入参type不合法
  VAR_DATA_MODEL_PREPARE_OBJ_NONE: 数据模型未包含预处理对象
  INPUT_PAGE_ERR: 输入查找页数错误
  TARGET_ENV_PRIVATE_KEY_NOT_DEPLOY: 目标环境未初始化发布私钥
  DESENSITIZE_RULE_NONE: 脱敏规则不存在
  DESENSITIZE_RULE_ALLOCATE: 以下变量路径已被分配脱敏规则
  STRATEGY_MODEL_ID_EMPTY: 策略id和数据模型id不能同时为空
  DIC_DATA_NONE: 字典类型数据不存在
  LIST_TYPE_NONE: 名单类型不存在
  LIST_TYPE_REPEAT: 同一类型下未出库的名单值不能重复
  LIST_REPEAT_STORE_ERR: 不允许对同一名单值重复入库
  LIST_EXIST: 已在在库名单中，不允许重复入库
  DOMAIN_SPECIFY_NONE: 指定领域不存在
  EXCEL_IMPORT_STYLE_NOT_SUPPORT: Excel 导入方式不受支持
  EXCEL_SHEET_NAME_EMPTY: Excel 工作簿文件名不能为空
  EXCEL_SHEET_READ_EXC: Excel 工作页提取异常
  EXCEL_SHEET_PAGE_EMPTY: Excel 工作页名称不能为空
  EXCEL_IMPORT_LIST_TYPE_NOT_MATCH: Excel 工作页名和待导入名单类型的名称不一致
  EXCEL_TABLE_TITLE_EMPTY: Excel 表头读取异常
  EXCEL_TABLE_TITLE_NOT_SUPPORT: 不受支持的 Excel 表头类型
  EXCEL_TABLE_TITLE_READ_EXC: Excel 表格数据读取异常
  EXCEL_TABLE_DATA_EMPTY: 名单数据内容不能为空
  LIST_CODE_NONE: 未查询到编码为{0}的名单类型。
  RESULT_FIELD_ERR: 结果字段已调整或参数错误，请刷新后重试。
  BEAN_NONE_EXC: 找不到对应bean异常。
  DATE_FORMAT_ERR: 日期格式不正确。
  TEST_RESULT_INCOMPLETE: 未查询到组件最后测试结果对应的测试数据集，请检查数据完整性。
  SCORE_COMP_VAR_NONE: 当前评分组件没有定义变量。
  SCORE_COMP_BUILD_FAIL: 评分组件构建失败。
  SERVICE_STRATEGY_NONE: 服务下没有可用策略。
  FACADE_DATA_CONFIG_EXC: 表现数据落库配置错误。
  DATASOURCE_NOT_SELECT: 请选择拆分库数据源和报文数据源。
  DATASET_EMPTY: 数据集不能为空。
  TASK_DOING_DEL: 任务状态为“进行中”不允许删除。
  TASK_DOING_MODIFY: 任务状态[进行中]不允许修改。
  TASK_SUCCESS_MODIFY: 任务状态[成功]不允许修改。
  DOMAIN_RENEW_CONFIRM: 该对象在数据模型中已经存在，且不可修改。是否为该对象创建新的数据模型版本
  IS_DEL_CONFIRM: 是否确认删除
  VAR_SPACE_ONLINE_DEL: 该变量空间下存在已上架的变量，不允许删除
  DESENSITIZATION_SPACE_NOT_FOUND: 未找到指定 ID 对应的决策领域/外数空间。
  DESENSITIZATION_RULE_NAME_OCCUPIED: 脱敏规则名称不允许重复
  DESENSITIZATION_SYS_METHOD_NOT_FOUND: 用于脱敏的系统方法不存在
  DESENSITIZATION_SYS_METHOD_DISABLED: 选定的系统方法处于停用状态，脱敏方法设置失败
  CHECK_DATA_INTEGRITY: 请检查数据完整性
  NOT_SUPPORTED_STRATEGY_CODE: 请检查数据完整性
  TOPIC_NOT_NULL: topic不能为空！
  FIELD_TYPE: 字段类型
  NOT_MATCH_DATA_TYPE: 未匹配到数据类型
  NOT_QUERY_INTERNAL_DATA_CONFIG_MESSAGE: 未查询到内部数据配置信息
  FAIL_QUERY_DATA: 查询数据失败
  DYNAMIC_TABLE_NAME_FILED: 数据模型未包含预处理对象
  NOT_NULL: 不能为空！
  MODEL_METADATA_EXCEL_ERROR: 模型元数据excel数据解析异常！
  ROOT_OBJECT_NOT_NULL: 根对象不能为空
  ROOT_OBJECT_ALLOW_INPUT_ALPHABET_OR_NUMBER_ALPHABET: 根对象只能输入字母或者字母与数字组合！
  ROOT_OBJECT_CONNOT_EXIST_PROPERTY_NAME: 根对象中不能存在属性名称！
  NOT_ROOT_OBJECT_SUBOBJECT: 根对象中不能即是根对象又是子对象！
  ROOT_OBJECT_TYPE_ONLY_OBJECT: 根对象类型只能是object
  ROOT_TYPE_NOT_NULL: 根对象类型不能为空！
  ROOT_OBJECT_REQUIRED_FIELD_IS_EMPTY_OR_ZERO_OR_ONE: 根对象必填字段只能为空或者0或者1
  ROOT_OBJECT_ARRAY_FIELD_IS_EMPTY_OR_ZERO: 根对象中数组字段只能为空或者为0！
  ROOT_OBJECT_DICTIONARY_FIELD_IS_EMPTY_OR_ZERO: 根对象中字典字段只能为空或者为0！
  SUB_OBJECT_ALLOW_INPUT_ALPHABET_OR_NUMBER_ALPHABET: 子对象只能输入字母或者字母与数字组合！
  SUB_OBJECT_NOT_EMPTY: 子对象不能为空！
  SUB_OBJECT_NOT_ALLOW_MORE_SUB_OBJECT_MEANWHILE: 子对象中不能同时存在多个子对象！
  SUB_OBJECT_PROPERTY_NAME_ONLY_EMPTY: 子对象的属性名只能为空！
  SUB_OBJECT_TYPE_NOT_EMPTY: 子对象类型不能为空！
  SUB_OBJECT_TYPE_ONLY_OBJECT: 子对象类型只能是object
  SUB_OBJECT_REQUIRED_FIELD_IS_EMPTY_OR_ZERO_OR_ONE: 子对象必填字段只能为空或者0或者1！
  SUB_OBJECT_ARRAY_FIELD_IS_EMPTY_OR_ZERO_OR_ONE: 子对象中数组字段只能为空或者为0或者1！
  SUB_OBJECT_DICTIONARY_FIELD_IS_EMPTY_OR_ZERO: 子对象中字典字段只能为空或者为0！
  PROPERTY_NOT_ALLOW_EXIST_ROOT_OBJECT_NAME: 属性中不能存在根对象名称！
  PROPERTY_NOT_ALLOW_EXIST_SUB_OBJECT_NAME: 属性中不能存在根对象名称！
  PROPERTY_NAME_NOT_EMPTY: 属性的名称不能为空！
  PROPERTY_NAME_RULE: 属性的名称只能输入字母或者字母与数字组合，且至少为2个字符！
  PROPERTY_TYPE_NOT_ALLOW_EMPTY: 属性的类型不能为空！
  PROPERTY_TYPE_RULE: 属性的类型只能是java中存在的非对象类型！
  PROPERTY_REQUIRED_FILED_RULE: 属性的必填字段只能为空或者0或者1！
  PROPERTY_ARRAY_FILED_RULE: 属性的数组字段只能为空或者0或者1！
  PROPERTY_DICTIONARY_FILED_RULE: 属性的字典字段只能为空或者0或者1！
  SUB_OBJECT_NOT_HAVE_DIRECT_ROOT_OBJECT: 子对象没有直属父级对象！
  REPETITIVE_VAR_PATH: '存在重复的变量路径：'
  RANGE_TYPE: 区间范围必须包含两个值以上
  NULL_DATA_SOURCE: 数据源为空
  TARGET_OBJECT_CREATED_FAIL: 目标对象创建失败
  CONVERSION_FAIL: 转换失败，请检查属性类型是否匹配
  NOT_HAVEN_AVAILABLE_FILE_PREVIEW_SERVICE: 没有可用的文件预览服务
  DATABASE: 数据库
  GET_SHEMASQL_NOT_COVER_DATAVASETYPEENUM: getSchemaSql输入未覆盖的DatabaseTypeEnum
  WISECO_ENCRYPTOR_CLIENT_NOT_BEAN_INSTACE: wisecoEncryptorClient没有Bean被实例化！
  NOT_SUPPORT_DATABASE_TYPE: 不支持的数据库类型
  KERBEROS_GET_TICKET_FAIL: kerberos 获取ticket失败
  CONNECT_DATABASE_FAIL: 选择的数据库连接失败！
  LENGTH_RULE: 长度不能大于外部传入字段的hash长度
  LENGTH_MUST_LARGER_THEN_OR_EQUAL_TO_ONE: 长度必须大于等于1！
  LENGTH_RULE_TWO: 长度不能大于部字段加固定字段的hash长度
  PROVINCIALISM_INCOMPATIBILITY: 方言不适配！需要适配后重试！
  METHOD_NOT_SUPPORT_DECISION_IO_TYPE_COMPONENT: 本方法不支持决策流类型组件（主流程、决策流等）trace 内容的解析
  TYPE_NOT_ALLOW_CHANGED: 方法已经被使用后，不可以更改适用范围

SimulatorCodeMessageEnum:
  SERVICE_ID_EMPTY: 决策服务ID不能为空
  EVAL_RESULT_EMPTY: 衍生指标结果为空
  QUERY_RESULT_ERR: 查询数据错误
  MODEL_ID_EMPTY: 数据模型ID不能为空
  DATA_NOT_CONFIG: 表现数据未配置
  CONDITION_ERR: where 条件错误
  DATA_PREVIEW_ERR: 数据预览错误
  DATA_SET_NOT_EXIST: 数据集[{0}]不存在
  DATABASE_CONFIG_ERR: 生产数据落库配置错误
  DATA_RUN_TIME_ERR: 生产数据执行时间范围未选择
  DATA_RUN_TIME_NOT_CONFIG: 请选择任务执行时间
  TASK_RUN_TIME_ERR: 任务执行时间应在当前时间之后
  DATA_PRODUCT_NOT_CONFIG: 生产数据未配置
  DATABASE_CHOOSE_ERR: 请选择拆分库数据源和报文数据源
  DATABASE_MOCK_FAIL: 模拟拆分库建表失败
  DATABASE_FIELD_REPEAT: 生产落库配置落库字段名重复
  TASK_START_NOT_SUPPORT: 不支持的任务启动方式
  FIELD_PARSE_ERR: 生产数据落库字段配置解析错误
  FIELD_CONFIG_PARSE_ERR: 表现数据落库字段配置解析错误
  DATABASE_CONN_ERR: 表现数据建表失败
  DATASET_NOT_FOUND: 数据集不存在或已删除
  DATASET_NOT_DEL: 正在执行数据集导入任务,不允许删除
  DATASET_EXIST: 该数据集已经有模拟记录，不允许删除
  DATASOURCE_ERR: 数据来源不合法
  DATASET_RECORD_NOT_FOUND: 数据集记录不存在
  INDICATOR_RECORD_NOT_FOUND: 指标记录不存在
  INDICATOR_LOGIC_EMPTY: 指标执行逻辑不能为空
  INDICATOR_LOGIC_REPLACE_NOT_SUPPORT: 指标执行逻辑替换表名称不支持除SELECT查询以外的其他DML操作
  SQL_PARSE_ERR: 指标执行逻辑SQL解析异常
  DATA_IMPORT_CONFIG_EMPTY: 获取数据导入配置参数不能为空
  DATASET_IMPORT_CONFIG_EMPTY: 数据集类型对应的导入配置不存在
  JOIN_NOT_SUPPORT: 不支持的连接操作
  CONDITION_WHERE_ERR: where条件不正确
  MODEL_NOT_FOUND: 未查询到数据模型
  DATA_DETAIL_ERR: 数据明细下未查询到相关表头信息
  DATA_DETAIL_OBTAIN_ERR: 数据明细下获取表头失败
  DATASOURCE_CONFIG_EMPTY: 获取数据源配置信息参数不能为空
  DATASOURCE_CONFIG_FAIL: 获取数据源配置信息失败
  MONGO_INIT_EMPTY: 初始化mongo客户端参数不能为空
  PARAM_EMPTY: 参数不能为空
  DATASET_NAME_EXIST: 数据集名称已存在
  DATASOURCE_RECORD_NOT_EXIST: 数据源配置记录不存在
  DATASOURCE_TYPE_NOT_SUPPORT: 暂不支持[{0}]类型的数据源！
  DATA_DRIVER_EMPTY: 数据库驱动类不能为空！
  DATABASE_URL_EMPTY: 数据库连接URL不能为空！
  DATABASE_USERNAME_EMPTY: 数据库用户名不能为空！
  NOT_SUPPORT: 暂不支持！
  EXE_RANGE_ERR: 请选择执行时间范围！
  DATABASE_CONFIG_NONE: 决策服务拆分落库配置未定义！ decision_id：{0}
  DATA_IMPORT_ERR: 表现数据导入错误
  DATASET_IMPORT_FAIL: 数据集导入任务执行失败！数据集不存在！{0}
  DATABASE_PRESENT_FAIL: 表现数据落库配置错误
  PARAM_DATASOURCE_EMPTY: 参数[数据来源]不能为空
  INDICATOR_ONLINE_ID_EMPTY: 在线添加指标ID不能为空
  INDICATOR_ONLINE_RECORD_EMPTY: 在线添加指标记录不存在
  INDICATOR_NAME_EXIST: 指标名称已存在
  INDICATOR_RECORD_NOT_EXIST: 指标记录不存在
  DATASET_ID_EMPTY: 数据集Id为空
  MOCK_TASK_ID_EMPTY: 模拟任务指标结果查看：模拟任务ID不能为空
  MOCK_TASK_DATASET_EMPTY: 模拟任务的数据集不存在
  INDICATOR_EVAL_ERR: 指标计算错误
  DATASET_EMPTY: 数据集不能为空
  EVAL_FUNC_EMPTY: 计算函数不能为空
  EVAL_FUNC_NOT_SUPPORT: 计算函数不支持
  VAR_EMPTY: 统计变量不能为空
  DATA_PRECISION_EMPTY: 数据精度不能为空
  IS_REPEAT_EMPTY: 是否去重不能为空
  QUERY_PARAM_EMPTY: 查询参数错误！数据集Id和模拟任务Id都为空
  REPORT_GEN_FAIL: 报表生成失败
  INDICATOR_RULE: 请至少选择一个指标
  VECTOR_RULE: 请至少选择一个维度
  VECTOR_ERR: 行维度和列维度不能重复
  DATA_PRESENT_CONFIG_NONE: 表现数据关联配置未设置
  DATASET_READ_ERR: 数据集不存在或已被删除，无法读取维度定义
  MOCK_TASK_NONE: 模拟任务不存在或已删除
  TIME_CONFIG_ERR: 时间维度设置错误
  TIME_SELECT_ERR: 时间维度[{0}]选择错误！
  MOCK_STRATEGY_NOT_DEL: 该模拟策略已经有模拟记录，不允许删除
  STRATEGY_VERSION_REPEAT: 策略版本已存在不能重复添加
  MOCK_STRATEGY_EXIST: 模拟策略名称已存在
  TASK_EXIST: 任务不存在
  TASK_STATUS_NOT_DEL: 任务状态为“进行中”不允许删除
  MOCK_TASK_EXIST: 模拟任务不存在
  SERVICE_NOT_EXIST: 决策服务不存在
  DATASET_NOT_EXIST: 数据集不存在
  SERVICE_ID_NOT_EXIST: 策略不存在
  INDICATOR_CONFIG_NOT_EXIST: 指标配置记录不存在
  VECTOR_NOT_EXIST: 维度定义记录不存在
  DATA_IMPORT_CONFIG_ERR: 数据导入配置数据不正确
  MOCK_TASK_ID_ERR: 模拟任务ID不能为空
  MOCK_TASK_RECORD_ERR: 模拟任务记录不存在
  TASK_PARAM_ERR: 添加/修改任务参数不能为空
  TASK_ID_ERR: 待修改的任务ID不能为空
  TASK_ID_EMPTY: 任务ID不存在
  TASK_DOING_ERR: 任务状态[进行中]不允许修改！
  TASK_SUCCESS_ERR: 任务状态[进行中]不允许修改！
  STRATEGY_PARAM_EMPTY: 保存策略信息参数不能为空
  MOCK_STRATEGY_PARAM_EMPTY: 模拟策略列表数据不能为空
  PARSE_PARAM_EMPTY: 保存分析配置参数不能为空
  INDICATOR_LIST_EMPTY: 指标列表数据不能为空
  INDICATOR_DATASET_ID_EMPTY: 指标列表数据不能为空
  VECTOR_VAR_EMPTY: 维度定义变量不能为空
  GROUP_SET_EMPTY: 分组设置方式不能为空
  VECTOR_RANGE_ERR: 维度定义的区间范围包含方式不正确
  MOCK_DATA_LIST_ERR: 任务策略信息模拟策略列表数据不存在
  TASK_NAME_EXIST: 任务名称已存在
  MOCK_CONTEXT_NONE: 模拟任务上下文不存在
  FLOW_NO_NONE: 服务配置业务流水号未配置
  FLOW_NO_EMPTY: 服务配置业务流水号配置为空
  REQ_MSG_EMPTY: 策略模拟原始请求报文为空
  INPUT_DATA_ERR: 组装input类数据异常
  DOMAIN_ID_EMPTY: 领域ID不能为空
  DOMAIN_RECORD_EMPTY: 领域记录不存在
  MOCK_RECORD_EMPTY: 模拟策略记录不存在
  RESULT_RECORD_EMPTY: 获取模拟策略执行结果表名称参数不能为空
  BASE_PARAM_EMPTY: 获取基准数据表名称参数不能为空
  TASK_PARAM_EMPTY: 执行模拟任务参数不能为空
  BUILD_CONTEXT_EMPTY: 构建执行上下文参数不能为空
  ENGINE_INIT_ERR: 初始化引擎异常
  TABLE_EMPTY: clickhouse表名称为空
  KAFKA_TOPIC_EMPTY: 请检查是否配置Kafka Topic

SyncCodeMessageEnum:
  JSON_CONVERTOR_ERR: 转换规则非有效JSON，请检查
  JSON_CONVERTOR_SAVE_FAIL: 转换规则保存失败
  DATABASE_CONF_NONE: 数据库配置不存在或参数错误
  PRE_DATA_EXC: 预配数据异常
  TASK_CRON_NONE: 启用的任务没有配置执行Cron
  DATABASE_CONF_UNABLE: 数据库配置不存在或未启用
  TASK_NAME_REPEAT: 任务配置重名，请修改后重试
  CONFIG_PK_NONE: 配置主键错误或数据不存在
  UPDATE_PK_EMPTY: 更新时主键为空
  PARAM_ERR_OR_NONE: 参数错误或数据不存在
  OPERATION_NONE: 状态相同，无需操作
  DATA_EXE_STATUS_FAIL: 数据不是执行失败状态
  DATABASE_TYPE_NOT_SUPPORT: 不支持的数据库类型
  TABLE_RESULT_EXC: 表{0}查询结构时异常：{1}
  DATABASE_TASK_RUNNING_DEL: 当前数据库还有{0}个有效任务在使用，不允许删除！
  DATA_STATUS_UPDATED: 数据状态已更新
  DATABASE_TASK_RUNNING_STOP: 当前数据库还有{0}个有效任务在使用，不允许停用！
  TABLE_STRUCTURE_FAIL: 表结构处理错误
  TARGET_FIELD_USE_REPEAT: 目标字段重复使用
  TARGET_FIELD_MAP_ERR: 字段映射有错误
  ORIGINAL_FIELD_EMPTY: 原字段全部为空不允许保存
  CRON_EXP_ERR: cron表达式格式错误
  MULTI_TABLE_JOIN_ERR: 多表关联没有关联关系或关联条件
  MULTI_TABLE_JOIN_ALIAS_NONE: 多表关联时需要配置别名
  MULTI_TABLE_JOIN_NONE: 多表关联时第{0}个表没有关联关系！
  FIELD_MAP_EMPTY: 任务字段映射为空，请配置后重试

ServiceCodeMessageEnum:
  DOMAIN_MODEL_TEMPLATE_EXPORT_ERR: 导出变量模板IO异常
  DOMAIN_MODEL_VAR_EXPORT_ERR: 导出变量 Excel 文件异常
  DOMAIN_MODEL_EXCEL_TITLE_EMPTY: 表头不存在
  DOMAIN_DIC_EXCEL_READ_ERR: 读取字典项数据excel的sheetName异常
  DOMAIN_DIC_EXCEL_DATA_ERR: 读字典项数据excel的数据异常
  DOMAIN_DIC_EXCEL_EXPORT_ERR: 导出字典项模板输入输出IO流读写异常
  DOMAIN_DIC_TREE_ERR: 字典树路径异常
  DOMAIN_DIC_TREE_FIND_ERR: 根据路径寻找字典树异常
  DOMAIN_LIST_NOT_EXIST: 名单类型不存在
  DOMAIN_SERVICE_TYPE_EMPTY: 参数公共类型（commonType）不能为空
  DOMAIN_SERVICE_INTERFACE_EXIST: 决策服务接口名称已存在，保存失败
  DOMAIN_SERVICE_EXIST: 决策服务已存在
  DOMAIN_SERVICE_EXPORT_ERR: 导出变量 Excel 文件异常
  DOMAIN_STRATEGY_JAR_ERR: 离线JAR打包异常
  DOMAIN_STRATEGY_IDS_EMPTY: 策略ID集合不能为空
  DOMAIN_STRATEGY_ID_EMPTY: 策略ID不存在
  DOMAIN_SERVICE_ID_NOT_EXIST: 服务ID不存在
  DOMAIN_ID_NOT_EXIST: 领域ID不存在
  DOMAIN_ENGINE_PACKAGE_ERR: 离线引擎包数据写入异常
  DOMAIN_LIB_DEPENDENCY_ERR: 依赖类库数据写入异常
  DOMAIN_FILE_NAME_EMPTY: 文件名称不能为空
  DOMAIN_FILE_ARCHER_EMPTY: 读取归档文件内容异常
  DOMAIN_JAR_DOWNLOAD_FAIL: 离线jar下载失败
  DOMAIN_FILE_DOWNLOAD_FAIL: oss下载文件失败
  DOMAIN_FILE_SAVE_FAIL: 文件保存失败
  DOMAIN_PARAM_TYPE_NOT_EXIST: 参数类型不存在
  DOMAIN_PARAM_DYNAMIC_EMPTY: 动态参数刷新频率不能为空
  DOMAIN_PARAM_DYNAMIC_RATE_EMPTY: 动态参数刷新频率不存在
  DOMAIN_PARAM_STATIC_RATE_EMPTY: 静态参数没有动态参数刷新频率，只能为空
  DOMAIN_STRATEGY_TYPE_EMPTY: 数据类型不存在
  DOMAIN_STRATEGY_INT_INVALID: '{0}为int类型，类型填写异常'
  DOMAIN_STRATEGY_DOUBLE_INVALID: '{0}为double类型，类型填写异常'
  DOMAIN_STRATEGY_BOOLEAN_INVALID: '{0}为boolean类型，类型填写异常'
  DOMAIN_STRATEGY_DATE_INVALID: '{0}为date类型，类型填写异常'
  DOMAIN_STRATEGY_DATETIME_INVALID: '{0}为datetime类型，类型填写异常'
  DOMAIN_COMPONENT_UNABLE: 该策略下没有查询到可用的组件
  DOMAIN_STRATEGY_DEPLOY_FAIL: 审批通过的策略上线失败
  DOMAIN_COMMON_STRATEGY_DEPLOY_FAIL: 公共决策模块的策略上线发布失败
  DOMAIN_STRATEGY_ONLINE_NOT_FOUND: 未查询到申请上线记录
  DOMAIN_STRATEGY_DEPLOY_AGAIN_FAIL: 策略重新上线失败
  DOMAIN_STRATEGY_FALLBACK_NOT_FOUND: 没有可回退的版本
  DOMAIN_STRATEGY_DEPLOY_TIME_EMPTY: 指定上线时间不能为空
  DOMAIN_STRATEGY_TERMINAL_TIME_SELECT: 请选择终止条件
  DOMAIN_STRATEGY_INFO_EMPTY: 指标信息不能为空
  DOMAIN_STRATEGY_TERMINAL_TIME_EMPTY: 终止时间不能为空
  DOMAIN_STRATEGY_GRAY_RATIO_RULE: 灰度流量比例总和不应超过100%，请重新分配
  DOMAIN_STRATEGY_NACOS_DEPLOY_FAIL: 模拟上线读取nacos上线策略ID异常
  DOMAIN_STRATEGY_VERSION_NOT_EXIST: 复制策略版本不存在，请重新操作
  DOMAIN_STRATEGY_FILE_EXPORT_FAIL: 导出文件失败
  DOMAIN_STRATEGY_FILE_READ_FAIL: 写入文件内容失败
  DOMAIN_STRATEGY_PACKAGE_CHECK_FAIL: 数据包验证不通过
  DOMAIN_STRATEGY_PACKAGE_ONLINE_CHECK_FAIL: 上线包验证不通过
  DOMAIN_STRATEGY_IMPORT_ZIP_FAIL: 策略导入解压失败
  DOMAIN_STRATEGY_SIGN_DEC_FAIL: 签名文件解密异常
  DOMAIN_STRATEGY_DATA_SIGN_DEC_FAIL: 数据文件解密异常
  DOMAIN_STRATEGY_DATA_IMPORT_FAIL: 导入解析数据异常
  DOMAIN_STRATEGY_DATA_PARSE_FAIL: 数据包解析失败
  DOMAIN_COMPONENT_DEPLOY_FAIL: 决策组件发布异常
  DOMAIN_COMPONENT_DEPLOY_STATUS_ERR: 查询组件发布状态异常
  PERMISSION_INDICATOR_INCORRECT: 资源权限标识不正确
  FILE_PARAM_EMPTY: 参数remoteDirType不能为空
  FILE_PARAM_NOT_SUPPORT: 不持支的remoteDirType：1=json组件，2=模型文件
  FILE_NAME_EMPTY: 参数fileName不能为空
  FILE_PARSE_FAIL: 文件解析失败
  INDICATOR_SCRIPT_EMPTY: 指标脚本不能为空
  INDICATOR_TYPE_NOT_SUPPORT: 不支持的指标类型
  INDICATOR_IDS_EMPTY: 参数数据集ID不能为空
  INDICATOR_RECORD_EMPTY: 数据集记录不存在
  INDICATOR_VALID_TYPE_NOT_SUPPORT: 不支持的校验类型
  INDICATOR_CONFIG_ID_EMPTY: 指标配置ID不能为空
  INDICATOR_CONFIG_EMPTY: 指标配置记录不存在
  INDICATOR_VALID_NAME_EMPTY: 校验指标名称是否存在参数不能为空
  INDICATOR_NAME_EXIST: 指标名称已存在
  FUNC_EMPTY: 计算函数不能为空
  FUNC_NOT_SUPPORT: 计算函数不支持
  VAR_EMPTY: 统计变量不能为空
  DATA_EMPTY: 数据精度不能为空
  REPEAT_EMPTY: 是否去重不能为空
  OUTSIDE_SERVICE_ID_EMPTY: 外部服务引入对象id不存在
  VAR_IMPORT_TEMPLATE_ERR: 导入策略引擎变量字典项模板输入输出读写异常
  PYTHON_MODEL_PULL_FAIL: 通知模型进行文件拉取失败
  STRATEGY_INTERFACE_INVOKE_FAIL: 调用模型文件拉取通知服务接口异常
  MODEL_VAR_TEMPLATE_DOWNLOAD_ERR: 模型特征变量模板文件下载异常
  PAGE_RULE_ERR: 页码pageNo与页大小pageSize必传
  PAGE_NUM_ERR: 参数页码不正确
  STRATEGY_DATA_INCOMPLETE: 未查询到指定策略，请检查数据完整性
  DATA_DETAIL_STRATEGY_EMPTY: 数据明细下未查询到策略
  DATA_DETAIL_VAR_EMPTY: 未查询到组件相关变量
  DATA_MODEL_NOT_FOUND: 未查询到数据模型
  DATA_DETAIL_NOT_FOUND: 未查询到相关数据集明细
  DATA_DETAIL_TITLE_NOT_FOUND: 数据明细下未查询到相关表头信息
  DATA_DETAIL_TITLE_FAIL: 数据明细下获取表头失败
  DATA_INFO_NOT_FOUND: 数据集信息不存在
  DATA_MERGE_FAIL: 选择合并的数据集存来源不同, 不允许合并
  STRATEGY_EXPORT_NOT_FOUND: 导出模板未查询到策略
  MODEL_EXPORT_NOT_FOUND: 导出模板未查询到数据模型
  VAR_USE_NOT_FOUND: 未查询到使用的变量
  LINE_IMPORT_EMPTY: 导入行数不能为空
  HISTORY_DATA_EMPTY: 未查询到历史数据
  STRATEGY_RUN_DATA_DETAIL_EMPTY: 策略测试执行未查询到相关数据集明细
  STRATEGY_RUN_DATA_EMPTY: 策略测试执行未查询到策略
  STRATEGY_RUN_DATA_MODEL_EMPTY: 策略测试执行未查询到数据模型
  STRATEGY_RUN_FAIL: 策略测试执行失败
  STRATEGY_RUN_RESULT_FAIL: 策略测试保存结果异常
  STRATEGY_QUERY_CONDITION_FAIL: 查询条件状态不正确
  STRATEGY_QUERY_PARAM_FAIL: 查询条件参数不正确
  STRATEGY_SUCCESS_DATA_NOT_FOUND: 未查询到正常执行的数据，不允许保存为预期结果
  DATA_MODEL_QUERY: 数据模型没有查询到
  STRATEGY_FORM_ONLINE_NOT_FOUND: 在线表单未查询到策略
  DATA_MODEL_FORM_ONLINE_NOT_FOUND: 在线表单未查询到数据模型
  PARAM_NOT_INCLUDE: 参数[{0}]不在参数定义中
  TABLE_TITLE_FORMAT_ERR: 表头格式错误
  PARAM_CONTENT_EMPTY: 参数表内容不能为空
  FILE_PARSE_ERR: 文件解析异常
  PARAM_NOT_DEFINE: 没有定义参数
  SCORE_CARD_FORMAT_ERR: 非评分卡文件格式
  TABLE_CONTENT_EMPTY: 表头内容不能为空
  TABLE_FORMAT_EMPTY: 表头格式错误
  SCORE_CARD_CONTENT_EMPTY: 评分卡内容不能为空
  COMP_RULE_EMPTY: 第{0}行变量名、变量中文名、数据类型、基准分、系数不能为空！
  COMP_RULE_INCOMPLETE: 第{0}行变量名、变量中文名、数据类型、基准分{1}填写不完整！
  COMP_SCORE_VALUE_INCOMPLETE: 第{0}行分箱、分值填写不完整！
  TABLE_TILE_NOT_DEFINE: 决策表头未定义
  TABLE_TILE_CONTENT_EMPTY: 决策表内容不能为空
  FIELD_INCOMPLETE: 字段不完整
  FIELD_LINE_INCOMPLETE: 条件行第{0}行字段不正确。
  FIELD_COL_INCOMPLETE: 条件列第{0}列字段不正确。
  RESULT_VAR_SET_ERR: 结果变量设置不一致
  PKL_MODEL_CONTENT_ERR: PKL/MODEL模型DTO内容必传项不能为空
  PKL_MODEL_NOT_EXIST: 组件ID对应的PKL/MODEL模型不存在
  PKL_MODEL_ID_NOT_EXIST: PKL/MODEL模型id不存在
  COMP_NO_NOT_EXIST: 编码已经存在
  COMP_NAME_EXIST: 组件名称已经存在
  MODEL_VALID_FAIL: 模型验证失败
  MODEL_VALID_INTERFACE_INVOKE_FAIL: 调用模型验证服务接口异常
  FILE_FOLDER_ID_NOT_EXIST: 文件夹ID不存在
  FILE_ROOT_FOLDER_NOT_DEL: 根文件夹不允许删除
  FILE_PARENT_FOLDER_NOT_EXIST: 父类文件夹不存在，请重新选择
  FILE_PARENT_FOLDER_ERR: 父类文件夹不能选择当前文件夹
  FILE_FOLDER_NAME_REPEAT: 已经存在同名文件夹
  DOC_EXIST: 分类下存在文档，请先删除文档
  VAR_CHOOSE_ERR: 请选择一个数组变量
  TEMPLATE_STATIC_INFO_ERR: 读取静态模板信息出错
  COMP_TEST_DATA_NOT_FOUND: 组件测试执行未查询到测试数据集
  COMP_TEST_INFO_NOT_FOUND: 组件测试执行未查询到组件信息
  COMP_TEST_STRATEGY_NOT_FOUND: 组件测试执行未查询到策略
  COMP_TEST_SERVICE_NOT_FOUND: 组件测试执行未查询到服务
  COMP_TEST_DOMAIN_NOT_FOUND: 组件测试执行未查询到领域
  COMP_TEST_DATA_MODEL_NOT_FOUND: 数据明细下未查询到数据模型
  COMP_TEST_DATA_DETAIL_NOT_FOUND: 没有可执行的测试明细数据
  COMP_TEST_FAIL: 组件执行失败
  COMP_TEST_ID_FAIL: 测试结果查询ID未传入
  COMP_TEST_RESULT_NOT_FOUND: 未查询到测试记录
  COMP_TEST_RESULT_STATUS_INCORRECT: 查询条件状态不正确
  COMP_TEST_PARAM_INCORRECT: 查询条件参数不正确
  FILE_EXCEL_EXPORT_ERR: 导出Excel文件异常
  COMP_TEST_RESULT_SAVE_ERR: 未查询到正常执行的数据，不允许保存为预期结果
  COMP_TEST_CHECKOUT_OWNER_ERR: 当前用户不是检出人，不允许测试
  FILE_EXCEL_READ_ERR: 读取Excel数据异常
  FILE_EXCEL_VAR_ERR: 模型特征变量Excel数据不能为空
  FILE_EXCEL_DATA_EMPTY: 模型特征变量Excel除表头之外必须包含至少一行数据
  FILE_EXCEL_TITLE_ERR: 模型特征变量Excel表头不正确
  FILE_EXCEL_TITLE_MULTI_ERR: Excel表头错误，多余字段
  FILE_EXCEL_COL_MULTI_ERR: 第{0}行存在多余的数据列，请删除
  FILE_EXCEL_VAR_ROW_ERR: 第{0}行模型特征变量为空
  FILE_EXCEL_VAR_ROW_REPEAT_ERR: 第{0}行模型特征变量与第{1}行模型特征变量重复
  FILE_EXCEL_ROW_REPEAT_EMPTY: 第{0}行数据类型为空
  FILE_EXCEL_DATA_TYPE_ERR: 数据类型不正确，数据类型范围
  FILE_MODEL_ID_ERR: 模型文件ID格式不正
  TARGET_ENV_INVOKE_FAIL: 目标环境接口调用失败
  CUSTOM_TYPE_NOT_SUPPORT: 不支持的自定义生成类型
  LOGIC_DEPENDENCY_INCOMPLETE: 逻辑依赖尚未完成
  TEST_DATA_LOCAL_OPEN_FAIL: 本地文件打开失败
  TEST_DATA_LOCAL_READ_FAIL: 本地文件读取失败
  TEST_TABLE_ONLINE_FAIL: 在线表单获取表头失败
  TEST_TABLE_IMPORT_FAIL: 导入异常
BusinessCodeMessageEnum:
  OSS_UPLOAD_ERR: Aliyun OSS对象存储上传文件异常
  OSS_DOWNLOAD_ERR: 获取Aliyun OSS对象存储文件异常
  OSS_KEY_REPEAT: 不合法的key重写参数为空
  AW3_OSS_KEY_REPEAT: AWS S3对象存储上传文件异常
  AW3_OSS_RESOURCE_ERR: 获取AWS S3对象存储文件异常
  AW3_OSS_RESOURCE_NOT_EXIST: AWS S3对象存储文件不存在
  DATA_POSITION_RULE: 位置入参只能是
  OBJECT_TYPE_MIXED: object类型不能和其它类型进行混合
  COMP_NOT_FOUND: 组件不存在或已删除
  TEMPLATE_VAR_APPEND_FAIL: 数组内追加变量信息失败，请联系技术人员
  TEMPLATE_VAR_APPEND_REPEAT: 需要追加变量的数组元素相同，无法查询，请重新操作
  FUNC_RETURN_NOT_FOUND: 未获取到函数返回值信息
  OPERATOR_EMPTY: 操作符不能为空
  OPERATOR_NOT_SUPPORT: 不支持的操作符
  FIELD_TYPE_EMPTY: 字段值类型不能为空
  FIELD_VALUE_EMPTY: 字段值不能为空
  FIELD_VALUE_INCORRECT: 字段值类型不正确
  OPERATOR_EQUAL_NOT_DEAL: 无法处理非等值操作符
  OPERATOR_RANGE_NOT_DEAL: 无法处理非范围操作符
  OPERATOR_RANGE_NOT_SUPPORT: 暂不支持的范围操作符
  OPERATOR_COLLECTION_NOT_DEAL: 无法处理非集合操作符
  OPERATOR_COLLECTION_NOT_SUPPORT: 暂不支持的集合操作符
  OPERATOR_VALUE_NOT_DEAL: 无法处理非无值操作符
  OPERATOR_VALUE_NOT_SUPPORT: 暂不支持的无值操作符
  OPERATOR_LIKE_NOT_DEAL: 无法处理非模糊操作符
  OPERATOR_LIKE_NOT_SUPPORT: 暂不支持的模糊操作符
  FIELD_NAME_EMPTY: 字段名不能为空
  FIELD_RANGE_START_EMPTY: 字段范围开始值不能为空
  FIELD_RANGE_END_EMPTY: 字段范围结束值不能为空
  OPERATOR_LIST_NOT_DEAL: 无法处理非列表操作符
  OPERATOR_LIST_EMPTY: 字段值列表不能为空
  INPUT_TYPE_RULE: 类型入参只能是
  INPUT_ARRAY_TYPE_RULE: 类型入参如果包含array那就不能再包含其它类型
  DOMAIN_MODEL_NOT_EXIST: 数据模型不存在
  STRATEGY_NOT_EXIST: 策略不存在
  DATA_NODE_EMPTY: 数据查找节点不能为空
  VAR_SPACE_NOT_EXIST: 变量空间不存在
  PERMISSION_BUSINESS_TYPE_NOT_FOUND: 【权限验证】未查询到业务类型
  PERMISSION_BUSINESS_IDS_NOT_FOUND: 【权限验证】未获取到 {0} ID
  PERMISSION_NOT_ALLOW: 【权限验证】权限不足
  PERMISSION_BUSINESS_ID_NOT_CONFIG: 【权限验证】未配置业务ID属性
  PERMISSION_BUSINESS_ID_OBTAIN_FAIL: 【权限验证】未获取到
  PERMISSION_BUSINESS_ID_ERR: 参数业务ID错误
  PERMISSION_COMP_NOT_FOUND: 根据组件ID[{0}]未查询到组件
  PERMISSION_STRATEGY_COMP_NOT_FOUND: 根据组件ID[{0}]未查询到策略组件关系
  PERMISSION_DOMAIN_ROLE_NOT_FOUND: 未查询到领域的角色
  PERMISSION_BUCKET_NOT_FOUND: 未查询到细分信息
  PERMISSION_STRATEGY_NOT_FOUND: 未查询到策略信息
  PERMISSION_BUCKET_USE_NOT_FOUND: 未查询到细分use信息
  PERMISSION_FOLDER_NOT_FOUND: 未查询到文件夹信息
  PERMISSION_BUSINESS_TYPE_NOT_CONFIG: 未配置对应的业务类型
  PERMISSION_STRATEGY_NOT_CONFIG: 未查询到策略配置
  PERMISSION_TYPE_ERR: 权限类型传入错误
  PERMISSION_DATA_NOT_FOUND: 未查询到数据
  PERMISSION_SPACE_TYPE_ERR: 空间类型错误
  PERMISSION_ROLE_EXIST: 角色权限已存在
  PERMISSION_RESOURCE_NOT_FOUND: 未查询到相关资源
  PERMISSION_USER_ROLE_NOT_FOUND: 未查询到当前角色信息
  PERMISSION_ADMIN_NOT_REMOVE: 无法移除成员的超级管理员角色：该成员为领域唯一的超级管理员
  PERMISSION_USER_CHOOSE_NOT_REMOVE: 选定成员无法被移除：该成员为领域唯一的超级管理员
  SYS_LOG_DATE_ERR: 系统日志时间查询条件转换异常
  SYS_PARAM_TYPE_NOT_EXIST: 参数类型不存在
  SYS_PARAM_TYPE_ADD_NOT_ALLOW: 不能新增参数类型为内置参数类型的系统参数
  SYS_PARAM_TYPE_ADD_ERR: 不能新增参数类型为内置参数类型的系统参数
  SYS_PARAM_NAME_REPEAT: 参数名不能重复
  SYS_PARAM_NAME_CN_REPEAT: 参数中文名不能重复
  SYS_PARAM_ID_NOT_EXIST: 系统参数id不存在
  SYS_PARAM_BUILD_IN_NOT_MODIFY: 内置参数不允许编辑
  SYS_PARAM_NAME_NOT_MODIFY: 不可以修改参数名
  SYS_PARAM_TYPE_NOT_MODIFY: 不可以修改数据类型
  SYS_DATA_TYPE_NOT_EXIST: 数据类型不存在
  SYS_DATA_INT_RULE_ERR: 为int类型，类型填写异常
  SYS_DATA_DOUBLE_RULE_ERR: 为double类型，类型填写异常
  SYS_DATA_BOOLEAN_RULE_ERR: 为boolean类型，类型填写异常
  SYS_DATA_DATE_RULE_ERR: 为date类型，类型填写异常
  SYS_DATA_DATETIME_RULE_ERR: 为datetime类型，类型填写异常
  SYS_TARGET_NAME_REPEAT: 已存在同名配置
  SYS_PK_EMPTY: 更新时主键不能为空
  SYS_CONFIG_ID_ERR: 配置id错误或数据不存在
  SYS_ENV_PARAM_EMPTY: 环境配置参数为空
  SYS_TARGET_ENV_PARAM_EMPTY: 环境配置参数：目标环境名称不能为空
  SYS_TARGET_ENV_PARAM_RULE_ERR: 环境配置参数：目标环境名称不能超过50个字符
SYS_TENANT_REPEAT_NANE: 租户名已存在，不允许重复使用。
SYS_TENANT_MONGODB_REPEAT_NANE: 报文存储库已存在，不允许重复使用。
SYS_TENANT_NOT_QUERY: 未查询到租户信息。
SYS_TENANT_USE_NOT_UPDATE: 租户已被使用，不允许修改。
SYS_TENANT_USE_NOT_DELETE: 租户已被使用，不允许删除。
SYS_TENANT_CONNET_NOT_DELETE: 已关联用户，不允许删除
SYS_TENANT_USE_NOT_EMPTY: 用户不能为空
METHOD_CANNOT_DELETE: 方法正在使用，不可被删除！
METHOD_NAME_CANNOT_REPEAT: 方法名称不允许重复
METHOD_PATH_CANNOT_REPEAT: 方法路径不允许重复
CHINESE_NAME_REPETITION: 收藏代码块中文名重复，请修改
CHINESE_NAME_NOT_NULL: 收藏代码块中文名不能为空
ID_MESSAGE_IS_NULL: 删除id信息为空
  OutsideCodeMessageEnum:
    OUTSIDE_SERVICE_ID_NOT_EXIST: 外部服务id不存在
    OUTSIDE_SERVICE_CALL_FAIL: 查询失败请稍后重试
    OUTSIDE_SERVICE_CACHE_DURATION: 数据缓存期填写后必须填写缓存类型
    OUTSIDE_SERVICE_CODE_REPEAT: 服务编码不能重复
    OUTSIDE_SERVICE_NAME_REPEAT: 服务名称不能重复
    OUTSIDE_SERVICE_STATUS_NOT_MODIFY: 不允许修改处于“启用”/“停用”状态外部服务的编码
    OUTSIDE_SERVICE_SCOPE_SAME_DOMAIN: 外部服务的作用域不能存在相同的领域
    OUTSIDE_SERVICE_DOMAIN_NOT_EXIST: 决策领域信息不存在
    OUTSIDE_SERVICE_SCOPE_MULTI_DOMAIN: 外部服务的作用域出现重复领域
    OUTSIDE_SERVICE_EMPTY: 外部服务信息不能为空
    OUTSIDE_SERVICE_AUTH_EMPTY: 未添加任何授权信息
    OUTSIDE_SERVICE_DOMAIN_AUTH_SUCCESS: 领域服务授权关系添加成功
    OUTSIDE_SERVICE_VAR_AUTH_SUCCESS: 外部服务-变量空间服务授权关系添加成功
    OUTSIDE_SERVICE_IMPORT_NOT_DEL: 该领域已经引入本服务，无法删除授权
    OUTSIDE_SERVICE_STATUS_ERR: 状态参数有误
    OUTSIDE_SERVICE_SCRIPT_PARSE_FAIL: 外部服务表达式脚本解析失败
    OUTSIDE_SERVICE_RESPONSE_DATA_NONE: 请定义外部服务响应数据
    OUTSIDE_SERVICE_RESPONSE_CODE_FAIL: 外部服务响应码类编译失败
    OUTSIDE_SERVICE_STATUS_NOT_EXIST: 外部服务状态不存在
    OUTSIDE_SERVICE_NOT_FOUND: 未查询到外部服务
    OUTSIDE_SERVICE_ID_EMPTY: 服务id不能为空
    OUTSIDE_SERVICE_QUERY_ERR: 查询异常
    OUTSIDE_SERVICE_CALL_TIME_EMPTY: 单次调用超时时间不能为空
    OUTSIDE_SERVICE_TOTAL_TIME_EMPTY: 服务总超时时间不能为空
    OUTSIDE_SERVICE_RETRY_TIME_EMPTY: 重试次数不能为空
    OUTSIDE_SERVICE_CACHE_TIME_EMPTY: 数据缓存期不能为空
    OUTSIDE_SERVICE_REQ_PARAM_EMPTY: 请求参数配置未定义，请配置
    OUTSIDE_SERVICE_RES_PARAM_EMPTY: 响应数据未定义，请配置
    OUTSIDE_SERVICE_RES_CODE_EMPTY: 响应码未定义，请配置
    OUTSIDE_SERVICE_RES_CODE_CONFIG_EMPTY: 响应码({0})未定义，请配置
    OUTSIDE_SERVICE_RES_CODE_CONFIG_ERR: 响应码({0})表达式有误，请修改
    OUTSIDE_REPORT_QUERY_FAIL: 报表查询失败
    OUTSIDE_REPORT_EXPORT_FAIL: 报表导出失败
    OUTSIDE_SERVICE_EXPORT_FAIL: mock测试时需要选择mock数据集
    OUTSIDE_SERVICE_QUERY_SUCCESS: 查询成功，未查得
    OUTSIDE_SERVICE_DATA_INCOMPLETE: 在系统参数中未查询到配置“外部服务数据缓存天数”，请检查数据完整性
    OUTSIDE_SERVICE_PARAM_ERR: 服务不存在或参数错误
    OUTSIDE_SERVICE_TARGET_ERR: 目标服务器不存在或参数错误
    OUTSIDE_SERVICE_FLOW_CODE_ERR: 发布流水号错误
    OUTSIDE_SERVICE_DEPLOY_AGAIN_ERR: 非一键发布失败状态，不能执行再次发布
    OUTSIDE_SERVICE_DEPLOY_RECORD_NONE: 发布记录不存在！
    OUTSIDE_SERVICE_TARGET_ENV_NONE: 目标环境不存在，不能执行再次发布！
    OUTSIDE_SERVICE_DEPLOY_NOT_ALLOW: 非一键发布类型不可发布！
    OUTSIDE_SERVICE_DEPLOY_TIMEOUT: 发布进度数据已超时！
    OUTSIDE_SERVICE_PACKAGE_MISS: 一键发布包丢失！
    OUTSIDE_SERVICE_PACKAGE_ERR: 一键发布包丢失！请重新生
    OUTSIDE_SERVICE_PACKAGE_UNABLE: 打包文件不可用，不能执行发布了！
    OUTSIDE_SERVICE_PACKAGE_FAIL: 打包失败或包不可用，不能执行发布了！
    OUTSIDE_SERVICE_DEPLOY_NOT_QUERY: 非一键发布类型不可查！
    OUTSIDE_SERVICE_STATUS_NOT_QUERY: 非发布中状态不可查！
    OUTSIDE_SERVICE_DEPLOY_DATA_NOT_EXIST: 流水号错误或发布数据不存在！
    OUTSIDE_SERVICE_PACKAGE_DOWNLOAD_FAIL: 非打包发布或打包不成功，不能执行发布包下载！
    OUTSIDE_SERVICE_PACKAGE_GENERATE_FAIL: 打包文件不可用，请重新生成！
    OUTSIDE_SERVICE_PACKAGE_NOT_EXIST: 发布包不存在，请重新生成！
    OUTSIDE_SERVICE_PRIVATE_KEY_NOT_PUBLISH: 目标环境未初始化发布私钥！
    OUTSIDE_SERVICE_FILE_IMPORT_INCOMPLETE: 导入的文件缺少基本信息！
    OUTSIDE_SERVICE_ZIP_FORMAT_ERR: 压缩包结构不正确，没有数据文件！
    OUTSIDE_SERVICE_FILE_DEC_ERR: 文件解密失败
    OUTSIDE_SERVICE_FILE_DEC_COMPLETE: 文件解析完成
    OUTSIDE_SERVICE_PACKAGE_UPLOAD_FAIL: 上传发布包失败
    OUTSIDE_SERVICE_INTERFACE_INVOKE_FAIL: 查询状态接口异常
    OUTSIDE_DOMAIN_ID_NOT_EXIST: 领域id不存在
    OUTSIDE_VAR_ID_NOT_EXIST: 变量空间id不存在
    OUTSIDE_MOCK_REPEAT: 同一个服务下Mock数据集名称不能重复
    OUTSIDE_MOCK_COLLECTION_NOT_EXIST: Mock集合id不存在
    OUTSIDE_MOCK_COLLECTION_EMPTY: Mock集合id不能为空
    OUTSIDE_MOCK_RES_EMPTY: 响应报文不能为空
    OUTSIDE_INPUT_REPEAT: '入参配置不能重复:'
    OUTSIDE_MOCK_DETAIL_NOT_EXIST: 外部服务Mock明细id不存在
    OUTSIDE_MOCK_DETAIL_READ_ERR: 读取外部服务Mock明细数据excel的sheetName异常！
    OUTSIDE_MOCK_SHEET_ERR: excel中的sheetName为空！
    OUTSIDE_MOCK_TITLE_ERR: 读取外部服务Mock明细数据excel的表头异常！
    OUTSIDE_MOCK_TITLE_EMPTY: 表头不能为空！
    OUTSIDE_MOCK_TITLE_NOT_EXIST: 表头不存在！
    OUTSIDE_MOCK_TITLE_ORDER_ERR: 表头顺序错误，必须是
    OUTSIDE_MOCK_EXCEL_DATA_ERR: 读取外部服务Mock明细数据excel的数据异常！
    OUTSIDE_MOCK_EXCEL_DATA_EMPTY: 外部服务Mock明细数据不能为空！
    OUTSIDE_MOCK_EXCEL_DATA_NOT_MATCH: excel中的值不能超过表头！
    OUTSIDE_MOCK_EXCEL_VAR_EXPORT_ERR: 导出变量Excel文件异常！
    OUTSIDE_MOCK_VAR_TYPE_NOT_EXIST: 变量的字段类型不存在！
    OUTSIDE_MOCK_VAR_TYPE_NOT_MATCH: 选择的变量类型只能是int类型的整数！
    OUTSIDE_MOCK_VAR_TYPE_DOUBLE_NOT_MATCH: 选择的变量类型只能是double类型的小数！
    OUTSIDE_MOCK_VAR_TYPE_FORMAT_ERR: { 0 }选择的变量类型只能是{1}格式！
    OUTSIDE_MOCK_ID_NOT_EXIST: 服务Mock配置id不存在！
    OUTSIDE_MOCK_RES_CODE_NOT_FOUND: 未查询到响应码类型！
    OUTSIDE_MOCK_RES_CODE_NOT_SUPPORT: 不支持的响应码类型！
    OUTSIDE_MOCK_STRATEGY_ID_EMPTY: 新策略ID不能为空！
    OUTSIDE_MOCK_SOURCE_STRATEGY_ID_EMPTY: 源策略ID不能为空！
    OUTSIDE_MOCK_REQ_PARAM_REPEAT: Header、url参数、Body中的参数名：{0}出现重复！
    OUTSIDE_MOCK_SYS_PARAM_NONE: 系统参数不存在！
    OUTSIDE_REQ_PARAM_EMPTY: 请求参数不能为空！
    OUTSIDE_BODY_PARAM_EMPTY: body参数不能为空！
    OUTSIDE_CONFIG_PARAM_EMPTY: 服务请求参数配置id不存在！
    OUTSIDE_RES_EMPTY: 响应数据结构不能为空！
    OUTSIDE_RES_DEMO_EMPTY: 响应数据示例不能为空！
    OUTSIDE_RES_EXIST: 服务中已经存在响应数据结构了！
    OUTSIDE_RES_SHEET_NAME_READ_ERR: 读取外部服务响应数据excel的sheetName异常！
    OUTSIDE_RES_SHEET_NAME_EMPTY: excel中的sheetName为空！
    OUTSIDE_RES_SHEET_NAME_RULE: 外部服务响应数据excel的sheetName必须是
    OUTSIDE_RES_TITLE_READ_ERR: 读取外部服务响应数据excel的表头异常
    OUTSIDE_DATA_MODEL_DATA_READ_ERR: 读取决策数据模型excel的数据异常
    OUTSIDE_DATA_EMPTY: 数据不能为空
    OUTSIDE_OBJECT_NAME_RULE: 对象名称不能为
    OUTSIDE_OBJECT_SUB_ERR: 取子对象级别错误
    OUTSIDE_TITLE_NOT_SUPPORT: 出现不支持的表头：
    OUTSIDE_TITLE_ORDER_ERR: 表头排序不正确：
    OUTSIDE_HEADER_MISS: 缺失
    OUTSIDE_RES_PARAM_EMPTY: 响应码参数不能为空
    OUTSIDE_RES_TYPE_EMPTY: 类型不能为空
    OUTSIDE_RES_TYPE_NOT_EXIST: 外部服务响应类型不存在
    OUTSIDE_EXP_VAR_NOT_EXIST: 表达式中使用的变量{0}在响应参数中不存在！
    OUTSIDE_EXP_VAR_NOT_MATCH: 表达式中使用的变量{0}的类型与响应参数中定义的不一致！
    OUTSIDE_SCRIPT_PARSE_FAIL: 外部服务表达式脚本解析失败
    OUTSIDE_RES_DATA_EMPTY: 请定义外部服务响应数据
    RSA_ENC_FAIL: 加密失败
    RSA_DEC_FAIL: 解密失败
    OUTSIDE_STRATEGY_ID_NOT_EXIST: 策略id不存在
    OUTSIDE_STRATEGY_SAME_NAME_EXIST: 同一个策略下接收对象名称不能重复
    OUTSIDE_STRATEGY_SAME_NAME_CN_EXIST: 同一个策略下接收对象中文名称不能重复
    OUTSIDE_STRATEGY_SAME_OBJECT_EXIST: 同一个策略下接收对象名称不能重复
    OUTSIDE_STRATEGY_SAME_OBJECT_CN_EXIST: 同一个策略下接收对象中文名称不能重复
    OUTSIDE_IMPORT_OBJECT_ID_NOT_EXIST: 外部服务引入对象id不存在
    OUTSIDE_RECEIVE_OBJECT_NAME_REPEAT: 接收对象名称{0}出现重复
    OUTSIDE_RECEIVE_OBJECT_NAME_CN_REPEAT: 接收对象中文名称{0}出现重复
    OUTSIDE_IMPORT_SERVICE_ID_NOT_EXIST: 引入服务id不存在
    OUTSIDE_STRATEGY_NAME_REPEAT: 同一个策略下接收对象名称：{0}出现重复
    OUTSIDE_STRATEGY_NAME_CN_REPEAT: 同一个策略下接收对象中文名称：{0}出现重复
    METHOD_CANNOT_DELETE: 方法正在使用，不可被删除！
    METHOD_NAME_CANNOT_REPEAT: 方法名称不允许重复
    METHOD_PATH_CANNOT_REPEAT: 方法路径不允许重复
    Chinese_Name_Repetition: 收藏代码块中文名重复，请修改
    Chinese_Name_NOT_NULL: 收藏代码块中文名不能为空
    ID_MESSAGE_IS_NULL: 删除id信息为空
  ExceptionMessageEnum:
    CLIENT_ABORT_EXCEPTION: 服务正常处理，客户端断开连接
    HTTP_MESSAGE_Not_READABLE_EXCEPTION: 请求参数错误
    PARAM_ERR_OR_STRATEGY_NOT_EXIST: 参数错误或策略不存在！
    TOPIC_NOT_NULL: topic不能为空！
    DATABASE: 数据库
    PROVINCIALISM_INCOMPATIBILITY: 方言不适配！需要适配后重试！
    LENGTH_RULE: 长度不能大于外部传入字段的hash长度
    LENGTH_MUST_LARGER_THEN_OR_EQUAL_TO_ONE: 长度必须大于等于1！
    LENGTH_RULE_TWO: 长度不能大于部字段加固定字段的hash长度
  SysMethodStateEnum:
    STOP: 停用
    START: 启用
  SysMethodTypeRangeEnum:
    External_Number_REQUEST_PARAM: 外数请求参数
    DATA_Desensitization: 数据脱敏
    TEST_DATA_CREATE: 测试数据生成
  ComponentTypeEnum:
    RULE: 规则集
    SINGLE_RULE: 规则
    TABLE: 决策表
    TREE: 决策树
    MODEL: 评分卡
    FUNCTION: 自定义函数
    SCRIPT: 自定义脚本
    SERVICE: 外部服务
    SUBFLOW: 决策流
    MAINFLOW: 主流程
    PMML: PMML模型
    PYTHON_PKL: PKL模型
    PYTHON_MODEL: MODEL模型
    LOOKUP: 参数表
    SPLIT: 串行分支
    PARALLEL: 并行分支
    LOOK_TABLE: 查找表
    SINGLE_AXIS_TABLE: 单轴决策表
    DOUBLE_AXIS_TABLE: 双轴决策表
    ABTESTPLAN: abTestplan
    ABTESTRANDOM: abTestrandom
    ABTEST: abTest
    BLAZE: blaze决策模块
  DomainRosterExcelHeaderEnum:
    LIST_VALUE: 名单值
    EXPIR_DATA: 到期时间
    DESCRIPTION: 描述
  StrategyPropertiesEnum:
    PROPERT_INFO: 属性信息
    AB_TEST_REF: A/B测试引用
    OUTSIDE_REF: 外部服务引用
    COMMON_DES_REF: 公共决策模块引用
    VAR_SERVER_REF: 变量服务引用
    TEST: 测试信息
    LIFECYCLE_LIST: 生命周期列表
    ONLINE_INFO: 上线信息
    ONLINE_TYPE: 上线方式
    ONLINE_TIME: 预计上线时间
    ONLINE_END_TIME: 终止时间
    ONLINE_FLOW_RADIO: 流量比例
    ONLINE_DESC: 上线描述
    ONLINE_ROLLBACK: 回滚策略
    ONLINE_VIEW: 查看
    SERVER_PRE: 服务
    MODEL_PRE: 模块
    NAME: 名称
    CODE: 编码
    DESC: 描述
    CREATED_TIME: 创建时间
    CREATED_USER: 创建人
    BUCKET_NAME: 细分名称
    BUCKET_STATUS: 细分状态
    BUCKET_DESC: 细分描述
    TEST_PASS_RATE: 最后测试通过率
    TEST_COVER_RATE: 测试覆盖率
    TEST_NOT: 未测试
    STRATEGY_VERSION: 版本号
    STRATEGY_STATUS: 版本状态
    STRATEGY_SOURCE: 版本来源
    STRATEGY_COPY_TO: 复制于
    DATA_MODEL: 数据模型
    STRATEGY_VERSION_DESC: 版本描述
    STRATEGY_DESC: 策略描述
    STRATEGY_VERSION_ATTR: 版本归属
    STRATEGY_VERSION_ATTR_TYPE: 归属类型
    STRATEGY_LIFE_STATUS: 状态
    STRATEGY_LIFE_TYPE: 操作类型
    STRATEGY_LIFE_USER: 操作人
    STRATEGY_LIFE_TIME: 操作时间
    STRATEGY_LIFE_REMARK: 备注
CodeGenerateErrorCodeEnum:
  COMPONENT_VALIDATE_ERROR: 组件校验失败
  COMPONENT_SCORECARD_RESULT_VALIDATE_ERROR: 评分卡输出定义配置不完整
  COMPONENT_PMML_NOSCORE_RESULT_VALIDATE_ERROR: pmml非评分卡输出定义配置不完整
  COMPONENT_PMML_SCORECARD_RESULT_VALIDATE_ERROR: pmml评分卡输出定义配置不完整
  COMPONENT_PYTHON_PKLANDMODEL_RESULT_VALIDATE_ERROR: pkl or model模型输出定义配置不完整
  EXP_VALIDATE_ERROR: 表达式校验失败
  EXP_PARAM_LOCAL_VALIDATE_ERROR: 参数or本地变量【{0}】已经不存在了
  EXP_INVOK_COM_NOTFOUND_ERROR: 未查询到组件【{0}】调用得自定义函数【{1}】的定义信息，请核对!
  EXP_INVOK_COM_VALIDATE_ERROR: 组件【{0}】调用得自定义函数【{1}】校验出错:{2}
  EXP_DOUBLE_TABLE_OPERATE_ERROR: 没有定义的的双轴决策表操作符号定义:{0}
  EXP_DOUBLE_TABLE_ACTION_VARTYPE_ERROR: 不支持的的双轴决策表动作列变量类型:{0}
  EXP_SINGLE_TABLE_OPERATE_ERROR: 没有定义的的单轴决策表操作符号定义:{0}
  EXP_ACTION_ASSIGN_VARTYPE_ERROR: 不支持的的动作赋值变量类型:{0}
  EXP_SINGLE_TABLE_INVOKCOM_PARAM_ERROR: 第【{0}】行数据执行组件【{1}】参数列表选择不完整
  EXP_FLOW_NODE_ERROR: "决策流节点校验失败: FlowName:{0},nodeName:{1},nodeId:{2} {3}"
  EXP_PARAM_NUMBER_MATCH_ERROR: 组件[name={0},componentId:{1}]的参数数量和组件定义的参数数量不一致
  EXP_UNKNOWN_NODE_PARAMIN_ERROR: 未知的{0}模块调用入参绑定类型:{1}
  EXP_FUNCTIONRETURN_ASSIGN_ERROR: 函数返回值缺少赋值操作
  EXP_FUNCTIONRETURN_VOID_NOTASSIGN_ERROR: 函数[{0}]无返回值，不能对返回值进行赋值操作
  EXP_RESONCODE_CANTFIND_ERROR: 原因码定义缺失【{0}】
  EXP_TREE_OPERA_ERROR: 没有定义的的决策树操作符号:【{0}】
  EXP_NOT_FILL_INFO_ERROR: 有未填写的内容信息【{0}】
  EXP_NOT_FIND_FUNCTION_ERROR: 未查询到组件【{0}】调用得自定义函数【{1}】的定义信息，请核对!
  EXP_INVOK_FUNCTION_ERROR: 组件【{0}】调用得自定义函数【{1}】校验出错:{2}
  EXP_DICT_TYPE_ERROR: 不支持的字典类型【{0}】
  EXP_DICT_CODE_TYPE_NOTMATCH_ERROR: 字典项字典编码类型不匹配【{0}】
  EXP_ARRAYLOOP_DYNAMICOBJECTS_ERROR: 循环内组装dynamicObjects异常，请联系技术人员
  EXP_SORT_ATTRI_NOTNULL_ERROR: 排序属性不能为空【{0}】
  EXP_TWO_SORT_ATTRI_NOTNULL_ERROR: 对象数组排序两个属性不能相同【{0}】
  EXP_SORT_TYPE_NOTNULL_ERROR: 排序方式不能为空【{0}】
  ENGINE_MODEL_INPUT_CODE_GENERATE_ERROR: input变量模型代码生成失败
  ENGINE_MODEL_OUTPUT_CODE_GENERATE_ERROR: output变量模型代码生成失败
  ENGINE_MODEL_ENGINEVARS_CODE_GENERATE_ERROR: engine引擎变量模型代码生成失败
  ENGINE_MODEL_EXTERNAL_CODE_GENERATE_ERROR: externalData外数模型代码生成失败
  ENGINE_MODEL_COMMONDATA_CODE_GENERATE_ERROR: commonData共享模块变量模型代码生成失败
  COMPONENT_PARAMETER_CODE_GENERATE_ERROR: 组件参数代码生成失败
  COMPONENT_LOCALVARS_CODE_GENERATE_ERROR: 组件本地变量代码生成失败
  COMPONENT_CODE_GENERATE_ERROR: 组件代码生成失败
  ENGINE_MODEL_INPUT_COMPILE_ERROR: input变量模型编译失败
  ENGINE_MODEL_OUTPUT_COMPILE_ERROR: output变量模型代码生成失败
  ENGINE_MODEL_ENGINEVARS_COMPILE_ERROR: engine引擎变量模型代码生成失败
  ENGINE_MODEL_EXTERNAL_COMPILE_ERROR: externalData外数模型代码生成失败
  ENGINE_MODEL_COMMONDATA_COMPILE_ERROR: commonData共享模块变量模型代码生成失败
  ENGINE_MODEL_VARPROCESS_COMPILE_ERROR: externalVar变量服务模块变量模型代码生成失败
  ENGINE_COMPONENT_COMPILE_ERROR: 组件编译失败;{0}
  ENGINE_PUBLISH_COMPRESS_ERROR: 生成策略资源压缩包失败,策略id:{0};{1}
  ENGINE_CONTAINER_COMPONENT_LOAD_ERROR: 容器初始化加载组件失败
```