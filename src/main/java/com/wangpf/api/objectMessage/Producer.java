package com.wangpf.api.objectMessage;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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

        String exchangeName = "test_object_exchange";
        String routingKey = "test.object.save";

        //构建消息
        Order order = new Order();
        order.setName("wangxiaoer");
        order.setAge(18);
        order.setDate("2018-02-03 15:15:15");
        //转换为JSON字符串
        String msg = new Gson().toJson(order);

        //有个问题，为毛不序列化也可以发送成功,难道是因为已经转换成字符节数组？
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        //关闭连接
        channel.close();
        connection.close();
    }
}
