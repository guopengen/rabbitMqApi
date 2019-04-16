package com.wangpf.api.manyConsumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @author wangpf
 * @Description: TODO
 * @date 2019/4/8 11:20
 */
public class Consumer2 {
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

        String queueName = "test_thread_queue";
        //创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        //消费者和队列绑定
        channel.basicConsume(queueName, false, queueingConsumer);

        channel.basicQos(1);

        while(true){
            Thread.sleep(5000);
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.err.println("结束-消费端2: " + msg);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        }

    }
}
