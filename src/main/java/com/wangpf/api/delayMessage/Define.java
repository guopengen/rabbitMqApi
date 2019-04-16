package com.wangpf.api.delayMessage;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

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

        String exchangeName = "test_delay_exchange";
        String exchangeType = "x-delayed-message";
        String queueName = "test_delay_queue";
        String routingKey ="test.delay.#";

        Map<String, Object> arguments = new HashMap<String,Object>();
        arguments.put("x-delayed-type","topic");
        //声明一个交换机,且交换机类型为direct,durable参数为是否持久化
        channel.exchangeDeclare(exchangeName,exchangeType,true,false,arguments);
        //声明一个队列,durable参数为是否持久化
        channel.queueDeclare(queueName,true,false,false,null);
        //建立一个绑定的关系
        channel.queueBind(queueName,exchangeName,routingKey);

        channel.close();
        connection.close();
    }
}
