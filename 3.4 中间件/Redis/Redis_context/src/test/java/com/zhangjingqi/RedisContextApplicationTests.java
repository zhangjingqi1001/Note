package com.zhangjingqi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhangjingqi.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class RedisContextApplicationTests {
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @Test
//    void test() {
////     这个地方set里面的参数写字符串也可以，写Object也可以，底层有一个自动序列化机制
////     写入一条String数据
//        redisTemplate.opsForValue().set("name", "zhangjingqi");
//
//        Object name = redisTemplate.opsForValue().get("name");
//        System.out.println(name);
//    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
//  JSON工具
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testStringTemplate() throws JsonProcessingException {
//      准备对象
        User user = new User("虎哥", 18);
//      手动序列化
        String json = mapper.writeValueAsString(user);
//      写入一条数据到Redis
        stringRedisTemplate.opsForValue().set("user:200",json);

//      读取数据
        String s = stringRedisTemplate.opsForValue().get("user:200");
//      反序列化
        User user1 = mapper.readValue(s,User.class);
        System.out.println(user1);
    }


//    @Test
//    void testSaveUser(){
////      写入数据
//        redisTemplate.opsForValue().set("user:100",new User("张靖奇",21));
////      获取数据
//        User o = (User) redisTemplate.opsForValue().get("user:100");
//        System.out.println(o);
//    }


//    private Jedis jedis;
//
//    @BeforeEach//测试类中任何一个测试方法执行之前都先执行该注解标注的方法
//    void contextLoads() {
////   1.建立连接
//        jedis = new Jedis("127.0.0.1",6379);
////   2.设置密码
////        jedis.auth("root");
////   3.选择库  如果我们不选择的话，默认就是选择的0号库
//        jedis.select(0);
//    }
//
//    @Test
//    void testString(){
////     存入数据
//        String result = jedis.set("name","张靖奇");
//        System.out.println("result:"+result);
////     获取数据
//        String name = jedis.get("name");
//        System.out.println("name:"+name);
//    }
//    @Test
//    void testHash(){
////      存放Hash数据
//        jedis.hset("user:1","name","jack");
//        jedis.hset("user:1","age","21");
////      取数据
//        Map<String, String> map = jedis.hgetAll("user:1");
//        System.out.println(map);
//    }
//
//    @AfterEach
//    void tearDown(){
////      我们这里应该判断一下，防止出现空指针异常
//        if(jedis !=null){
//            jedis.close();
//        }
//    }


}
