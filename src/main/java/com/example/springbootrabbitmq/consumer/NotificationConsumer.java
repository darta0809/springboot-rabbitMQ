package com.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private static final int MAX_RETRY = 3;
    private static final String RETRY_COUNT_HEADER = "x-retry-count";

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "ORDER_NOTIFICATION_QUEUE")
    public void process(Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        int retryCount = getRetryCount(message);
        String content = new String(message.getBody());

        try {
            log.info("[通知服務] 收到訂單事件 (第 {} 次嘗試): {}", retryCount + 1, content);

            // 模擬失敗：故意拋出例外（災難模擬時暫時關閉）
            // simulateFailure();

            log.info("[通知服務] 通知發送完成");
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("[通知服務] 發送通知失敗: {}", e.getMessage());

            if (retryCount < MAX_RETRY - 1) {
                // 手動 republish：ACK 當前訊息，帶上累加的重試次數重新發送
                log.warn("[通知服務] 準備第 {} 次重試, 重新發送到 Queue", retryCount + 2);
                republishWithRetryCount(message, retryCount + 1);
                channel.basicAck(tag, false);
            } else {
                // 超過重試上限，NACK(requeue=false) 進入 DLQ
                log.error("[通知服務] 已嘗試 {} 次仍失敗, 送入 DLQ", MAX_RETRY);
                channel.basicNack(tag, false, false);
            }
        }
    }

    /**
     * 模擬失敗：每次都拋出例外
     * 測試完成後可以註解掉這行來觀察正常流程
     */
    private void simulateFailure() {
        throw new RuntimeException("模擬：郵件伺服器連線失敗");
    }

    /**
     * 從自訂 header 取得重試次數
     */
    private int getRetryCount(Message message) {
        Object retryCount = message.getMessageProperties().getHeader(RETRY_COUNT_HEADER);
        return retryCount != null ? (int) retryCount : 0;
    }

    /**
     * 手動 republish 訊息到原本的 Queue，並在 header 累加重試次數
     * 這是業界處理重試的標準做法：不用 nack(requeue=true) 避免無限迴圈
     */
    private void republishWithRetryCount(Message originalMessage, int newRetryCount) {
        Message retryMessage = MessageBuilder
                .fromMessage(originalMessage)
                .setHeader(RETRY_COUNT_HEADER, newRetryCount)
                .build();

        rabbitTemplate.send(
                originalMessage.getMessageProperties().getReceivedExchange(),
                originalMessage.getMessageProperties().getReceivedRoutingKey(),
                retryMessage);
    }
}
