package com.wangpf.api.returnlistener;

import com.rabbitmq.client.*;

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

        String exchangeName = "test_return_exchange";
        String routingKey = "test.return.save";
        String routingKeyError = "error.return.save";

        //发送一条消息
        String msg = "Hello RabbitMQ Send return message!";
        //注意这里的mandatory必须设置为true,这样才能监听才到监控到
        channel.basicPublish(exchangeName, routingKeyError, true,null, msg.getBytes());
        //channel.basicPublish(exchangeName, routingKey, true,null, msg.getBytes());

        //添加一个返回监听
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText, String exchange,
                                     String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.err.println("---------handle  return----------");
                System.err.println("replyCode: " + replyCode);
                System.err.println("replyText: " + replyText);
                System.err.println("exchange: " + exchange);
                System.err.println("routingKey: " + routingKey);
                System.err.println("properties: " + properties);
                System.err.println("body: " + new String(body));
            }
        });

        //关闭连接
        /*channel.close();
        connection.close();*/
    }
}
