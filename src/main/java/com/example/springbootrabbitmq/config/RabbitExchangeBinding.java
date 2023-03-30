package com.example.springbootrabbitmq.config;

import com.example.springbootrabbitmq.enums.ExchangeEnum;
import com.example.springbootrabbitmq.enums.QueueEnum;
import com.example.springbootrabbitmq.enums.RoutingKeyEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AutoConfigureAfter(RabbitMQConfig.class)
@RequiredArgsConstructor
public class RabbitExchangeBinding {

  private final RabbitAdmin rabbitAdmin;

  @Bean
  DirectExchange directExchange() {
    DirectExchange directExchange = new DirectExchange(
        ExchangeEnum.DIRECT_EXCHANGE.getCode());
    rabbitAdmin.declareExchange(directExchange);
    log.info("Direct exchange create SUCCESS");
    return directExchange;
  }

  @Bean
  TopicExchange topicExchange() {
    TopicExchange topicExchange = new TopicExchange(ExchangeEnum.TOPIC_EXCHANGE.getCode());
    rabbitAdmin.declareExchange(topicExchange);
    log.info("Topic exchange create SUCCESS");
    return topicExchange;
  }

  @Bean
  FanoutExchange fanoutExchange() {
    FanoutExchange fanoutExchange = new FanoutExchange(ExchangeEnum.FANOUT_EXCHANGE.getCode());
    rabbitAdmin.declareExchange(fanoutExchange);
    log.info("Fanout exchange create SUCCESS");
    return fanoutExchange;
  }

  @Bean
  Queue directQueue1() {
    Queue queue = new Queue(QueueEnum.DIRECT_QUEUE_1.getCode());
    rabbitAdmin.declareQueue(queue);
    log.info("Direct queue 1 create SUCCESS");
    return queue;
  }

  @Bean
  Queue topicQueue1() {
    Queue queue = new Queue(QueueEnum.TOPIC_QUEUE_1.getCode());
    rabbitAdmin.declareQueue(queue);
    log.info("Topic queue 1 create SUCCESS");
    return queue;
  }

  @Bean
  Queue topicQueue2() {
    Queue queue = new Queue(QueueEnum.TOPIC_QUEUE_2.getCode());
    rabbitAdmin.declareQueue(queue);
    log.info("Topic queue 2 create SUCCESS");
    return queue;
  }

  @Bean
  Queue fanoutQueue1() {
    Queue queue = new Queue(QueueEnum.FANOUT_QUEUE_1.getCode());
    rabbitAdmin.declareQueue(queue);
    log.info("Fanout queue 1 create SUCCESS");
    return queue;
  }

  @Bean
  Queue fanoutQueue2() {
    Queue queue = new Queue(QueueEnum.FANOUT_QUEUE_2.getCode());
    rabbitAdmin.declareQueue(queue);
    log.info("Fanout queue 2 create SUCCESS");
    return queue;
  }

  @Bean
  Binding bindingDirectQueue1(Queue directQueue1, DirectExchange exchange) {
    Binding binding = BindingBuilder.bind(directQueue1).to(exchange)
        .with(RoutingKeyEnum.DIRECT_KEY_1.getCode());
    rabbitAdmin.declareBinding(binding);
    log.info("Direct queue 1 binding SUCCESS");
    return binding;
  }

  @Bean
  Binding bindingTopicQueue1(Queue topicQueue1, TopicExchange exchange) {
    Binding binding = BindingBuilder.bind(topicQueue1).to(exchange)
        .with(RoutingKeyEnum.TOPIC_KEY_1.getCode());
    rabbitAdmin.declareBinding(binding);
    log.info("Topic queue 1 binding SUCCESS");
    return binding;
  }

  @Bean
  Binding bindingTopicQueue2(Queue topicQueue2, TopicExchange exchange) {
    Binding binding = BindingBuilder.bind(topicQueue2).to(exchange)
        .with(RoutingKeyEnum.TOPIC_KEY_2.getCode());
    rabbitAdmin.declareBinding(binding);
    log.info("Topic queue 2 binding SUCCESS");
    return binding;
  }

  @Bean
  Binding bindingFanoutQueue1(Queue fanoutQueue1, FanoutExchange exchange) {
    Binding binding = BindingBuilder.bind(fanoutQueue1).to(exchange);
    rabbitAdmin.declareBinding(binding);
    log.info("Fanout queue 1 binding SUCCESS");
    return binding;
  }


  @Bean
  Binding bindingFanoutQueue2(Queue fanoutQueue2, FanoutExchange exchange) {
    Binding binding = BindingBuilder.bind(fanoutQueue2).to(exchange);
    rabbitAdmin.declareBinding(binding);
    log.info("Fanout queue 2 binding SUCCESS");
    return binding;
  }
}
