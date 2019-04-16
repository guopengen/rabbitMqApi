package com.wangpf.api.defineConsumer;


import com.rabbitmq.client.*;

/**
 * @author wangpf
 * @Description:
 * 1、消费端只关注queue,然后接受消息。
 * 2、消费端定义了具体的消费者。
 * @date 2019/4/4 15:47
 */
public class Consumer {
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

        String queueName = "test_consumer_queue";

        //队列名称,是否自动ack,这里设置为true,消费端会自动应答服务器。如果设置为false,那么服务端就会有unack的消息,每次启动都会收到消息
        channel.basicConsume(queueName, true, new MyConsumer(channel));
    }
}
