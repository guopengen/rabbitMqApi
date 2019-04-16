package com.wangpf.api.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author wangpf
 * @Description:
 * 1、exchange类型为topic,则消息通过消息的routingKey对照exchange和队列绑定的routingKey,从而发送到对应的队列中。
 * 2、一条消息只能进入到一个队列中
 * @date 2019/4/4 16:27
 */
public class TopicDefine {
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

        String exchangeName = "test_topic_exchange";
        String exchangeType = "topic";
        String queueName = "test_topic_queue";
        String routingKey ="test.topic.#";


        //声明一个交换机,且交换机类型为direct,durable参数为是否持久化
        channel.exchangeDeclare(exchangeName,exchangeType,true);
        //声明一个队列,durable参数为是否持久化
        channel.queueDeclare(queueName,true,false,false,null);
        //建立一个绑定的关系
        channel.queueBind(queueName,exchangeName,routingKey);

        channel.close();
        connection.close();
    }
}
