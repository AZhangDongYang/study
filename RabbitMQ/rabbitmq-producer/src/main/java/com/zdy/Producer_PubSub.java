package com.zdy;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer_PubSub {
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

        // 5. 创建交换机
        /*
        * exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments)
        * exchange: 交换机名称
        * type: 交换机类型
        *           DIRECT("direct"),定向
        *           FANOUT("fanout"),广播 发送到每一个与该交换机绑定的队列
        *           TOPIC("topic"),通配符方式
        * durable: 是否持久化
        * autoDelete: 是否自动删除
        * internal: 内部使用 一般为false
        * arguments: 参数
        * */
        String exchangeName = "fanout_exchange";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true, false, false, null);

        // 6. 创建队列
        /*
         * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
         * queue: 队列名称
         * durable: 是否持久化(当MQ关闭再启动时数据还在)
         * exclusive: 是否独占(只能有一个消费者监听该队列)
         * autoDelete: 当没有消费者时，是否自动删除队列
         * arguments： 参数
         * */
        String queue1Name = "fanout_queue1";
        String queue2Name = "fanout_queue2";
        channel.queueDeclare(queue1Name, true, false, false, null);
        channel.queueDeclare(queue2Name, true, false, false, null);

        // 7. 绑定交换机与队列
        /*
        * queueBind(String queue, String exchange, String routingKey)
        * queue: 队列名称
        * exchange: 交换机名称
        * routingKey： 路由key 因为交换机类型选为FANOUT 所以不需要routingKey
        * */
        channel.queueBind(queue1Name, exchangeName, "");
        channel.queueBind(queue2Name, exchangeName, "");

        // 发送消息
        String body = "hello rabbitmq";
        channel.basicPublish(exchangeName, "", null, body.getBytes());
        channel.close();
        connection.close();
    }
}
