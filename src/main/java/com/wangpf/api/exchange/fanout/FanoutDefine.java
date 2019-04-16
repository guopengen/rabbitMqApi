package com.wangpf.api.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author wangpf
 * @Description:
 * 1、exchange类型为fanout,则消息发到Exchange上,然后再发到和Exchange绑定的所有队列中,不用管这个routingKey了。
 * 2、同样的消息可以进入到多个队列中
 * @date 2019/4/4 16:27
 */
public class FanoutDefine {
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

        String exchangeName = "test_fanout_exchange";
        String exchangeType = "fanout";
        //channel.queueDeclare().getQueue() 可以创建一个随机队列名
        String queueName1 = "test_fanout_queue_1";
        String queueName2 = "test_fanout_queue_2";
        String routingKey ="";

        //声明一个交换机,且交换机类型为direct,durable参数为是否持久化
        channel.exchangeDeclare(exchangeName,exchangeType,true);
        //声明两个个队列,durable参数为是否持久化
        channel.queueDeclare(queueName1,true,false,false,null);
        channel.queueDeclare(queueName2,true,false,false,null);
        //建立一个绑定的关系
        channel.queueBind(queueName1,exchangeName,routingKey);
        channel.queueBind(queueName2,exchangeName,routingKey);

        channel.close();
        connection.close();
    }
}
