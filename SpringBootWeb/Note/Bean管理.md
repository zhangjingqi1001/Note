# Bean管理



# 一、获取Bean



​       **默认情况下，Spring项目启动时，会把Bean都创建好放在IOC容器中，如果主要获取这些Bean，可以通过如下方式：**

*  **根据name获取bean**： Object getBean（String name）
*  **根据类型获取bean**： \<T\> T getBean(Class \<T\> requiredType) 
*   **根据name获取bean（带类型转换）**： \<T\> T getBean(String name，Class \<T\> requiredType) 



```java
//  IOC容器对象
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testBean(){
//      TODO  根据bean名称获取   若没指定bean名称，默认类名首字母小写
        DeptController deptControllerBean1 =(DeptController) applicationContext.getBean("deptController");
        System.out.println(deptControllerBean1); //com.zhangjingqi.controller.DeptController@249b54af

//      TODO 根据bean的类型获取
        DeptController deptControllerBean2 = applicationContext.getBean(DeptController.class);
        System.out.println(deptControllerBean2);//com.zhangjingqi.controller.DeptController@249b54af

//      TODO 根据bean的名称 及 类型获取
        DeptController deptControllerBean3 = applicationContext.getBean("deptController",DeptController.class);
        System.out.println(deptControllerBean3);//com.zhangjingqi.controller.DeptController@249b54af

    }
```



​       **上述所说的【Spring项目启动时，会把其中的bean创建好】还会受到作用域及延迟初始化影响，这里主要针对于默认的单例非延迟加载的bean而言。**





# 二、 Bean的作用域



**Spring支持五中作用域，后三种在Web环境下才生效**：

|    作用域     |                      说明                       |
| :-----------: | :---------------------------------------------: |
| **singleton** | 容器内同名称的bean只有一个实例（单例）（默认）  |
| **prototype** |    每次使用该bean时会创建新的实例（非单例）     |
|    request    | 每个请求范围内会创建新的实例（Web环境中，了解） |
|    session    | 每个会话范围内会创建新的实例（Web环境中，了解） |
|  application  | 每个应用范围内会创建新的实例（Web环境中，了解） |



**singleton模式下bean对象情况，并且在容器启动的时候创建好的**

```java
@Test
public void testScope(){
    for (int i=0;i<10;i++){
        DeptController deptControllerBean2 = applicationContext.getBean(DeptController.class);
        System.out.println(deptControllerBean2);
    }
}
```

>  com.zhangjingqi.controller.DeptController@586728e8
>  com.zhangjingqi.controller.DeptController@586728e8
>  com.zhangjingqi.controller.DeptController@586728e8
>  com.zhangjingqi.controller.DeptController@586728e8
>  com.zhangjingqi.controller.DeptController@586728e8
>  com.zhangjingqi.controller.DeptController@586728e8
>  com.zhangjingqi.controller.DeptController@586728e8
>  com.zhangjingqi.controller.DeptController@586728e8
>  com.zhangjingqi.controller.DeptController@586728e8
>  com.zhangjingqi.controller.DeptController@586728e8



**我们也可以在第一次使用的时候实例化 @Lazy**

​    代表延迟初始化，直到第一次使用的时候

```java
@Lazy //
@RestController
@Slf4j
public class DeptController {...}
```



**通过@Scope注解来进行配置作用域**

```java
@Scope("prototype")
@RestController
@Slf4j
public class DeptController {}
```

>  com.zhangjingqi.controller.DeptController@7f6b57f2
>  com.zhangjingqi.controller.DeptController@144ee8a7
>  com.zhangjingqi.controller.DeptController@52b32b70
>  com.zhangjingqi.controller.DeptController@18c820d2
>  com.zhangjingqi.controller.DeptController@3d3930fe
>  com.zhangjingqi.controller.DeptController@5e51ec2e
>  com.zhangjingqi.controller.DeptController@15f2a43f
>  com.zhangjingqi.controller.DeptController@4c65d8e3
>  com.zhangjingqi.controller.DeptController@382faf51
>  com.zhangjingqi.controller.DeptController@69ce14e6



## 2.1 注意事项

*   默认singleton的bean，在容器启动时被创建，可以使用@Lazy注解来延迟初始化（延迟到第一次使用时）
*  prototype，每一次使用该bean的时候都会创建一个新的实例
*  实际开发当中，绝大部分的bean是单例的，也就是说绝大部分的bean不需要scope属性的



# 三、第三方Bean

​     项目开发中，自己开发的类使用@Component以及其三个衍生注解@Controller、@Service、@Repository注入即可

 

​       **但是还有一种情况是第三方提供的，比如依赖**

比如：dom4j就是第三方组织提供的。 dom4j中的SAXReader类就是第三方编写的。

​     如果我想将SAXReader对象存入到容器，需要在SAXReader类添加@Component注解，但是这是第三方bean，我们是无法修改的是不能添加注解的。

``` xml
        <dependency>
            <groupId>org.dom4j</groupId>
            <artifactId>dom4j</artifactId>
            <version>2.1.3</version>
        </dependency>
```



​       **如果要管理的bean对象来自于第三方（不是自定义的），是无法用@Component及衍生注解生命bean的，此时需要用到@Bean注解**

​        启动类也是配置类，我们完全可以在这里进行注入

```java
//Filter是javaweb三大组件之一，不是Spring提供的，如果想要使用三大组件，需要添加这个注解
@ServletComponentScan
@SpringBootApplication
public class SpringbootWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWebApplication.class, args);
    }
    //声明第三方bean
    @Bean //将当前方法的返回值对象交给IOC容器管理, 成为IOC容器bean
    public SAXReader saxReader(){
        return new SAXReader();
    }
}
```



**测试是否可以**

​    创建xml文件，解析下面内容

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<emp>
    <name>Tom</name>
    <age>18</age>
</emp>
```

  挺完美的

```java
@Autowired
private SAXReader saxReader;
@Test
public void testBean2() throws DocumentException {
    Document document = saxReader.read(this.getClass().getClassLoader().getResource("1.xml")
            );

    Element rootElement = document.getRootElement();
    String name = rootElement.element("name").getText();
    String age = rootElement.element("age").getText();
    System.out.println(name + " : " + age); // Tom : 18
}
```



​    但是在Spring项目中，我们一般会**保证启动类的纯粹性**，让启动类仅仅是启动类，我们把其他的配置单独列出来

```java
@Configuration //配置类 (在配置类当中对第三方bean进行集中的配置管理)
public class CommonConfig {
    //声明第三方bean
    @Bean //将当前方法的返回值对象交给IOC容器管理, 成为IOC容器bean
//通过@Bean注解的name/value属性指定bean名称, 如果未指定, 默认是方法名
    public SAXReader reader(DeptService deptService) {
        System.out.println(deptService);
        return new SAXReader();
    }
}
```