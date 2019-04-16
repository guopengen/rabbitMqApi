package com.wangpf.api.limit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @author wangpf
 * @Description: TODO
 * @date 2019/4/8 17:00
 */
public class MyConsumer extends DefaultConsumer{

    private Channel channel ;

    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.err.println("-----------limit message----------");
        System.err.println("consumerTag: " + consumerTag);
        System.err.println("envelope: " + envelope);
        System.err.println("properties: " + properties);
        System.err.println("body: " + new String(body));

        //如果这行注释了，则unack就有一条，其他消息就过不来，因为这条没有unack，其他都得堵塞了
        //就是消费端关闭了，则unack消息就回归到ready状态下
        //rabbitMq重启后数据丢失
        channel.basicAck(envelope.getDeliveryTag(),false);
    }
}
