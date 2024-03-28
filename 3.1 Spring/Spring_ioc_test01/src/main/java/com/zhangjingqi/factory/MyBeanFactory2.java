package com.zhangjingqi.factory;

import com.zhangjingqi.dao.UserDao;
import com.zhangjingqi.dao.impl.UserDaoImpl;

// 原先是怎么产生Bean的？
//     Spring容器通过全包名反射创建好对象放到容器当中
// 现在是怎么创建Bean？
//     Spring容器帮我们去调用MyBeanFactory1的静态方法userDao，最终将返回的对象存入到Spring容器之中
public class MyBeanFactory2 {

    public UserDao userDao(String name,int age){
        System.out.println("name:"+name+",age:"+age);
        return new UserDaoImpl();
    }

}
