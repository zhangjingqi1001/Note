# 前言



[推荐基于Lombok的Spring注入方式（基于构造器注入）及快速获取Spring容器中管理的对象 (ngui.cc)](https://www.ngui.cc/el/3665897.html?action=onClick)

# 一、字段注入

在Spring Boot中，字段注入是一种依赖注入的方式，它通过**直接注入依赖到类的字段上**来实现。

这个字段可以是私有的，而且不需要提供相应的setter方法。字段注入通常使用`@Autowired`注解实现。

以下是字段注入的示例：

```java
@Service
public class MyService {
    @Autowired
    private SomeDependency dependency;

    // ...
}
```

在上面的示例中，`SomeDependency`类型的依赖被注入到了`dependency`字段上。

在使用字段注入时，Spring会自动扫描并找到匹配的Bean，并将其注入到类的字段上。

需要注意的是，字段注入是通过反射实现的，所以字段不能是`final`的，而且最好将字段设置为私有的，以遵循封装原则。

需要注意的是，虽然字段注入简化了代码，**但也有一些潜在的问题**。由于字段注入是直接将依赖注入到字段上，我们就无法在构造方法或其他方法中明确地指定依赖项。这可能影响代码的可测试性和可读性。

因此**，最好在需要注入的类中使用构造方法注入或者通过setter方法进行注入**，这样更加明确和可控。

> 相关@Autowired注解在下面文章https://blog.csdn.net/weixin_51351637/article/details/131096709
>
> @Autowired注解：使用在使用在字段或方法上，用于根据类型（byType）注入引用数据
>
> 
>
> @Resource注解是Java EE规范提供的，它在JDK中的javax.annotation.Resource包中定义。
>
> @Resource注解既可以根据类型注入，也可以根据名称注入，详细可以查看上面的文章
>



# 二、构造方法/setter方法注入

**SpringBootBean的注入方式**

1. 构造器注入
2. Setter注入
3. 基于注解的 @Autowired 自动装配（Field 注入）

第三种我们很熟悉就不看了



**在Spring Boot中，可以使用构造方法注入或通过setter方法注入依赖项。**

> Spring中注入方式：可以查看 `4.3扩展`
>
> [ Spring Bean、XML方式Bean配置、Bean实例化配置、Bean注入_我爱布朗熊的博客-CSDN博客](https://blog.csdn.net/weixin_51351637/article/details/131056590)







## 2.1 构造方法注入

使用**构造方法注入**的步骤如下：

1. 在需要注入依赖的类中创建一个构造方法，并将需要注入的依赖作为参数传入。例如：

```java
@Service
public class MyService {
    private final SomeDependency dependency;

    public MyService(SomeDependency dependency) {
        this.dependency = dependency;
    }

    // ...
}
```

2. 确保依赖类（`SomeDependency`）已经在Spring容器中配置为一个Bean。可以使用`@Component`、`@Service`、`@Repository`或`@Controller`等注解之一进行标记。

```java
@Component
public class SomeDependency {
    // ...
}
```

3. 当Spring容器启动时，它将会自动检测到构造方法，并根据依赖的类型来注入相应的Bean。



**原因**是因为这种注入方式使类可以不依赖容器而使用，非IOC容器可以通过调用Constructor来实例化这个类。

并且这样的做法更加安全，一方面避免了循环依赖（如果出现循环依赖，Spring会在注入时报错），另一方面避免了注入对象为空（为空时这个类将无法被构建，spring不能启动）。



**下面这个例子就是完整版构造方法注入**

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService userService;
    private final IDeptService deptService;
    private final IRoleService roleService;
    private final UserCacheClean userCacheClean;
    
    public UserController(IUserService userService, IDeptService deptService,IRoleService roleService, UserCacheClean userCacheClean) {
        this.userService = userService;
        this.deptService = deptService;
        this.roleService = roleService;
        this.userCacheClean = userCacheClean;
    }
}
```



> **@NoArgsConstructor**后会 生成无参的构造方法
>
> **@AllArgsConstructor** 生成一个包含过所有字段的构造方法。
>
> **@RequiredArgsConstructor**会将类的每一个final字段或者non-null字段生成一个构造方法



所以上面的代码我们可以利用注解完成

```java
@AllArgsConstructor //这里使用@RequiredArgsConstructor也可以
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService userService;
    private final IDeptService deptService;
    private final IRoleService roleService;
    private final UserCacheClean userCacheClean;
    }
}
```

> 在这里@AllArgsConstructor注解 和@RequiredArgsConstructor注解都可以用来替换@Autowired写法
>
> 区别在@RequiredArgsConstructor必须要有final修饰。

## 2.2 setter方法注入

使用**setter方法注入**的步骤如下：

1. 在需要注入依赖的类中创建一个公共的setter方法，并在方法中接收需要注入的依赖作为参数。例如：

```java
@Service
public class MyService {
    private SomeDependency dependency;

    public void setDependency(SomeDependency dependency) {
        this.dependency = dependency;
    }

    // ...
}
```

2. 确保依赖类（`SomeDependency`）已经在Spring容器中配置为一个Bean。可以使用`@Component`、`@Service`、`@Repository`或`@Controller`等注解之一进行标记。

```java
@Component
public class SomeDependency {
    // ...
}
```

3. 当Spring容器启动时，它将会自动检测到setter方法，并根据依赖的类型来注入相应的Bean。



## 2.3 注意事项

需要注意的是，无论是构造方法注入还是通过setter方法注入，Spring容器都会自动完成依赖注入。你可以根据具体情况选择合适的注入方式。构造方法注入在创建对象时就完成了依赖注入，且通常应该将依赖设为不可变的（使用`final`修饰符）。而通过setter方法注入，则可以在对象创建后随时进行依赖注入。



# 三、借助Lombok注入

Lombok在spring中的**特殊注解@RequiredArgsConstructor和@AllArgsConstructor**，用在类上面，可以方便的注入对象，而不必每个字段都要@Autowired



> **为什么要使用这种方式**？
>
> 当我们使用`@Autowired`进行Bean注入的时候，IDEA会提示警告，不建议使用此方式进行注入。
>
> Spring官方更推荐使用构造方法进行注入
>
> **如果一个类中要注入多个对象的话，构造方法进行注入的方式会显得代码很臃肿**





## 3.1 @RequiredArgsConstructor

**@RequiredArgsConstructor**为每个需要特殊处理的字段生成一个带有1个参数的构造函数

对于那些用@NonNull标记的字段，还将生成显式null检查。如果用于标记为@NonNull的字段的任何参数包含null，则构造函数将抛出NullPointerException。参数的顺序与字段在类中出现的顺序相匹配

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final IUserService userService;
    private final IDeptService deptService;
    private final IRoleService roleService;
    private final UserCacheClean userCacheClean;
}

```

> @RequiredArgsConstructor只会处理final字段以及任何标记为@NonNull且未在声明位置初始化的字段，并且不会包含继承自父类的字段。
>
> 如果需要在构造方法中包含继承的字段，可以使用其他相关的Lombok注解，如@NoArgsConstructor、@AllArgsConstructor等



看一看一下下面的例子

使用@RequiredArgsConstructor注解，它自动生成了一个构造方法`public MyClass(String name, int age)`

```java
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MyClass {
    private final String name;
    private final int age;
    private String address; // 不是final字段，不会生成构造方法参数

    public static void main(String[] args) {
        MyClass myClass = new MyClass("John", 25);
    }
}

```





## 3.2 @AllArgsConstructor



同时使用@AllArgsConstructor+@Value注解会报错

如下所示

![image-20230907110036104](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230907110036104.png)



**解决方式**

**将@AllArgsConstructor注解替换成@RequiredArgsConstructor注解**



**原因**

**@AllArgsConstructor**是Lombok库提供的注解，它会自动生成一个包含所有字段的构造方法。

**@Value**注解也是Lombok库提供的注解，它会生成不可变（immutable）类的方法，包括私有字段、构造方法和访问方法。

这两个注解的生成结果存在冲突。

**@Value("${属性名}") 注解是通过对象的set 方法赋值的，构造方法的执行在set方法之前，所以在构造方法中使用变量会发现变量为 null**



























