package com.zhangjingqi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

//@Configuration
//public class RedisConfig {
//    @Bean     //RedisConnectionFactory不需要我们创建Spring会帮助我们创建
//    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){
////        1.创建RedisTemplate对象
//          RedisTemplate<String,Object> template = new RedisTemplate<>();
////        2.设置连接工厂
//          template.setConnectionFactory(connectionFactory);
////        3.创建JSON序列化工具
//          GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
////        4.设置Key的序列化
//          template.setKeySerializer(RedisSerializer.string());
//          template.setHashKeySerializer(RedisSerializer.string());
////        5.设置Value的序列化   jsonRedisSerializer使我们第三步new出来的
//          template.setValueSerializer(jsonRedisSerializer);
//          template.setHashValueSerializer(jsonRedisSerializer);
////        6.返回
//        return template;
//
//    }
//}