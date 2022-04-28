package com.zdy;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_PubSub {
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

        String queue1Name = "fanout_queue1";
        String queue2Name = "fanout_queue2";

        Consumer consumer = new Consumer() {
            public void handleConsumeOk(java.lang.String s) {

            }

            public void handleCancelOk(java.lang.String s) {

            }

            public void handleCancel(java.lang.String s) throws IOException {

            }

            public void handleShutdownSignal(java.lang.String s, ShutdownSignalException e) {

            }

            public void handleRecoverOk(java.lang.String s) {

            }
            /*
             * handleDelivery(java.lang.String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes)
             * s: 标识
             * envelope: 获取一些信息，交换机路由key等
             * basicProperties: 配置信息
             * bytes: 接收到的数据
             * */
            public void handleDelivery(java.lang.String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                System.out.println(new String(bytes));
            }
        };
        // 5. 接收消息
        /*
         * basicConsume(String queue, boolean autoAck, Consumer callback)
         * queue: 接收队列名称
         * autoAck: 是否自动确认(后期消息可靠性文章中进行讲解)
         * callback: 接收到消息执行的回调函数
         * */
        channel.basicConsume(queue1Name, true, consumer); // 消费队列fanout_queue1
        //channel.basicConsume(queue2Name, true, consumer); // 消费队列fanout_queue2
    }
}
