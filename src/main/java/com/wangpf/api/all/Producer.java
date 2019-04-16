package com.wangpf.api.all;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import com.wangpf.api.objectMessage.Order;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangpf
 * @Description: TODO
 * @date 2019/4/15 14:37
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

        String exchangeName = "test_all_exchange";
        String routingKey = "test223434.all.save";

        for(int i =0; i<5; i ++) {
            //构建消息
            Person person = new Person();
            person.setName("wangxiaoer");
            person.setAge(20);
            person.setBirthDate("2018-02-03 15:15:15");
            //转换为JSON字符串
            String msg = new Gson().toJson(person);

            Map<String, Object> headers = new HashMap<String, Object>();
            headers.put("num", 2);

            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().deliveryMode(2).contentEncoding("UTF-8")
                    //.expiration("10000")
                    .headers(headers).build();
            channel.basicPublish(exchangeName, routingKey, true, properties, msg.getBytes());

        }

        channel.addConfirmListener(new ConfirmListener() {
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("ack: deliveryTag = "+deliveryTag+" multiple: "+multiple);
            }
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("nack: deliveryTag = "+deliveryTag+" multiple: "+multiple);
            }
        });



        //添加一个监听
        //疑问：1、没有对应的队列，为何会返回ack   因为只关心消息是否到达了Broke
        //      2、没有被路由出去，为何会返回ack

        //添加返回监听,发送消息的时候必须设置mandatory为true。表示如果消息没被路由到，broker可以把情况返回给生产端，如果设置成false,则消息直接被清除了
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText,
                                     String exchange, String routingKey,
                                     AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.err.println("--------- handle  return ----------");
                System.err.println("replyCode: " + replyCode);
                System.err.println("replyText: " + replyText);
                System.err.println("exchange: " + exchange);
                System.err.println("routingKey: " + routingKey);
                System.err.println("properties: " + properties);
                System.err.println("body: " + new String(body));
            }
        });


       /* channel.addShutdownListener(new ShutdownListener() {
            public void shutdownCompleted(ShutdownSignalException cause) {

            }
        });*/

        //关闭连接
        /*channel.close();
        connection.close();*/
    }
}
