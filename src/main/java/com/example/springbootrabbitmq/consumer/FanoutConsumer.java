package com.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FanoutConsumer {

  @RabbitListener(queues = "FANOUT_QUEUE_1")
  public void process(String content, Message message, Channel channel) {
    log.info("Fanout mode - received queue 1 message: {}", content);
  }

  @RabbitListener(queues = "FANOUT_QUEUE_2")
  public void process2(String content, Message message, Channel channel) throws IOException {
    log.info("Fanout mode - received queue 2 message: {}", content);
    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
  }
}
