# 一、FastJson介绍



​     Fastjson是阿里巴巴的开源SON解析库它可以解析JSON格式的字符串，**支持将java Bean序列化为ISON字符串，也可以从JSON字符串反序列化到JavaBean。**



**Fastjson的优点**

- **速度快**
  **fastjson相对其他JSON库的特点是快**，从2011年fastjson发布1.1.版本之后其性能从未被其他ava实现的]SON库超越

  

- **使用广泛**
  fastjson在阿里巴巴大规模使用，在数万台服务器上部署，fastjson在业界被广泛接受。在2012年被开源中国评选为最受欢迎的国产开源软件之一

  

- **测试完备**
  fastjson有非常多的testcase，在1.2.11版本中，testcase超过3321个。每次发布都会进行回归测试，保证质量稳定

  

- **使用简单**
  fastison的API十分简洁



- **功能完备**
  支持泛型，支持流处理超大文本，支持枚举，支持序列化和反序列化扩展





# 二、FastJson序列化 API



**序列化：** **将Java对象转换成JSON格式字符串的过程。**





## 2.1 JSON对象转换成字符串

**使用 JSON.toJSONString(Object object)； 方法**



```java
public class ObjectToJSON {
    public static void main(String[] args) {
        Student student = new Student("张三",20,"北京市","zhangjinfqi@qq.com");
        String jsonString = JSON.toJSONString(student);
        System.out.println(jsonString);
    }
}
```

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String name;
    private Integer age;
    private String address;
    private String email;
}
```



![image-20230403171538032](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230403171538032.png)







## 2.2 List集合转换成JSON对象

**使用 JSON.toJSONString(Object object)； 方法**



```java
Student student1 = new Student("张三",20,"北京市","zhangjinfqi@qq.com");
Student student2 = new Student("张三",20,"北京市","zhangjinfqi@qq.com");
Student student3 = new Student("张三",20,"北京市","zhangjinfqi@qq.com");
Student student4 = new Student("张三",20,"北京市","zhangjinfqi@qq.com");

ArrayList<Student> list = new ArrayList<>();
list.add(student1);
list.add(student2);
list.add(student3);
list.add(student4);
String jsonString = JSON.toJSONString(list);
System.out.println(jsonString);
```



![image-20230403172132984](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230403172132984.png)







## 2.3 Map集合转换成JSON对象



```java
Map<String, Student> map = new HashMap<>();
Student student1 = new Student("张三", 20, "北京市", "zhangjinfqi@qq.com");
Student student2 = new Student("张三", 20, "北京市", "zhangjinfqi@qq.com");
Student student3 = new Student("张三", 20, "北京市", "zhangjinfqi@qq.com");
Student student4 = new Student("张三", 20, "北京市", "zhangjinfqi@qq.com");
map.put("1",student1);
map.put("2",student2);
map.put("3",student3);
map.put("4",student4);
String jsonString = JSON.toJSONString(map);
System.out.println(jsonString);
```



![image-20230403172608170](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230403172608170.png)







# 三、FastJSON反序列化



**将JSON格式的字符串转换成Java对象**





## 3.1 JSON字符串转Object对象

**JSON.parseObject(JSON字符串, 要转换成的类.class);**



```java
        String jsonString = "{\"address\":\"北京市\",\"age\":20,\"email\":\"zhangjinfqi@qq.com\",\"name\":\"张三\"}";
//        第一个参数传入JSON字符串，第二个参数传入我们要转换成的对象的类
        Student student = JSON.parseObject(jsonString, Student.class);
        System.out.println(student);
```



![image-20230403173718274](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230403173718274.png)





## 3.2  JSON字符串转List集合



```
JSON.parseArray(json格式字符串, 传递转换后的集合的泛型);
```



```java
        String jsonString = "[{\"address\":\"北京市\",\"age\":20,\"email\":\"zhangjinfqi@qq.com\",\"name\":\"张三\"},{\"address\":\"北京市\",\"age\":20,\"email\":\"zhangjinfqi@qq.com\",\"name\":\"张三\"},{\"address\":\"北京市\",\"age\":20,\"email\":\"zhangjinfqi@qq.com\",\"name\":\"张三\"},{\"address\":\"北京市\",\"age\":20,\"email\":\"zhangjinfqi@qq.com\",\"name\":\"张三\"}]";
//         第一个参数传递JSON格式字符串，第二个参数传递转换后的集合的泛型
        List<Student> studentsList = JSON.parseArray(jsonString, Student.class);
        System.out.println(studentsList);
```

![image-20230403174042963](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230403174042963.png)







## 3.3 JSON字符串转Map集合



```java
        String jsonString = "{\"1\":{\"address\":\"北京市\",\"age\":20,\"email\":\"zhangjinfqi@qq.com\",\"name\":\"张三\"},\"2\":{\"address\":\"北京市\",\"age\":20,\"email\":\"zhangjinfqi@qq.com\",\"name\":\"张三\"},\"3\":{\"address\":\"北京市\",\"age\":20,\"email\":\"zhangjinfqi@qq.com\",\"name\":\"张三\"},\"4\":{\"address\":\"北京市\",\"age\":20,\"email\":\"zhangjinfqi@qq.com\",\"name\":\"张三\"}}";
//      直接进行反序列化，Map集合是没有泛型的，也是可以正常输出的，但是没有泛型的集合是不安全的集合
//        Map map = JSON.parseObject(jsonString);

//        下面掉用户parseObject，传递参数TypeReference类型，在TypeReference的泛型中传递转后的Map集合即可
//         {}是什么意思？   因为TypeReference的构造方法是protected修饰的，只有子类才能调用，但是我们现在不是他的子类，在后面加{}让其在这成为匿名内部类，匿名内部类就是该类的子类对象
        Map<String, Student> map = JSON.parseObject(jsonString, new TypeReference<Map<String, Student>>() {
        });
        System.out.println(map);
```



![image-20230403175356986](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230403175356986.png)

