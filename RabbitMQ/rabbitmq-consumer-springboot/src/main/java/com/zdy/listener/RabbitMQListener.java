package com.zdy.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

import java.io.IOException;

@Component
public class RabbitMQListener {

//    @RabbitListener(queues = "hello_world_queue")
//    public void listenQueue(Message message){
//        System.out.println(new String(message.getBody()));
//    }

//    @RabbitListener(queues = "fanout_queue1")
//    public void listenQueueFanoutQ1(Message message){
//        System.out.println(new String(message.getBody()));
//    }
//
//    @RabbitListener(queues = "fanout_queue2")
//    public void listenQueueFanoutQ2(Message message){
//        System.out.println(new String(message.getBody()));
//    }

    @RabbitListener(queues = "direct_queue1")
    public void listenQueueFanoutQ1(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            // 业务逻辑处理成功，告诉RabbitMQ，已经接收到消息并做了处理了。这样消息队列这条消息才算真正消费成功
            channel.basicAck(tag, false);
        } catch (Exception e) {
            // 处理过程中，发生了不是业务逻辑的错误异常，则不答复ACK，这样MQ会认为这条消息未成功消费，所以会重新把该条消息放回队列中，直到ACK正常答复
            channel.basicNack(tag, false, true);
        }
        System.out.println(new String(message.getBody()));
    }

    @RabbitListener(queues = "direct_queue2")
    public void listenQueueFanoutQ2(Message message){
        System.out.println(new String(message.getBody()));
    }
}

