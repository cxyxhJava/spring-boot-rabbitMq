package com.frank.provide;

import com.frank.commom.FrankContent;
import com.frank.sender.TestSender;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * @author franyang
 * @date 2018/9/10
 */
@Component
@RabbitListener(queues = FrankContent.ROUTING_KEY)
public class TestReceiver {
    @Autowired
    private TestSender testSender;

    @RabbitHandler
    public void process(String message) throws Exception {

        System.out.println("开始 message:"+message);
        if ("fail".equals(message)){
            throw new Exception();
        }
    }

}
