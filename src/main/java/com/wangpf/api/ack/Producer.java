package com.wangpf.api.ack;

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

        String exchangeName = "test_ack_exchange";
        String routingKey = "test.ack.save";

        for(int i =0; i<5; i ++){

            String msg = "Hello RabbitMQ Send ack message "+i;

            Map<String,Object> headers = new HashMap<String,Object>();
            headers.put("num",i);

            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .headers(headers)
                    .build();
            channel.basicPublish(exchangeName, routingKey, true, properties, msg.getBytes());
        }

        //关闭连接
        channel.close();
        connection.close();
    }
}
