package com.frank.sender;

import com.alibaba.fastjson.JSON;
import com.frank.commom.FrankContent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author franyang
 * @date 2018/9/10
 */
@Service
public class TestSender {
    @Autowired
    private AmqpTemplate template;

    public void  testSender(String message){
        template.convertAndSend(FrankContent.ROUTING_KEY,JSON.toJSON(message));
    }
}
