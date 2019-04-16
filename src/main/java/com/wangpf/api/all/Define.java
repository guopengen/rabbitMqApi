package com.wangpf.api.all;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangpf
 * @Description: TODO
 * @date 2019/4/15 14:29
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

        String exchangeName = "test_all_exchange";
        String exchangeType = "topic";
        String queueName = "test_all_queue";
        String routingKey ="test.all.#";


        //声明一个交换机,且交换机类型为direct,durable参数为是否持久化
        channel.exchangeDeclare(exchangeName,exchangeType,true,false,null);

        Map<String, Object> agruments = new HashMap<String, Object>();
        agruments.put("x-dead-letter-exchange", "dlx_exchange");//指定死信队列的exchange
        agruments.put("x-dead-letter-routing-key", "to_dead");//指定发送到死信队列的routingKey,如果不指定那么默认使用原来队列的routingKey,就是test.dlx.#,就是相当于消息中自带的routingKey;如果送不到死信队列，到了有效期就清空这些消息了
        //agruments.put("x-message-ttl",10000);//指定队列中消息过期时间为10s
        //声明一个队列,durable参数为是否持久化
        channel.queueDeclare(queueName,true,false,false,agruments);
        //建立一个绑定的关系
        channel.queueBind(queueName,exchangeName,routingKey);

        channel.close();
        connection.close();
    }
}
