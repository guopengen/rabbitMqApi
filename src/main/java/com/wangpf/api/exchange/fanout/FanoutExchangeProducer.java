package com.wangpf.api.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author wangpf
 * @Description:
 * 1、生产端只关注exchange和routingKey,然后发送消息。
 * 2、如果exchange为空，那么会根据routingKey去寻找和routingKey相同名称的队列。
 * @date 2019/4/4 15:25
 */
public class FanoutExchangeProducer {
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

        String str = "Hello,Rabbit,I am is Fanout-Exchange";
        String exchangeName = "test_fanout_exchange";
        String routingKey = "";
        //发送消息
        for(int i=0;i<10;i++){
            channel.basicPublish(exchangeName,routingKey,null,str.getBytes());
            //Thread.sleep(5000);
        }
        //关闭连接
        channel.close();
        connection.close();
    }
}
