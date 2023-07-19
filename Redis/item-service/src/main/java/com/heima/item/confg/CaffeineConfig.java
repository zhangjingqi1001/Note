package com.heima.item.confg;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.heima.item.pojo.Item;
import com.heima.item.pojo.ItemStock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CaffeineConfig {
    //  import com.github.benmanes.caffeine.cache.Cache;
    @Bean
    public Cache<Long, Item> itemCache() {
        return Caffeine.newBuilder()
//              初始化容量为100
                .initialCapacity(100)
//              最大容量10000
                .maximumSize(10000)
                .build();
    }

    @Bean
    public Cache<Long, ItemStock> stockCache() {
        return Caffeine.newBuilder()
//              初始化容量为100
                .initialCapacity(100)
//              最大容量10000
                .maximumSize(10000)
                .build();
    }
}
