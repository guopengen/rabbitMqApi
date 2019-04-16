package com.wangpf.api.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangpf
 * @Description:
 * 1、exchange类型为topic,则消息通过消息的routingKey对照exchange和队列绑定的routingKey,从而发送到对应的队列中。
 * 2、一条消息只能进入到一个队列中
 * 3、三种情况下会生成死信
 *    1）消息被拒绝（basic.reject 或者 basic.nack），并且requeue=false,设置为true会重回队列的。
 *    2）消息的过期时间到期了
 *    3）队列长度限制了
 * @date 2019/4/4 16:27
 */
public class DlxDefine {
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

        String exchangeName = "test_dlx_exchange";
        String exchangeType = "topic";
        String queueName = "test_dlx_queue";
        String routingKey ="test.dlx.#";


        //声明一个交换机,且交换机类型为direct,durable参数为是否持久化
        channel.exchangeDeclare(exchangeName,exchangeType,true,false,null);

        Map<String, Object> agruments = new HashMap<String, Object>();
        agruments.put("x-dead-letter-exchange", "dlx_exchange");//指定死信队列的exchange
        agruments.put("x-dead-letter-routing-key", "to_dead");//指定发送到死信队列的routingKey,如果不指定那么默认使用原来队列的routingKey,就是test.dlx.#,就是相当于消息中自带的routingKey;如果送不到死信队列，到了有效期就清空这些消息了
        //agruments.put("x-message-ttl",6000);//指定队列中消息过期时间为6s

        //声明一个队列,durable参数为是否持久化,但是有一个前提是要求exclusive和autoDelete都为false
        //注意这个agruments要设置到该队列中
        //如果设置了agruments,则重复声明没有问题，但是如果改变了参数或增或减，都会报错
        //exclusive 如果是true，那么申明这个queue的connection断了，那么这个队列就被删除了，包括里面的消息
        //autoDelete 如果为true,当和这个队列没有任何exchange,broke就会删除它
        channel.queueDeclare(queueName,true,false,false,agruments);
        //建立一个绑定的关系
        channel.queueBind(queueName,exchangeName,routingKey);


        //声明一个死信队列
        String dlxExchangeName = "dlx_exchange";
        String dlxQueueName = "dlx_queue";
        String dlxExchangeType = "topic";
        String dlxRoutingKey = "#";//可以接受任何其他队列过来的信息,这里可以更换下routingKey试试

        channel.exchangeDeclare(dlxExchangeName,dlxExchangeType,true,false,null);
        channel.queueDeclare(dlxQueueName,true,false,false,null);
        channel.queueBind(dlxQueueName,dlxExchangeName,dlxRoutingKey);

        channel.close();
        connection.close();
    }
}
