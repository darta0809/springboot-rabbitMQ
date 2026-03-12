package com.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OrderCreatedConsumer {

    /**
     * 只收到 order.created 事件
     * 業務：建立物流單 + 寄確認信
     */
    @RabbitListener(queues = "ORDER_CREATED_QUEUE")
    public void process(String content, Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();

        log.info("[訂單建立] 建立物流單 + 寄送確認信: {}", content);
        channel.basicAck(tag, false);
    }
}
