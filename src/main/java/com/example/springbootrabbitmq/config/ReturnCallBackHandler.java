package com.example.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnsCallback;

@Slf4j
public class ReturnCallBackHandler implements ReturnsCallback {

  @Override
  public void returnedMessage(ReturnedMessage returnedMessage) {
    log.info("=========== Returns Call Back ===========");
    log.info("Message: {}", returnedMessage.getMessage());
    log.info("ReplyCode: {}", returnedMessage.getReplyCode());
    log.info("ReplyText: {}", returnedMessage.getReplyText());
    log.info("Exchange: {}", returnedMessage.getExchange());
    log.info("RoutingKey: {}", returnedMessage.getRoutingKey());
  }
}
