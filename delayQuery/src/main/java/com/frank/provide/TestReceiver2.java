package com.frank.provide;

import com.frank.commom.FrankContent;
import com.rabbitmq.client.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author franyang
 * @date 2018/9/10
 */
@Component
public class TestReceiver2 implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        // 消息消费处理
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    String message = new String(body, "UTF-8");
                    System.out.println("Received '" + message + "'");

                    // 消息处理函数
                  //  handler.handle(message, getOrigRoutingKey(properties, envelope.getRoutingKey()));

                } catch (Exception e) {
                    long retryCount = getRetryCount(properties);
                    System.out.println(retryCount+"sdsadsdsaddsfsdfsdffdf");
                    if (retryCount > 3) {
                        // 重试次数大于3次，则自动加入到失败队列
                        System.out.println("failed. send message to failed exchange");

                        Map<String, Object> headers = new HashMap<>();
                        headers.put("x-orig-routing-key", getOrigRoutingKey(properties, envelope.getRoutingKey()));
                        //channel.basicPublish(failedExchangeName(), queueName, createOverrideProperties(properties, headers), body);
                    } else {
                        // 重试次数小于3，则加入到重试队列，30s后再重试
                        System.out.println("exception. send message to retry exchange");

                        Map<String, Object> headers = properties.getHeaders();
                        if (headers == null) {
                            headers = new HashMap<>();
                        }

                        headers.put("x-orig-routing-key", getOrigRoutingKey(properties, envelope.getRoutingKey()));
                        //channel.basicPublish(retryExchangeName(), queueName, createOverrideProperties(properties, headers), body);
                    }
                }

                // 注意，由于使用了basicConsume的autoAck特性，因此这里就不需要手动执行
                // channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        // 执行消息消费处理
        channel.basicConsume(FrankContent.DELAY_PROCESS_QUEUE_NAME, true, consumer);
    }


    public void processMessage(String message) throws Exception { ;
        System.out.println("Received <" + message + ">");
        if (Objects.equals(message, "fail")) {
            throw new Exception("Some exception happened");
        }
    }

    /**
     * 获取原始的routingKey
     *
     * @param properties   AMQP消息属性
     * @param defaultValue 默认值
     * @return 原始的routing-key
     */
    protected String getOrigRoutingKey(AMQP.BasicProperties properties, String defaultValue) {
        String routingKey = defaultValue;
        try {
            Map<String, Object> headers = properties.getHeaders();
            if (headers != null) {
                if (headers.containsKey("x-orig-routing-key")) {
                    routingKey = headers.get("x-orig-routing-key").toString();
                }
            }
        } catch (Exception ignored) {
        }

        return routingKey;
    }

    /**
     * 获取消息重试次数
     *
     * @param properties AMQP消息属性
     * @return 消息重试次数
     */
    protected Long getRetryCount(AMQP.BasicProperties properties) {
        Long retryCount = 0L;
        try {
            Map<String, Object> headers = properties.getHeaders();
            if (headers != null) {
                if (headers.containsKey("x-death")) {
                    List<Map<String, Object>> deaths = (List<Map<String, Object>>) headers.get("x-death");
                    if (deaths.size() > 0) {
                        Map<String, Object> death = deaths.get(0);
                        retryCount = (Long) death.get("count");
                    }
                }
            }
        } catch (Exception ignored) {
        }

        return retryCount;
    }

}
