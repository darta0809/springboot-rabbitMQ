package com.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DirectConsumer {

  enum Action {
    ACCEPT, RETRY, REJECT
  }

  @RabbitListener(queues = "DIRECT_QUEUE_1")
  public void process(String content, Message message, Channel channel) {
    Action action = Action.ACCEPT;
    long tag = message.getMessageProperties().getDeliveryTag();

    try {
      log.info("Direct mode - 1 received queue 1 message: {}", content);
    } catch (Exception e) {
      action = Action.RETRY;
      e.printStackTrace();
    } finally {
      try {
        switch (action) {
          case ACCEPT -> channel.basicAck(tag, false);
          case RETRY -> channel.basicNack(tag, false, true);
          case REJECT -> channel.basicNack(tag, false, false);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
