package com.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OrderAllEventConsumer {

    /**
     * 監聽所有訂單事件（routing key: order.*）
     * 不管是 order.created / order.paid / order.cancelled 都會收到
     */
    @RabbitListener(queues = "ORDER_ALL_EVENT_QUEUE")
    public void process(String content, Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();

        log.info("[全部事件日誌] routingKey: {}, 內容: {}", routingKey, content);
        channel.basicAck(tag, false);
    }
}
