package com.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicConsumer {

  @RabbitListener(queues = "TOPIC_QUEUE_1")
  public void process(String content, Message message, Channel channel) {
    log.info("Topic mode - received queue 1 message: {}", content);
  }

  @RabbitListener(queues = "TOPIC_QUEUE_2")
  public void process2(String content, Message message, Channel channel) {
    log.info("Topic mode - received queue 2 message: {}", content);
  }
}
