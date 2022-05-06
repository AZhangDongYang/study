package com.zdy;

import com.zdy.config.RabbitMQConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Test
    public void testTopicsSend(){
//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            @Override
//            public void confirm(CorrelationData correlationData, boolean b, String s) {
//                if (b){
//                    System.out.println("发送成功");
//                }else {
//                    System.out.println("发送失败");
//                }
//            }
//        });
//        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
//            @Override
//            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
//                System.out.println(new String(message.getBody()));
//            }
//        });
//        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "log.error", "hello rabbitmq");
//        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "log.infoxx.xxx.xx", "hello rabbitmq");
        MessageProperties messageProperties = new MessageProperties();

        //设置过期时间
        messageProperties.setExpiration("10000");
        CorrelationData correlationData = new CorrelationData();

        Message message = new Message("hello rabbitmq".getBytes(), messageProperties);

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "asd.dead", message);
    }
}
