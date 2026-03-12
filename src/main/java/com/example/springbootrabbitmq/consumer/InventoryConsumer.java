package com.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class InventoryConsumer {

    @RabbitListener(queues = "ORDER_INVENTORY_QUEUE")
    public void process(String content, Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("[庫存服務] 收到訂單事件, 開始扣減庫存: {}", content);
            // 模擬庫存扣減邏輯
            Thread.sleep(500);
            log.info("[庫存服務] 庫存扣減完成");
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("[庫存服務] 處理失敗: {}", e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}
