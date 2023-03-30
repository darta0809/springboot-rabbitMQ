package com.example.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;

@Slf4j
public class ConfirmCallBackHandler implements ConfirmCallback {

  @Override
  public void confirm(CorrelationData correlationData, boolean ack, String cause) {
    log.info("=========== Confirm Call Back ===========");
    log.info("CorrelationData : {}", correlationData);
    log.info("result: {}", ack);
    if (!ack) {
      log.error("fail reason: {}", cause);
    }
  }
}
