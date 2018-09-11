package com.frank;

import com.frank.sender.TestSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author franyang
 * @date 2018/9/10
 */
@RestController
@SpringBootApplication
public class Application {

    @Autowired
    TestSender testSender;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/test")
    public void setTestSender(){
        System.out.println("test");
        testSender.testSender("test");
    }

    @GetMapping("/test/{message}")
    public void setTestSender(@PathVariable("message") String message){
        testSender.testSender(message);
    }
}
