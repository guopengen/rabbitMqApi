package com.wangpf.api.dlx;

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
 * 3、这里有个问题
 *    假如第一条发送过去，但是过期时间很长，那么其他后面消息即使比第一条消息过期时间短，依然要等着第一条过期后，才能进入到死信队列中
 * @date 2019/4/4 15:25
 */
public class DlxProducer {
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

        String exchangeName = "test_dlx_exchange";
        String routingKey = "test.dlx";

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                .expiration("100000")
                .contentEncoding("UTF-8")
                .build();

        AMQP.BasicProperties properties2 = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                .expiration("5000")
                .contentEncoding("UTF-8")
                .build();

       /* for(int i=0;i<5;i++){*/
            String msg = "1 Hello RabbitMQ!";
            //发送消息,mandatory这个参数,如果为true,则消息没有路由到一个队列中,则消息返回生产者
            channel.basicPublish(exchangeName, routingKey,false, properties, msg.getBytes());
       /* }*/

        String msg2 = "2 Hello RabbitMQ!";
        //发送消息,mandatory这个参数,如果为true,则消息没有路由到一个队列中,则消息返回生产者
        channel.basicPublish(exchangeName, routingKey,false, properties2, msg2.getBytes());
        //关闭连接
        channel.close();
        connection.close();
    }
}
