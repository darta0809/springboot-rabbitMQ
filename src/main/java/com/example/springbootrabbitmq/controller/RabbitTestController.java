package com.example.springbootrabbitmq.controller;

import com.example.springbootrabbitmq.dto.User;
import com.example.springbootrabbitmq.enums.RoutingKeyEnum;
import com.example.springbootrabbitmq.producer.RabbitMQProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RabbitTestController {

    private final RabbitMQProducer producer;

    @GetMapping("testDirect")
    public String directSendMessage() {
        producer.sendRabbitMQDirect(RoutingKeyEnum.DIRECT_KEY_1.getCode(), "I am Direct!");
        return "SUCCESS";
    }

    @GetMapping("testTopic")
    public String topicSendMessage() {
        producer.sendRabbitMQTopic("1.TOPIC.1", "test TOPIC.1");
        producer.sendRabbitMQTopic("cc", "test cc");
        return "SUCCESS";
    }

    @GetMapping("testFanout")
    public String fanoutSendMessage() {
        User user = User.builder().userName("Vincent").userAge(20).userSchool("NTU").build();
        producer.sendRabbitMQFanout(user);

        User user1 = User.builder().userName("Wade").userAge(22).userSchool("NTU").build();
        producer.sendRabbitMQFanout(user1);
        return "SUCCESS";
    }
}
