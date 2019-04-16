package com.wangpf.api.all;

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
        System.err.println("-----------ack message  hhhh----------");
        System.err.println("body: " + new String(body));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if((Integer)properties.getHeaders().get("num") == 8) {
            channel.basicNack(envelope.getDeliveryTag(), false, true);
            System.out.println("==============第：" + (Integer)properties.getHeaders().get("num") + "条消息------没有被消费");
        } else {
            channel.basicAck(envelope.getDeliveryTag(), false);
            System.out.println("==============第：" + (Integer)properties.getHeaders().get("num") + "条消息------被消费");
        }
    }
}
