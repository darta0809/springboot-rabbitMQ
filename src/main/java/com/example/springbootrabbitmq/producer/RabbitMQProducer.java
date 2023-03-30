package com.example.springbootrabbitmq.producer;

import com.example.springbootrabbitmq.enums.ExchangeEnum;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
@RequiredArgsConstructor
public class RabbitMQProducer {

  private final RabbitTemplate rabbitTemplate;

  public void sendRabbitMQDirect(String routingKey, Object message) {
    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
    log.info("[message producer] send message to direct exchange,send: [{}] message: {}",
        correlationData.getId(),
        message);
    rabbitTemplate.convertAndSend(ExchangeEnum.DIRECT_EXCHANGE.getCode(), routingKey, message,
        correlationData);
  }

  public void sendRabbitMQTopic(String routingKey, Object message) {
    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
    log.info("[message producer] send message to topic exchange,send: [{}] message: {}",
        correlationData.getId(),
        message);
    rabbitTemplate.convertAndSend(ExchangeEnum.TOPIC_EXCHANGE.getCode(), routingKey, message,
        correlationData);
  }

  public void sendRabbitMQFanout(Object message) {
    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
    log.info("[message producer] send message to fanout exchange,send: [{}] message: {}",
        correlationData.getId(),
        message);
    rabbitTemplate.convertAndSend(ExchangeEnum.FANOUT_EXCHANGE.getCode(), "", message,
        correlationData);
  }
}
