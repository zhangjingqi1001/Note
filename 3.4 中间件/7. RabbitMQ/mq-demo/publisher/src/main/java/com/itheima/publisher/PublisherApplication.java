package com.itheima.publisher;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PublisherApplication {
    public static void main(String[] args) {
        SpringApplication.run(PublisherApplication.class);
    }

//    @Bean
//    public MessageConverter jacksonMessageConvertor(){
//        //import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//        return new Jackson2JsonMessageConverter();
//    }

    @Bean
    public MessageConverter messageConverter(){
        //import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
        //1.定义消息转换器
        Jackson2JsonMessageConverter jjmc = new Jackson2JsonMessageConverter();
        //2.配置自动创建爱你消息id，用于识别不同消息，也可以在业务中基于ID判断是否是重复消息
        jjmc.setCreateMessageIds(true);//改成true后，会自动生成消息ID
        return jjmc;
    }
}
