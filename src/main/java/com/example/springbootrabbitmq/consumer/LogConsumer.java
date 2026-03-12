package com.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LogConsumer {

    @RabbitListener(queues = "ORDER_LOG_QUEUE")
    public void process(String content, Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("[日誌服務] 收到訂單事件, 記錄日誌: {}", content);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("[日誌服務] 記錄失敗: {}", e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
