package com.wangpf.api.objectMessage;

import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/** 
 * @description: 
 * @auther: wangpf
 * @return:
 */
public class Consumer {
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

        String queueName = "test_object_queue";
        //创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        //消费者和队列绑定
        channel.basicConsume(queueName, true, queueingConsumer);

        while(true){
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.err.println("消费端: " + msg);
            OrderConsumer orderConsumer = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(msg, OrderConsumer.class);
            System.out.println(orderConsumer);
        }

    }
}
