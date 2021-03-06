package com.zdy.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "topic_exchange";
    public static final String DEAD_EXCHANGE_NAME = "dead_exchange";
    public static final String QUEUE1_NAME = "topic_queue";
    public static final String QUEUE2_NAME = "dead_queue";

    // 1. 创建Exchange交换机
    @Bean("topic_exchange")
    public Exchange createExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean("dead_exchange")
    public Exchange createDeadExchange(){
        return ExchangeBuilder.topicExchange(DEAD_EXCHANGE_NAME).durable(true).build();
    }

    // 2. 创建Queue队列
//    @Bean("topic_queue1")
//    public Queue createQueue1(){
//        return QueueBuilder.durable(QUEUE1_NAME).build();
//    }
//    @Bean("topic_queue2")
//    public Queue createQueue2(){
//        return QueueBuilder.durable(QUEUE2_NAME).build();
//    }

    @Bean("topic_queue")
    public Queue createQueue1(){
        return QueueBuilder
                .durable(QUEUE1_NAME)
                .withArgument("x-dead-letter-exchange", "dead_exchange")
                .withArgument("x-dead-letter-routing-key", "#.dead")
                .build();
    }
    @Bean("dead_queue")
    public Queue createQueue2(){
        return QueueBuilder.durable(QUEUE2_NAME).build();
    }

    // 3. 创建绑定交换机与队列
//    @Bean
//    public Binding createBinding1Error(@Qualifier("topic_queue1") Queue queue, @Qualifier("topic_exchange") Exchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with("#.error").noargs();
//    }
//    @Bean
//    public Binding createBinding2Error(@Qualifier("topic_queue2") Queue queue, @Qualifier("topic_exchange") Exchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with("#.error").noargs();
//    }
//    @Bean
//    public Binding createBinding2Warn(@Qualifier("topic_queue2") Queue queue, @Qualifier("topic_exchange") Exchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with("#.warning").noargs();
//    }
//    @Bean
//    public Binding createBinding2Info(@Qualifier("topic_queue2") Queue queue, @Qualifier("topic_exchange") Exchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with("*.info").noargs();

    @Bean
    public Binding createBindingTopic(@Qualifier("topic_queue") Queue queue, @Qualifier("topic_exchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("#.dead").noargs();
    }
    @Bean
    public Binding createBindingDead(@Qualifier("dead_queue") Queue queue, @Qualifier("dead_exchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("#.dead").noargs();
    }
}
