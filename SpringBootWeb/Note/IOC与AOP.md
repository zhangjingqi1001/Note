# IOC AOP



# 一、 分层解耦



* 内聚： 软件中各个功能模块内部的功能联系
* 耦合： 衡量软件中各个层/模块之间的依赖、关联的程度
* 软件设计原则：高内聚、低耦合



​     **控制反转**：Inversion Of Control,简称IOC。对象的创建控制权由程序自身转移到外部（容器），这种思想成为控制反转

​     **依赖注入**：Dependency Injection，简称DI。容器为应用程序提供运行时，所依赖的资源，称为依赖注入。

​     **Bean对象**： IOC容器中创建、管理的对象，称为bean

 



## 1.1 IOC - 控制反转 详细

把某个对象交给IOC容器管理，需要添加如下注解之一：

|    注解     |        说明        |                      位置                       |
| :---------: | :----------------: | :---------------------------------------------: |
| @Component  | 生命bean的基础注解 |           不属于以下三类时，用此注解            |
| @Controller | @Component衍生注解 |                  标注在控制器                   |
|  @Service   | @Component衍生注解 |                 标注在业务类上                  |
| @Repository | @Component衍生注解 | 标注在数据访问类上（由于与Mybatis整合，用的少） |



* 声名bean的时候，可以通过value属性指定bean的名字，如果没有指定，默认是类名首字母小写

  

*  使用以上四个注解都可以生命bean，但是在Springboot集成web开发中，声名控制器bean只能用@Controller 



* bean的四大注解想要生效，**需要被组件扫描注解@ComponentScan扫描**

  

*  @ComponentScan注解虽然没有显示配置，但是实际上已经包含在了启动类生命注解@SpringBootApplication中，默认扫描的范围是启动类所在包及其子包

    如下包名是从“java”包后开始的，但是下面这种不推荐，我们希望的是按照Spring的规范，将包设置在启动类所在包及其子包

```java
@ComponentScan({"dao","com.zhangjingqi"})
```





## 1.2 DI - 依赖注入 详解

 

**@Autowired 注解**，**默认是按照类型进行**的，如果存在多个相同的bean，会报错。



 EmpServiceA 实现 EmpService类，EmpServiceB 实现 EmpService类，我们在某个地方注入EmpService对象时便会出现注入错误。



**解决方案**

* **@Primary  设置bean的优先级**

​        如果我们想要哪个bean填入容器，可以在类名之上添加@Primary



*  **@Qualifier**  指定bean的名字

```java
   @Qualifier("empServiceA")
   @Autowired
   private EmpService empService;
```



* **@Resource** 按照名称注入

   **@Autowired 注解默认按照类型注入，@Resource默认按照类名进行注入**

```java
   @Resource(name = "empServiceB")
   private EmpService empService;
```

 



# 二、AOP

## 2.1 了解

Spring的第二大核心，第一大核心是IOC



**AOP**：面向切面编程、面向方面编程，其实就是面向特定方法编程



**实现**：

* **动态代理是面向切面编程最主流的实现。**而SpringAOP是Spring框架的高级技术，目的是在管理bean对象的过程中，主要通过底层的动态代理机制，对特定的方法进行编程





* **为什么要面向方法编程？**

​      **场景**：案例部分功能运行较慢，定位执行耗时较长的业务方法，此时需要统计每一个业务方法的执行耗时，找到耗时较长的业务进行优化

​    按照之前的方式，就是在方法开始前和开时候分别获取一个时间，两个时间相减就是执行耗时，但是这种方式是非常繁琐的

![image-20230517160748642](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230517160748642.png)



**如果我们基于AOP，面向方法编程，我们可以做到在不改动原始方法的基础上，来针对原始的方法进行编程，可以是对原始方法功能的增强，也可以改变原始方法的功能**



比如我们现在要统计方法的耗时，我们只需要**定义一个模板方法**，将公共的代码定义在模板方法中

原始业务方法在这里指的是需要统计执行耗时的业务方法。而这样面向一个或者多个方法进行编程，就称为**面向切面编程**

比如我们调用list()方法，此时并不会直接执行原始的list方法，而是自动的去执行模板方法。

**模板中所定义的代码逻辑其实是创建出来的代理对象方法中的逻辑**

![image-20230517161356224](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230517161356224.png)









## 2.2  快速入门 - AOP 开发步骤

**需求**：统计各个业务层方法执行耗时



### 2.2.1 Maven依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```



### 2.2.2 代码实现

 **针对于特定方法根据业务需要进行编程**



```java
@Slf4j
@Component //交给容器IOC进行管理
@Aspect //加上这个注解表示不是一个普通的类，而是一个AOP类，在此类中定义模板方法
public class TimeAspect {

//  参数是一个表达式，表示针对哪些特定方法进行编程
//  com.zhangjingqi.service 包名
//  第一个*代表任意返回值 第二个*代表类名或者接口名  第三个*代表方法名
    @Around("execution(* com.zhangjingqi.service.*.*(..))") //切入点表达式
    public Object recordTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long begin = System.currentTimeMillis();

//      result 原始方法执行返回值
        Object result = proceedingJoinPoint.proceed();//调用原始方式运行

        long end = System.currentTimeMillis();

//      proceedingJoinPoint.getSignature() 获取方法的签名，我们就知道是哪个方法了
//      如： List com.zhangjingqi.service.impl.DeptServiceImpl.list()执行耗时：239ms
        log.info(proceedingJoinPoint.getSignature() + "执行耗时：{}ms", end - begin);

//      原始方法的返回值我们需要返回回去
        return result;
    }
}
```



### 2.2.3 AOP 应用场景及优势

**应用场景**

* **记录操作日志**
*  **权限控制**
*  **事务管理**

![image-20230517164446034](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230517164446034.png)





**优势**

* **代码无侵入**
* **减少重复代码**
* **提高开发效率**
* **维护方便**





## 2.3 核心概念



### 2.3.1 连接点 - JoinPoint

**连接点**：JoinPoint，可以被AOP控制的方法（暗含方法执行时的相关信息）



**通知**：Advice，指那些重读的逻辑，也就是共性功能（最终体现为一个方法）



**切入点**：PointCut，匹配连接点的条件，通知仅会在切入点方法执行时被应用（就是实际被AOP控制的方法）

   我们通常会使用下面的切入点表达式来描述切入点

```java
@Around("execution(* com.zhangjingqi.service.*.*(..))") 
```



**切面**：Aspect，描述通知与切入点的对应关系（通知+切入点），被@Aspect注解修饰的类我们一般称为切面类



**目标对象**：Target，通知所应用的对象。



![image-20230517175551925](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230517175551925.png)





### 2.3.2 AOP执行流程

**通知如何与目标对象结合在一起对目标对象中的方法进行功能增强的？**



​    ①**SpringAOP是基于动态代理技术来实现的。程序运行的时候会自动的基于动态代理技术为目标对象生成一个对应的代理对象。**



​    ② **在代理对象中就会对目标对象中的原始方法进行功能的增强。**

​           **如何来增强的？增强的逻辑是什么样子的？**

​               其实就是我们的通知



​    ③**最终在Spring容器中注入的是代理对象，调用的方法也是代理对象中的对应方法**

![image-20230517180323565](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230517180323565.png)











## 2.4 通知

### 2.4.1 通知类型

* **@Around：环绕通知**，此注解标注的通知方法在目标方法前、后都被执行，出现异常后后置代码不会执行。（因为原始方法出现异常了）
*  **@Before：前置通知**，此注解标注的通知方法在目标方法前被执行
*  **@After：后置通知**，此注解标注的通知方法在目标方法后被执行，**无论是否有异常都会执行**
*  **@AfterReturning：返回后通知**，此注解标注的通知方法在目标方法后被执行，**有异常不会执行**
* **@AfterThrowing：异常后通知**，此注解标注的通知方法在**发生异常后执行**



```java
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class MyAspect1 {
    //前置通知
    @Before("execution(* com.zhangjingqi.service.*.*(..))")
    public void before(JoinPoint joinPoint) {
        log.info("before ...");
    }

    //环绕通知
    @Around("execution(* com.zhangjingqi.service.*.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint)
            throws Throwable {
        log.info("around before ...");
        //调用目标对象的原始方法执行
        Object result = proceedingJoinPoint.proceed();
        //原始方法如果执行时有异常，环绕通知中的后置代码不会在执行了
        log.info("around after ...");
        return result;
    }

    //后置通知
    @After("execution(* com.zhangjingqi.service.*.*(..))")
    public void after(JoinPoint joinPoint) {
        log.info("after ...");
    }

    //返回后通知（程序在正常执行的情况下，会执行的后置通知）
    @AfterReturning("execution(* com.zhangjingqi.service.*.*(..))")
    public void afterReturning(JoinPoint joinPoint) {
        log.info("afterReturning ...");
    }

    //异常通知（程序在出现异常的情况下，执行的后置通知）
    @AfterThrowing("execution(* com.zhangjingqi.service.*.*(..))")
    public void afterThrowing(JoinPoint joinPoint) {
        log.info("afterThrowing ...");
    }
}
```



**注意事项**

* **@Around环绕通知需要自己调用ProceedingJoinPoint.proceed()来执行原始方法，其他通知不需要考虑原始方法的执行**



* **@Around环绕通知的方法的返回值，必须指定为Object，来接收原始方法的返回值**

​        如果不return，在调用这个方法的地方时拿不到返回值的





**对切入点表达式进行抽取**

```java
//   生命切入点表达式的注解,切点
    @Pointcut("execution(* com.zhangjingqi.service.*.*(..))")
    private void pt(){

    }

    //前置通知
    @Before("pt()")
    public void before(JoinPoint joinPoint) {
        log.info("before ...");
    }
```



其他类中也可以进行抽取,只需要定位到切入点表达式的位置即可。

``` java
@Slf4j
@Component
@Aspect
public class MyAspect2 {
//引用MyAspect1切面类中的切入点表达式
@Before("com.zhangjingqi.aspect.MyAspect1.pt()")
public void before(){
log.info("MyAspect2 -> before ...");
 }
}
```





### 2.4.2 通知顺序

​    **当有多个切面的切入点都匹配到了目标方法，目标方法运行，多个通知方法都会被执行。**



>  ​      **下面研究多个切面类的通知顺序**。同个切面类的通知顺序不再研究



* **不同切面类中，默认按照切面类的类名字母排序**

​            目标方法前的通知方法：字母排名靠前的先执行

​            目标方法后的通知方法：字母排名靠前的后执行



* **使用@Order(数字)加在切面类上来控制顺序**









## 2.5 切入点表达式

* 切入点表达式：描述切入点方法的一种表达式
* 作用：主要用来决定项目中哪些方法需要加入通知
*  **常见形式**

​         execution(......):根据方法的签名来匹配

​         @annotation（......）：根据注解匹配



### 2.5.1 execution

​    主要根据方法的返回值、包名、类名、方法名、方法参数等信息来匹配

​    **下面来描述的时候，可以基于接口。也可以基于实现类**

>   execution(访问修饰符?  返回值  包名.类名.?方法名(方法参数) throws 异常？) 
>
>  
>
>  ​    其中？表示可省略的部分
>
>  *  访问修饰符：可省略，比如public、protected
>  *   包名.类名：可省略，但是不建议
>  *   throws 异常：可省略（注意是方法上声明抛出的异常，不是实际抛出的异常）





#### 2.5.1.1 execution通配符

**\***:**单个独立的任意符号，可以匹配任意返回值、包名、类名、方法名、方法参数等信息来匹配**

​    此案例表示返回值人任意，二级包任意，类或接口任意，方法参数任意但是有且只有一个

```java
execution(* com.*.service.*.update(*)
```



  匹配类名以Service结尾，方法以delete开头的方法

``` java
execution(void
com.itheima.service.impl.*Service.delete*(java.lang.Integer)
)
```





**..**：**多个连续的任意符号，可以通配任意层级的包，或者任意类型、任意个数的参数**

​    层级包任意，方法的参数任意

```java
execution(* com.zhangjingqi..DeptService.*(..)
```



  返回值任意，方法名任意，方法参数任意

```java
execution(* *(..)
```







#### 2.5.1.2 execution表达式案例

```java
@Pointcut("execution(* com.zhangjingqi.service.*.*(..))")
```



*  **省略异常**

``` java
execution(public void
com.itheima.service.impl.DeptServiceImpl.delete(java.lang.Integer)
)
```





*  **省略方法访问修饰符**

      参数是全类名

``` java
execution(void
com.itheima.service.impl.DeptServiceImpl.delete(java.lang.Integer)
)
```



*  **使用".."省略包名**

``` java
execution(public void  com..DeptServiceImpl.delete(java.lang.Integer))
```



*  **省略包名类名**

​      指定方法名。

​      不建议将包名和方法名省略。一旦省略，将表达式的范围扩大，一是影响匹配的效率，而是可能匹配到其他不需要的方法

``` java
execution(public void  delete(java.lang.Integer))
```



*  **匹配所有的方法**

​     此时表示匹配DeptServiceImpl类中的所有方法

``` java
execution(public void  com..DeptServiceImpl.*(java.lang.Integer))
```



*  **使用 且（&&）、或（||）、非（!） 来组合比较复杂的切入点表达式**

   ``` java
   execution(* com.zhangjingqi.service.DeptService.list(..)) ||
   execution(* com.zhangjingqi.service.DeptService.delete(..))
   ```

   

#### 2.5.1.3 切入点表达式建议

*  **所有业务方法名在命名时尽量规范，方便切入点快速匹配。**

​         如查询方法find开头，更新类方法update开头



*   **描述切入点方法通常基于接口描述，而不是直接描述实现类，增强拓展性**



*   **在满足业务需要的前提下，尽量缩小切入点的匹配范围**

​          包名匹配进行不使用“..”,使用“*”匹配单个包





### 2.5.2 @annotation

用于匹配标识有特定注解的方法

``` java
@Before("@annotation(com.zhangjingqi.anno.MyLog)")
```



**简化下列表达式**

``` java
execution(* com.zhangjingqi.service.DeptService.list(..)) ||
execution(* com.zhangjingqi.service.DeptService.delete(..))
```





**实现步骤**

1. 编写自定义注解

2. 在业务类要做为连接点的方法上添加自定义注解



**创建自定义注解类**

```java
@Retention(RetentionPolicy.RUNTIME)//描述注解什么时候生效的：运行时有效
@Target(ElementType.METHOD)//当前注解可以作用在哪些地方
public @interface MyLog {
}
```



**添加自定义注解**

``` java
@Override
@MyLog //自定义注解（表示：当前方法属于目标方法）
public List<Dept> list() { ... }

@Override
@MyLog //自定义注解（表示：当前方法属于目标方法）
public void delete(Integer id) { ... }
```



**切面类**

``` java
@Slf4j
@Component
@Aspect
public class MyAspect6 {
//针对list方法、delete方法进行前置通知和后置通知
//前置通知
@Before("@annotation(com.zhangjingqi.anno.MyLog)")
   public void before(){
   log.info("MyAspect6 -> before ...");

//后置通知
@After("@annotation(com.zhangjingqi.anno.MyLog)")
   public void after(){
   log.info("MyAspect6 -> after ...");
 }
}
```





### 2.5.3 切入点表达式总结

*  **execution切入点表达式**

​         根据我们所指定的方法的描述信息来匹配切入点方法，这种方式也是最为常用的一种方式

​         如果我们要匹配的切入点方法的方法名不规则，或者有一些比较特殊的需求，通过

​        execution切入点表达式描述比较繁琐

*  **annotation 切入点表达式**

​           基于注解的方式来匹配切入点方法。这种方式虽然多一步操作，我们需要自定义一个注解，但

​           是相对来比较灵活。我们需要匹配哪个方法，就在方法上加上对应的注解就可以了



## 2.6 连接点

​      **被AOP控制的方法**，目标对象中所有的方法都可以被AOP控制，**在Spring AOP中又特制方法的执行**

![image-20230517175551925](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230517175551925.png)



*  **在Spring中用JoinPoint抽象了连接点，用它可以获得方法执行时的相关信息，如目标类名、方法类名、方法参数等**

​         对于@Around通知，获取连接点信息只能用ProceedingJoinPoint

![image-20230520103824339](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230520103824339.png)

​        

​        对于其他四种通知，获取连接点信息只能使用JoinPoint，它是ProceedingJoinPoint父类型

![image-20230520103842552](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230520103842552.png)





>  **对于@Around通知，为什么获取连接点信息只能用ProceedingJoinPoint？**
>
>      在Spring AOP中，@Around通知是最为强大和灵活的通知类型，它可以决定是否执行连接点，以及如何处理连接点返回的结果。因此，@Around通知需要通过ProceedingJoinPoint参数来获取连接点信息。
>
>  ​       ProceedingJoinPoint是JoinPoint的子类，同时也是JoinPoint的扩展版本。JoinPoint表示连接点，也就是被Advice修饰的方法。**而ProceedingJoinPoint除了表示连接点外，还具有一个proceed()方法，该方法是执行目标方法的关键**。在@Before和@After通知中，JoinPoint足以满足需要，因为它们只需要获取连接点信息即可，不需要执行目标方法。但在@Around中，除了获取连接点信息，还需要控制目标方法的执行，因此需要用到ProceedingJoinPoint。
>
>  ​       在@Around通知中，可以通过ProceedingJoinPoint的proceed()方法，手动控制目标方法的执行。例如，可以在proceed()方法前后进行一些预处理或后处理。同时，ProceedingJoinPoint还提供了一些其他的工具方法，例如getArgs()获取目标方法参数，getSignature()获取目标方法签名等，这些方法在编写@Around通知时也非常有用。













