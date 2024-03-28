package com.hmdp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisIdWorker {
    //  2022年1月1日0时0分0秒  的 秒数
    private static final long BEGIN_TIMESTAMP = 1640995200L;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    /**
     * id生成策略
     *
     * @param keyPrefix 业务前缀
     * @return 生成的id
     */
    public long nextId(String keyPrefix) {
//      TODO 1.生成时间戳（当前时间减去我们规定的开始时间的秒数）
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

//      TODO 2.生成序列号
//      同一个业务不要使用同一个key，因为incr自增有限度，2的64次方
//      为了预防订单数日积月累超过2的64次方，我们可以再拼接一个日期字符串，这样做还能方便以后统计
//      TODO 2.1 获取当前日期
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));

//      TODO 2.2 自增长
//      返回值是Long，但是我们改成了long，改完出现警告：会有空指针问题
//      但是并不会出现空指针问题,加入此key不存在,它会在自动帮我们创建一个key
        long count = stringRedisTemplate.opsForValue().increment("icr" + keyPrefix + ":" + date);

//      TODO 拼接两部分
//      我们的返回值是long,直接拼接timestamp与count就是字符串了,不能直接拼接
//      为了解决这个问题,我们使用位运算
//      timestamp<<32 时间戳向左移动32位,把redis自增的数创建出来,空出来的数以0位补充
//      | 代表或运算，一个为真，就是真 0|0=0， 0|1=1，因为后面32位都是0，所以还是count本身
        return timestamp << 32 | count;
    }

//    public static void main(String[] args) {
////
//        LocalDateTime time = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
//        传入一个时区作为参数
//        long second = time.toEpochSecond(ZoneOffset.UTC);
//        System.out.println(second);
//    }

}
