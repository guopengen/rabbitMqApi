package com.wangpf.api.delayMessage;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangpf
 * @Description: TODO
 * @date 2019/4/8 10:54
 * 使用delay插件比普通延迟队列多了一个好处，具体见延迟队列的一个不足点。如果是每条消息的
 */
public class Producer {
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
        String routingKey = "test.delay.save";

        Map<String,Object> headers = new HashMap<String,Object>();
        headers.put("x-delay","1000000");
        Map<String,Object> headers2 = new HashMap<String,Object>();
        headers2.put("x-delay","5000");

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                //.expiration("100000")
                .contentEncoding("UTF-8")
                .headers(headers)
                .build();

        AMQP.BasicProperties properties2 = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                //.expiration("5000")
                .contentEncoding("UTF-8")
                .headers(headers2)
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
