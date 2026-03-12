package com.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OrderPaidConsumer {

    /**
     * 只收到 order.paid 事件
     * 業務：通知倉庫出貨
     */
    @RabbitListener(queues = "ORDER_PAID_QUEUE")
    public void process(String content, Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();

        log.info("[訂單付款] 通知倉庫出貨: {}", content);
        channel.basicAck(tag, false);
    }
}
