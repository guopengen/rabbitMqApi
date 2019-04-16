package com.wangpf.api.all;

import com.rabbitmq.client.*;

/**
 * @author wangpf
 * @Description: TODO
 * @date 2019/4/15 14:44
 */
public class Consumer1 {
    public static void main(String[] args) throws Exception{
        //创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.16.0.77");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("wangpf");
        connectionFactory.setPassword("wangpf");

        /*connectionFactory.setUri(new URI("amqp://acq_v3:acq_v3@172.16.0.93:5672/%2facq_v3"));*/
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String queueName = "test_all_queue";

        //channel.basicQos(0, 1, false);

        //队列名称,是否自动ack,这里设置为true,消费端会自动应答服务器。
        // 如果设置为false,且消费端没有手动ack消息，那么服务端就会有unack的消息,每次启动都会收到消息
        channel.basicConsume(queueName, false, new MyConsumer(channel));
    }
}
