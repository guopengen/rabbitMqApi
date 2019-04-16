package com.wangpf.api.manyConsumer;

import com.rabbitmq.client.*;

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

        String exchangeName = "test_thread_exchange";
        String routingKey = "test.thread";

        //发送一条消息
        for(int i=1;i<101;i++){
            String msg = "Hello RabbitMQ Send return message  "+i;
            //注意这里的mandatory必须设置为true,这样才能监听才到监控到
            channel.basicPublish(exchangeName, routingKey, false,null, msg.getBytes());
        }

        //关闭连接
        channel.close();
        connection.close();
    }
}
