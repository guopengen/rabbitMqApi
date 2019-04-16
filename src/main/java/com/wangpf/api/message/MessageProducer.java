package com.wangpf.api.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangpf
 * @Description:
 * 1、生产端只关注exchange和routingKey,然后发送消息。
 * 2、如果exchange为空，那么会根据routingKey去寻找和routingKey相同名称的队列。
 * @date 2019/4/4 15:25
 */
public class MessageProducer {
    public static void main(String[] args) throws Exception{
        //创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("172.16.0.77");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("wangpf");
        connectionFactory.setPassword("wangpf");

        /*connectionFactory.setUri(new URI("amqp://acq_v3:acq_v3@172.16.0.93:5672/%2facq_v3"));*/
        //创建Connection
        Connection connection = connectionFactory.newConnection();
        //创建Channel
        Channel channel = connection.createChannel();

        String exchangeName = "test_message_exchange";
        String routingKey = "test.message";

        Map<String, Object> myHeaders = new HashMap<String,Object>();
        myHeaders.put("my1", "111");
        myHeaders.put("my2", "222");

        //deliveryMode 设置为2,说明消息持久化。
        //expiration 消息的有效期时间
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .expiration("10000")
                .headers(myHeaders)
                .build();

        for(int i=0;i<5;i++){
            String msg = "Hello RabbitMQ!";
            //发送消息
            channel.basicPublish(exchangeName, routingKey, properties, msg.getBytes());
        }
        //关闭连接
        channel.close();
        connection.close();
    }
}
