package com.frank.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.frank.commom.FrankContent.DEAD_LETTER_EXCHANGE;
import static com.frank.commom.FrankContent.DEAD_ROUTING_KEY;
import static com.frank.commom.FrankContent.ROUTING_KEY;

/**
 * @author franyang
 * @date 2018/9/10
 */
@Configuration
public class RabbitConfig {
    @Bean
    public Queue maintainQueue() {
        Map<String,Object> args=new HashMap<>();
        // 设置该Queue的死信的信箱
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // 设置死信routingKey
        args.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        return new Queue(ROUTING_KEY,true,false,false,args);
    }

    @Bean
    public Binding maintainBinding() {
        return BindingBuilder.bind(maintainQueue()).to(DirectExchange.DEFAULT)
                .with(ROUTING_KEY);
    }

    @Bean
    public Queue deadLetterQueue(){
        return new Queue(DEAD_ROUTING_KEY);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    public Binding deadLetterBindding(){
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DEAD_ROUTING_KEY);
    }
































//    /**
//     * 创建DLX exchange
//     *
//     * @return
//     */
//    @Bean
//    DirectExchange delayExchange() {
//        return new DirectExchange(DELAY_EXCHANGE_NAME);
//    }
//
//    /**
//     * 创建per_queue_ttl_exchange
//     *
//     * @return
//     */
//    @Bean
//    DirectExchange perQueueTTLExchange() {
//        return new DirectExchange(PER_QUEUE_TTL_EXCHANGE_NAME);
//    }
//
//    /**
//     * 创建delay_queue_per_message_ttl队列
//     *
//     * @return
//     */
//    @Bean
//    Queue delayQueuePerMessageTTL() {
//        return QueueBuilder.durable(DELAY_QUEUE_PER_MESSAGE_TTL_NAME)
//                .withArgument("x-dead-letter-exchange", DELAY_EXCHANGE_NAME) // DLX，dead letter发送到的exchange
//                .withArgument("x-dead-letter-routing-key", DELAY_PROCESS_QUEUE_NAME) // dead letter携带的routing key
//                .build();
//    }
//
//    /**
//     * 创建delay_queue_per_queue_ttl队列
//     *
//     * @return
//     */
//    @Bean
//    Queue delayQueuePerQueueTTL() {
//        return QueueBuilder.durable(DELAY_QUEUE_PER_QUEUE_TTL_NAME)
//                .withArgument("x-dead-letter-exchange", DELAY_EXCHANGE_NAME) // DLX
//                .withArgument("x-dead-letter-routing-key", DELAY_PROCESS_QUEUE_NAME) // dead letter携带的routing key
//                .withArgument("x-message-ttl", QUEUE_EXPIRATION) // 设置队列的过期时间
//                .build();
//    }
//
//    /**
//     * 创建delay_process_queue队列，也就是实际消费队列
//     *
//     * @return
//     */
//    @Bean
//    Queue delayProcessQueue() {
//        return QueueBuilder.durable(DELAY_PROCESS_QUEUE_NAME)
//                .build();
//    }
//
//    /**
//     * 将DLX绑定到实际消费队列
//     *
//     * @param delayProcessQueue
//     * @param delayExchange
//     * @return
//     */
//    @Bean
//    Binding dlxBinding(Queue delayProcessQueue, DirectExchange delayExchange) {
//        return BindingBuilder.bind(delayProcessQueue)
//                .to(delayExchange)
//                .with(DELAY_PROCESS_QUEUE_NAME);
//    }
//
//    /**
//     * 将per_queue_ttl_exchange绑定到delay_queue_per_queue_ttl队列（统一失效时间，用于队列延迟重试）
//     *
//     * @param delayQueuePerQueueTTL
//     * @param perQueueTTLExchange
//     * @return
//     */
//    @Bean
//    Binding queueTTLBinding(Queue delayQueuePerQueueTTL, DirectExchange perQueueTTLExchange) {
//        return BindingBuilder.bind(delayQueuePerQueueTTL)
//                .to(perQueueTTLExchange)
//                .with(DELAY_QUEUE_PER_QUEUE_TTL_NAME);
//    }
//


//    //新建一个监听容器用于存放消费者
//    /**
//     * 定义delay_process_queue队列的Listener Container
//     *
//     * @param connectionFactory
//     * @return
//     */
//    @Bean
//    SimpleMessageListenerContainer processContainer(ConnectionFactory connectionFactory, TestReceiver2 testReceiver2) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(DELAY_PROCESS_QUEUE_NAME); // 监听delay_process_queue
//        container.setMessageListener(new MessageListenerAdapter(testReceiver2));
//        return container;
//    }
}
