package com.wangpf.api.defineConsumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author wangpf
 * @Description: TODO
 * @date 2019/4/8 11:39
 */
public class Define {
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

        String exchangeName = "test_consumer_exchange";
        String exchangeType = "topic";
        String queueName = "test_consumer_queue";
        String routingKey ="test.consumer.#";


        //声明一个交换机,且交换机类型为direct,durable参数为是否持久化
        channel.exchangeDeclare(exchangeName,exchangeType,true,false,null);
        //声明一个队列,durable参数为是否持久化
        channel.queueDeclare(queueName,true,false,false,null);
        //建立一个绑定的关系
        channel.queueBind(queueName,exchangeName,routingKey);

        channel.close();
        connection.close();
    }
}
