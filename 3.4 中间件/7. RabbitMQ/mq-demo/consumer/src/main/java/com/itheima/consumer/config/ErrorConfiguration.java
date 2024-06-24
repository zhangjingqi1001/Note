package com.itheima.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConditionalOnProperty当某个属性满足条件时会生效
@ConditionalOnProperty(prefix = "spring.rabbitmq.listener.simple.retry",
        //当spring.rabbitmq.listener.simple.retry.enabled=true的时候，会加载下面的配置
        name = "enabled",
        havingValue = "true")
public class ErrorConfiguration {
    //交换机
    @Bean
    public DirectExchange errorExchange() {
        // 交换机名称error.direct
        return new DirectExchange("error.direct");
    }

    //定义队列
    @Bean
    public Queue errorQueue() {
        return new Queue("error.queue");
    }

    // 绑定
    @Bean
    public Binding errorBinding(Queue errorQueue, DirectExchange errorExchange) {
        //"error"是交换机和队列绑定的那个key
        return BindingBuilder.bind(errorQueue).to(errorExchange).with("error");
    }

    //配置消息转换
    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        //RabbitTemplate是AmqpTemplate的子类
        return new RepublishMessageRecoverer(rabbitTemplate, "error.direct", "error");
    }
}
