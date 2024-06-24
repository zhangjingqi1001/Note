package com.itheima.publisher.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * ApplicationContextAware是spring提供的和Aware有关的一系列的东西 Aware是通知的意思
 * 当Spring容器初始化完毕的时候，会检查一下哪一些类实现了这个接口，就会去调用这些类中的方法，并且会把ApplicationContext传到方法里面
 */
@Slf4j
@Configuration
public class CommonConfig implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        //设置ReturnCallback
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            //message 消息本身 replyCode响应的状态码 replyText响应的消息 Exchange交换机 routingKey路由信息
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                log.debug("收到消息的Return Callback，exchange:{},routingKey:{},ReplyCode:{},ReplyText:{},message:{},",
                        returnedMessage.getExchange(), returnedMessage.getRoutingKey(),
                        returnedMessage.getReplyCode(), returnedMessage.getReplyText(),
                        returnedMessage.getMessage());
                System.out.println(returnedMessage.getMessage());
            }
        });
    }
}
