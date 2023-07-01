package com.hmdp.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class SimpleRedisLock implements ILock {
    //  业务名称，为了获取锁
    private String name;
    private StringRedisTemplate stringRedisTemplate;
    //  锁的前缀
    private static final String KEY_PREFIX = "lock:";
    //UUID,使用胡图工具类,  true表示将UUID生成的横线去掉
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";

    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean tryLock(long timeoutSec) {
//      获取线程标识
        String threadId = ID_PREFIX + Thread.currentThread().getId(); //线程id，同一个JVM线程id是不会重复的

//      获取锁
//      setIfAbsent是setnx
//      此处的value比较特殊，要加上线程的标识
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + name, threadId, timeoutSec, TimeUnit.SECONDS);
//      直接返回success自动拆箱，会有安全风险。比如success为null，那拆箱后就是空指针
//      所以采取下面这种情况
        return Boolean.TRUE.equals(success);
    }

    //  TODO 提前获取锁的脚本
//  RedisScript是一个接口，我们使用一个实现类，泛型就是返回值的类型
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    //  TODO 静态代码块做初始化
    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
//      TODO 借助Spring提供的方法区resource下找
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
//      TODO 配置一下返回值类型
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    @Override
    public void unlock() {
//      TODO 调用脚本
//      Key是一个集合，Collections.singletonList(KEY_PREFIX + name)可以快速生成一个集合
        stringRedisTemplate.execute(
                UNLOCK_SCRIPT,
                Collections.singletonList(KEY_PREFIX + name),
                ID_PREFIX + Thread.currentThread().getId()
        );
    }


//    @Override
//    public void unlock() {
////      TODO 获取线程标识
//        String threadId = ID_PREFIX+Thread.currentThread().getId();
////      TODO 获取锁中标识
//        String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
////      TODO 判断标识是否一致
//        if (threadId.equals(id)){
////          释放锁
//            stringRedisTemplate.delete(KEY_PREFIX + name);
//        }
//    }
}
