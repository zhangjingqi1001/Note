# SpringbootWeb快速入门



Group: 组名，一般采用域名倒写

Artifact： 模块名

Package name: 包名

```xml
<groupId>com.zhangjingqi</groupId>
<artifactId>Springboot-Web</artifactId>
<version>0.0.1-SNAPSHOT</version>

<name>Springboot-Web</name>
<description>Springboot-Web</description>
```



# 一、HTTP协议



Hyper Text Transfer Protocol 超文本传输协议，规定了**浏览器和服务器之间数据传输的规则。**



**规则主要包括请求数据的格式（Request Headers）、相应数据的格式（Response Headers）**

   如下图所示，发送请求时会携带请求头（Request Headers），将这些字符串带到服务端去，格式非常固定

![image-20230511110507381](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230511110507381.png)





* **特点**

    基于TCP协议：面向连接、安全。每一次请求前都需要三次握手，链接完了之后确定双方都有收发能力再传输数据

  

    基于请求-响应模型的： 一次请求对应一次响应

  


    HTTP协议是无状态的协议，对于书屋处理没有记忆能力。每次请求 - 响应都是独立的

  


* **缺点**

​          多次请求间不能共享数据



* **优点**

     速度快



## 1.1 请求数据格式



![image-20230511113154221](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230511113154221.png)



请求方式-GET： 请求参数在请求行中，没有请求体，如：/brand/findAll?name=OPPO&status=1

请求方式-POST： 请求参数在请求体中，POST请求大小是没有限制的。





**常见请求头代表的含义**

|     请求头      |                             含义                             |
| :-------------: | :----------------------------------------------------------: |
|      Host       |     请求的主机名， 如localhost:8080代表访问本机8080端口      |
|     Accept      | 表示浏览器能接收的资源类型，如text/\*，image/\*或者\*/\*表示所有 |
|   User-Agent    | 浏览器版本，例如Chrome浏览器的标识类似Mozilla/5.0 ... Chrome/79，IE浏览器的标识类似Mozilla/5.0 (Windows）like Gecko |
| Accept-Language |   表示浏览器偏好的语言，服务器可以根据此返回不同语言的网页   |
| Accept-Encoding |      表示浏览器可以支持的压缩类型，例如gzip，deflate等       |
|  Content-Type   |                      请求主体的数据类型                      |
| Content-Length  |                 请求主体的大小（单位：字节）                 |



 

## 1.2 响应数据格式



![image-20230511141900017](https://picture-typora-zhangjingqi.oss-cn-beijing.aliyuncs.com/image-20230511141900017.png)





**响应状态码**

| 状态码 |                             含义                             |
| :----: | :----------------------------------------------------------: |
|  1xx   | 响应中 - 临时状态码，表示请求已经接收，告诉客户端应该继续请求或者如果它已经完成则忽略它 |
|  2xx   |           成功 - 表示请求已经被成功接收，处理完成            |
|  3xx   | 重定向 - 重定向到其他地方；让客户端再发起一次请求以完成整个处理 |
|  4xx   | 客户端错误 - 处理发生错误，责任在客户端。如：请求了不存在的资源、客户端未被授权、禁止访问等。 |
|  5xx   |        服务器错误 - 处理发生错误，责任在服务端。如：         |



200 客户端请求成功

404 请求资源不存在，一般是URL输入有误，或者网站资源被删除

500  服务器发生不可预期的错误



**响应头**

|      响应头      |                            含义                            |
| :--------------: | :--------------------------------------------------------: |
|   Content-Type   |  表示该响应内容的类型，例如text/html，application/json。   |
|  Content-Length  |               表示该响应内容的长度(字节数)。               |
| Content-Encoding |               表示该响应压缩算法，例如gzip。               |
|  Cache-Control   | 指示客户端应如何缓存，例如max-age=300表示可以最多缓存300秒 |
|    Set-Cookie    |          告诉浏览器为当前页面所在的域设置cookie。          |



​    比如说我们响应的数据是一个JSON，那我们Content-Type的值就是application/json，之后客户端接收到的数据就是JSON格式的。





## 1.3 协议解析

​          根据HTTP的请求格式解析请求数据以及响应数据。





# 二、 Tomcat



[Tomcat服务器的使用_怎么访问tomcat服务器_我爱布朗熊的博客](https://blog.csdn.net/weixin_51351637/article/details/126110360)



[Day04-15. Web入门-Tomcat-介绍](https://www.bilibili.com/video/BV1m84y1w7Tb/?p=64&spm_id_from=pageDriver&vd_source=c01240addcba226237f3c4781490fbae)



**官网：** https://tomcat.apache.org/download-90.cgi



**Windows版下载地址：** [Apache Tomcat® - Apache Tomcat 9 Software Downloads](https://tomcat.apache.org/download-90.cgi)



**安装：** 下载安装包直接解压



**卸载：** 将解压后的目录直接删除



**启动：**

​            双击 bin\startup.bat

​             控制台中文乱码： 修改conf/logging.properties

​              之前是UTF-8

```
 java.util.logging.ConsoleHandler.encoding = GBK
```



**关闭： **

* 点击 “×”
*  bin\shutdown.bat : 正常关闭
*   Ctrl+C: 正常关闭



**问题**

* 启动窗口一闪而过：检查JAVA_HOME环境比那辆是否正确配置
*  端口号冲突：找到对应程序，将其关闭掉





 **修改端口号：**

找到 conf/server.xml 文件，修改port即可

说明： HTTP协议默认端口号为80，如果将Tomcat端口号改为80，则将来访问Tomcat时，不用输入端口号

```xml
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443"
           maxParameterCount="1000"
           />
```




**部署项目：**

​    将项目放置到webapps目录下，即部署完成









































































