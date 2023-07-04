package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient(){
//      TODO 配置类
        Config config = new Config();
//      TODO 添加Redis地址，如单点地址（非集群）
//      我没有密码，如果有密码的话可以设,如果是集群的话使用config.useClusterServers()添加集群地址
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
//      TODO 创建客户端
        return Redisson.create(config);
    }

//    @Bean
//    public RedissonClient redissonClient2(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6380");
//        return Redisson.create(config);
//    }
//
//    @Bean
//    public RedissonClient redissonClient3(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6381");
//        return Redisson.create(config);
//    }
}
