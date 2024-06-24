package com.itheima.publisher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue() {
        // 队列名称
        String queueName = "simple.queue";
        //消息
        String message = "hello,simple.queue";
        //发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    void testWorkQueue() throws InterruptedException {
        String queueName = "work.queue";
        for (int i = 1; i <= 50; i++) {
            String msg = "hello, worker, message_" + i;
            rabbitTemplate.convertAndSend(queueName, msg);
            Thread.sleep(20);
        }
    }


    @Test
    void testSendMessage2Queue() {
        String queueName = "simple.queue";
        String msg = "hello, amqp!";
        rabbitTemplate.convertAndSend(queueName, msg);
    }


    @Test
    void testSendFanout() {
        String exchangeName = "hmall.fanout";
        String msg = "hello, everyone!";
        rabbitTemplate.convertAndSend(exchangeName, null, msg);
    }

    @Test
    void testSendDirect() {
        String exchangeName = "hmall.direct";
        String msg = "蓝色通知，警报解除，哥斯拉是放的气球";
        rabbitTemplate.convertAndSend(exchangeName, "blue", msg);
    }

    @Test
    void testSendTopic() {
        String exchangeName = "hmall.topic";
        String msg = "今天天气挺不错，我的心情的挺好的";
        rabbitTemplate.convertAndSend(exchangeName, "china.weather", msg);

        rabbitTemplate.convertAndSend(exchangeName, "china.news", "中国的新闻");

        rabbitTemplate.convertAndSend(exchangeName, "japan.news", "日本新闻");
    }

    @Test
    void testSendObject() {
        Map<String, Object> msg = new HashMap<>(2);
        msg.put("name", "jack");
        msg.put("age", 21);
        //我们发送的是一个对象，Spring在接受这个对象的时候会将其转换成字节的形似
        rabbitTemplate.convertAndSend("object.queue", msg);
    }

    @Test
    void testConfirm() throws InterruptedException {
        //1.创建cd
        //CorrelationData里面首先要有一个uuid，是当前消息的一个标识，每一次发消息都要有这样的一个data，这样以来到达MQ以后才能分辨哪个是哪个消息，防止消息之间产生混乱
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //2.添加
        //回调方面，我们采用了JDK中的Future（直接一段代码得到一个Future，但是拿到Future的那一刻，并没有拿到结果，因为是异步的，等执行成功后，才能从中取出结果），为其添加一个消息ConfirmCallback
        correlationData.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            //此ConfirmCallback有两个结果，onSuccess是成功onFailure是失败，但是这个失败不是ACK也不是NACK，而是指Spring内部在处理的时候失败了，这个失败和MQ没有任何的关系，这种失败一般情况下是不会触发的；
            @Override
            public void onFailure(Throwable ex) {
                log.error("消息回调失败", ex);
                System.out.println("消息回调失败" + ex);
            }

            //onSuccess是指MQ的回调成功了
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                log.debug("收到Confirm Callback回执");
                System.out.println("收到Confirm Callback回执");
                if (result.isAck()) {
                    log.debug("消息发送成功");
                    System.out.println("消息发送成功");
                } else {
                    log.debug("消息发送失败，原因：{}", result.getReason());
                    System.out.println("消息发送失败" + result.getReason());
                }
            }
        });

        rabbitTemplate.convertAndSend("hmall.direct", "red5", "hello", correlationData);//直接向队列发送消息

        Thread.sleep(5000); //睡眠等待回执

    }

    @Test
    void etstPageOut() {
        Message message = MessageBuilder.withBody("hello".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT)//设置消息为非持久化的
                .build();

        for (int i = 0; i < 1000000; i++) {
            rabbitTemplate.convertAndSend("lazy.queue", message);//直接向队列发送消息
        }
    }

    @Test
    void testSendTTLMessage() {
        Message message = MessageBuilder
                .withBody("hello".getBytes(StandardCharsets.UTF_8))
                .setExpiration("10000") //过期时间10s
                .build();
        //发送到死信队列
        rabbitTemplate.convertAndSend("simple.direct", "hi", message);//直接向队列发送消息
        log.info("消息发送成功！");
    }

    @Test
    void testSendDelayMessage() {
//        Message message = MessageBuilder
//                .withBody("hello".getBytes(StandardCharsets.UTF_8))
//                .setExpiration("10000") //过期时间10s
//                .build();
//        //发送到死信队列
        rabbitTemplate.convertAndSend("dela.direct", "hi", "hello", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("10000");//延迟十秒
                return message;
            }
        });//直接向队列发送消息
        log.info("消息发送成功！");
    }
}
