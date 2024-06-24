package com.itheima.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfiguration {

    //声明FanoutExchange交换机import org.springframework.amqp.core.FanoutExchange;
    @Bean
    public FanoutExchange fanoutExchange() {
        // ExchangeBuilder.fanoutExchange("").build();
        return new FanoutExchange("hmall.fanout2");
    }

    //声明队列import org.springframework.amqp.core.Queue;
    @Bean
    public Queue fanoutQueue3() {
        // QueueBuilder.durable("ff").build(); durable是持久化，当前的队列是持久的队列 与下面new的形式相同
        return new Queue("fanout.queue3");
    }

    //将上面声明的队列和交换机绑定
    @Bean
    public Binding fanoutBinding3(Queue fanoutQueue3, FanoutExchange fanoutExchange) {
        //将哪个队列绑定到哪个交换机
        return BindingBuilder.bind(fanoutQueue3).to(fanoutExchange);
    }

    //声明队列
    @Bean
    public Queue fanoutQueue4() {
        return new Queue("fanout.queue4");
    }

    //绑定队列和交换机
    @Bean
    public Binding fanoutBinding4() {
        //将哪个队列绑定到哪个交换机
        //下面是直接调用了队列和交换机的方法进行绑定的
        //凡是加了Bean的方法都会被动态代理，当我们调用方法时，Spring首先会检查Spring容器中是否有对应的Bean
        //如果有的话，就不会执行方法中的任何内容，直接从容器中取出Bean对象即可，
        //如果没有，便执行方法注入Bean即可
        //所以说这里虽然是直接调用了fanoutQueue4()和fanoutExchange()方法，但是这两个方法也要有@Bean注解
        return BindingBuilder.bind(fanoutQueue4()).to(fanoutExchange());
    }
}
