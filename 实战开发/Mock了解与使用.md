

# Mock了解与使用





   **前端开发做mock，后端开发做调试**

> ​      视频资料：[很详细的ApiFox接口测试工具教程以及Mock功能全解析！_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV14S4y147Sx/?spm_id_from=333.337.search-card.all.click&vd_source=c01240addcba226237f3c4781490fbae)





​     mock就是模拟数据，并不是一个很深奥的东西

![image-20230222153213768](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222153213768.png)







# 0、接口约定

   本系统为**Restful API接口**，若无特别说明，使用以下约定：

​      1、请求数据通过json格式传递

​      2、除注册、登录接口外，请求时需提供token

​      3、token通过登录获取，添加至请求头

​                   例如 Authorization： Bearer abc

​                   其中abc为token值

​      4、2xx状态码表示请求成功

​      5、4xx状态码表示请求失败，并通过detail字段返回原因

   

## 0.1 怎么提取token？

​      如下图所示，我们登录之后会返回给我们一个token，下面我就要把这个token提取出来

![image-20230222171336607](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222171336607.png)



当我们点击发送之后，我们就把"access_token"提取出来了

![image-20230222171647989](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222171647989.png)



我们在下面的控制台中也发现设置全局变量成功了

![image-20230222171932654](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222171932654.png)





## 0.2 提取出的token怎么使用？

​     除注册、登录接口外，请求时需提供token，比如我们有”订单管理“，如果我们请求的时候不添加token的时候，请求会失败，如下图所示

![image-20230222172908854](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222172908854.png)





   下面我们对这个请求设置一下请求头

​        我们这是使用的Bearer健全，那怎么健全呢？

​           点击Bearer后面的魔法棒！

![image-20230222173145543](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222173145543.png)

![image-20230222173318993](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222173318993.png)



插入之后就是下面这个效果，后面多了一个token数据

![image-20230222173350910](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222173350910.png)





再次请求，发送成功！！！！

![image-20230222173439376](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222173439376.png)











# 一、面临的开发和测试的问题

​    前后端分离架构



 产品，开发，测试三方会审，对需求评审后，前后端会开会定接口



 由开发指定接口文档（Rap）



后端开发依据接口文档开发接口，一边开发一边调试。（使用postman或ApiFox当然在后端开发的时候，也有可能与前端不协调）



前端开发需要Mock数据供前端调用



开发完成后，测试通过Postman或ApiFox去接口测试



# 二、ApiFox简介以及流程优化



ApiFox=Postman=Swagger+Jmeter+Mock工具集



1. **定接口文档（ApiFox） **

​          这个接口文档随着开发的进行可能随时变化，因为需要协调前后端



2. **前端直接根据ApiFox去Mock数据**

​       对于Mock而言，有本地的环境也有云端的环境；

​       根据接口直接去Mock出来符合接口的数据，不需要我们手动的写一些代码。

   

​    3.**后端开发直接在ApiFox进行调试**

​    4. **测试人员直接在ApiFox的接口文档下面写用例**

​    5、**前后端联调**







# 三、ApiFox基本使用



 ## 3.1 示例项目



![image-20230222140017482](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222140017482.png)





我们也很清楚的看出下面的项目示例是基于RestFul规范

我们可以看下面这个宠物店的示例项目，GET方法下面还有四个用例

![image-20230222140103095](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222140103095.png)





## 3.2 新建团队



一个团队可以有多个项目

![image-20230222140608111](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222140608111.png)

![image-20230222140705285](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222140705285.png)



下图第一个就是我们自己

![image-20230222140735763](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222140735763.png)



我们也可以邀请成员和我们一块协作开发

![image-20230222140853238](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222140853238.png)



## 3.3 注册、登录、团队管理、项目管理



![image-20230222141244764](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222141244764.png)



进入到里面就是下面这个样子

![image-20230222141307945](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222141307945.png)





![image-20230222141641864](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222141641864.png)





# 四、编写Api文档（定接口文档）





## 4.1  新建接口文档



![image-20230222142255588](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222142255588.png)



 ### 4.1.1   接口文档基本介绍



下图中的名称就是接口的名称

![image-20230222142513816](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222142513816.png)



**下面来举个例子**



​    刚开是的时候是没有接口文档的，https://api.ttt.one/rest-v2/login/sign_up这个是我们自定义的，随便写（因为我们最开始就是定接口文档）

![image-20230222143334673](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222143334673.png)



都写完之后保存一下，就是下面这个样子

![image-20230222143422331](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222143422331.png)





虽然我们保存了，但是依然可以修改接口文档

![image-20230222143721864](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222143721864.png)





### 4.1.2 Params请求参数

​    GET请求一般通过Params传参

![](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222143942745.png)





​     请求参数它有两类

​    **第一类是Query参数:**  url里面？之后的参数

![image-20230222145413773](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222145413773.png)



​     **第二类是URL参数：**      url中的{参数名}参数



​         具体的使用可以看下面

![image-20230222145031096](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222145031096.png)





### 4.1.3  Body请求参数

​      我们在使用Body请求参数的时候，ApiFox帮我们加上这个请求头   Content-Type:application/json

​    当我们正在编辑的时候是看不到这个请求头的，但是当我们把Api文件保存一下，就可以看到这个请求头了

![image-20230222153307958](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222153307958.png)











**form-data  ： 文件上传**

![image-20230222150007550](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222150007550.png)



**x-www-form-urlencoded:表单** 

**raw:文本格式**

**binary:二进制文件格式**







#### 4.1.3.1  对于JSON格式



   对于JSON格式我们可以快速导入，也可以一个一个的加，我们下面这个就是一个一个的导入

![image-20230222150531061](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222150531061.png)



当我们输入上JSON格式的字符串之后

![image-20230222150828711](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222150828711.png)



当我们上面点击”确定“之后，下面会自动给我们匹配（对JSON的格式进行了分析）

![image-20230222150911619](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222150911619.png)

 

包括我们对参数也可以设置是否可以是空

![image-20230222151443716](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222151443716.png)



**是否可以mock数据**

![image-20230222151534373](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222151534373.png)



如果我们选择”@emai“之后，可以直接帮我们mock出email格式



下面也可以添加中文名，以及说明备注

![image-20230222151611779](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222151611779.png)

![image-20230222151756823](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222151756823.png)





对节点设计更深

   每个字段下面有一个”更多“，但是我可能是新版本的原因，并没有这个按钮，所以截取一下视频中的内容

![image-20230222152042378](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222152042378.png)



点开之后就是下面的这种形式

![image-20230222152132697](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222152132697.png)



#### 4.1.3.2 对结果的返回响应做定义

如下图所示，我们可以再次导入JSON数据格式

![image-20230222153631855](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222153631855.png)



这下面的json格式就是以后后端返回给前端的格式如果没有意外的话

![image-20230222153913190](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222153913190.png)

##### 4.1.3.2.1添加错误响应

![image-20230222155952237](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222155952237.png)



![image-20230222160119096](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222160119096.png)

假设我们的错误响应是下面这个格式的，就有一个id

![image-20230222160210797](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222160210797.png)









##### 4.1.3.2.2**添加成功示例**

![image-20230222155258410](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222155258410.png)

![image-20230222155439290](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222155439290.png)



 或者直接点击自动生成，就是下面这个样子

![image-20230222155517428](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222155517428.png)



##### 4.1.3.2.3**添加异常示例**

 自动生成之后就是下面这个样子的

![image-20230222160409469](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222160409469.png)



**上面的两个示例就是mock**





## 4.2  看新建API文档



![image-20230222160735372](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222160735372.png)



![image-20230222160751273](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222160751273.png)



![image-20230222160806824](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222160806824.png)

![image-20230222160825840](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222160825840.png)







# 五、后端开发做调试（调试两个用例）



## 5.1  添加用例---如失败与成功

我们运行一下上面配置好的文件

​    为什么”email“字段能直接生成一个email格式的字符串呢？

​           因为前面我们做了一个mock处理（文档之前定义的）

![image-20230222161755124](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222161755124.png)



当我们生成之后可以进行发送进行测试，最后也可以保存用例

![image-20230222162939852](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222162939852.png)



点击确定

![image-20230222163005627](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222163005627.png)



当完成上面的操作的时候就生成了一个用例

![image-20230222163045360](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222163045360.png)



刚刚保存的”成功“用例，如下图所示，当然我们也可以保存一个”失败“案例，但是我现在没法访问这个网址，无法区分成功与失败

![image-20230222163129055](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222163129055.png)





## 5.2 在某个数据后面添加随机数



就比如下图，我希望在"email"字符串中的”@“之前添加一个随机数

![image-20230222164432784](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222164432784.png)





我们需要点击JSON串上上面的”动态值“

![image-20230222164634393](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222164634393.png)



界面如下图所示

![image-20230222164708366](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222164708366.png)





比如说我想加一个6位的整数

![image-20230222165126049](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222165126049.png)

![image-20230222165522992](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222165522992.png)











# 六、很多功能

   环境变量，全局变量，临时变量  是怎么处理的

   动态变量，随机参数

   自动化字段

   Socket接口

   团队管理

   导入导出

   Mock应用

   脚本开发

   自动化执行

   Jenkins持续集成



​    

## 6.1  开启云端

   打开就好

![image-20230222180042614](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222180042614.png)

![image-20230222180123608](C:\Users\zhangjingqi\AppData\Roaming\Typora\typora-user-images\image-20230222180123608.png)

