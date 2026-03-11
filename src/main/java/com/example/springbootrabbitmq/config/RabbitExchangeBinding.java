package com.example.springbootrabbitmq.config;

import com.example.springbootrabbitmq.enums.ExchangeEnum;
import com.example.springbootrabbitmq.enums.QueueEnum;
import com.example.springbootrabbitmq.enums.RoutingKeyEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitExchangeBinding {

    // ==================== Exchange ====================

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(ExchangeEnum.DIRECT_EXCHANGE.getCode());
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(ExchangeEnum.TOPIC_EXCHANGE.getCode());
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(ExchangeEnum.FANOUT_EXCHANGE.getCode());
    }

    // ==================== Queue ====================

    @Bean
    Queue directQueue1() {
        return new Queue(QueueEnum.DIRECT_QUEUE_1.getCode());
    }

    @Bean
    Queue topicQueue1() {
        return new Queue(QueueEnum.TOPIC_QUEUE_1.getCode());
    }

    @Bean
    Queue topicQueue2() {
        return new Queue(QueueEnum.TOPIC_QUEUE_2.getCode());
    }

    @Bean
    Queue fanoutQueue1() {
        return new Queue(QueueEnum.FANOUT_QUEUE_1.getCode());
    }

    @Bean
    Queue fanoutQueue2() {
        return new Queue(QueueEnum.FANOUT_QUEUE_2.getCode());
    }

    // ==================== Binding ====================

    @Bean
    Binding bindingDirectQueue1(Queue directQueue1, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue1).to(directExchange)
                .with(RoutingKeyEnum.DIRECT_KEY_1.getCode());
    }

    @Bean
    Binding bindingTopicQueue1(Queue topicQueue1, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue1).to(topicExchange)
                .with(RoutingKeyEnum.TOPIC_KEY_1.getCode());
    }

    @Bean
    Binding bindingTopicQueue2(Queue topicQueue2, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue2).to(topicExchange)
                .with(RoutingKeyEnum.TOPIC_KEY_2.getCode());
    }

    @Bean
    Binding bindingFanoutQueue1(Queue fanoutQueue1, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    @Bean
    Binding bindingFanoutQueue2(Queue fanoutQueue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}
