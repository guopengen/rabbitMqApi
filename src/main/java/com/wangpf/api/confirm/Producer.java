package com.wangpf.api.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

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

        //指定消息的投递模式 消息确认模式
        channel.confirmSelect();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "test.confirm.save";

        //发送一条消息
        String msg = "Hello RabbitMQ Send confirm message!";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        //添加一个监听
        //疑问：1、没有对应的队列，为何会返回ack
        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.err.println("-------ack!-----------");
            }
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.err.println("-------no ack!-----------");
            }
        });

        //关闭连接
        /*channel.close();
        connection.close();*/
    }
}
