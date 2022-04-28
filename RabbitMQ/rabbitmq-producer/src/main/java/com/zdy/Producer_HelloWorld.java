package com.zdy;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_HelloWorld {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1. 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 2. 设置参数
        connectionFactory.setHost("127.0.0.1"); // IP地址 默认地址localhost
        connectionFactory.setPort(5672); // 端口号 默认5672
        connectionFactory.setVirtualHost("/demo_virtual"); // 虚拟机名称 默认/
        connectionFactory.setUsername("zdy"); // 用户名 默认guest
        connectionFactory.setPassword("zdy"); // 密码 默认guest
        // 3. 创建连接 Connection
        Connection connection = connectionFactory.newConnection();
        // 4. 创建频道 Channel
        Channel channel = connection.createChannel();
        // 5. 创建队列
        /*
         * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
         * queue: 队列名称
         * durable: 是否持久化(当MQ关闭再启动时数据还在)
         * exclusive: 是否独占(只能有一个消费者监听该队列)
         * autoDelete: 当没有消费者时，是否自动删除队列
         * arguments： 参数
         * */
        channel.queueDeclare("hello_world", true, false, false, null);
        // 6. 发送消息
        /*
        * basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
        * exchange: 交换机名称
        * routingKey： 路由key
        * props： 属性
        * body: 要发送的消息
        * */
        String body = "hello rabbitmq";
        channel.basicPublish("", "hello_world", null, body.getBytes());
        channel.close();
        connection.close();
    }
}
