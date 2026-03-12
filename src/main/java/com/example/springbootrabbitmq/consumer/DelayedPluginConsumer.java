package com.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class DelayedPluginConsumer {

    /**
     * 監聽 Plugin 方式的延遲佇列
     * 訊息在 Exchange 層暫存，到達延遲時間後才被路由到此 Queue
     */
    @RabbitListener(queues = "ORDER_DELAYED_PLUGIN_QUEUE")
    public void process(String content, Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();

        // 取得原始設定的延遲時間（從 header 中讀回）
        Object delay = message.getMessageProperties().getHeader("x-delay");
        log.info("[延遲Plugin] 收到延遲訊息, 原始延遲: {}ms, 內容: {}", delay, content);

        // 實際業務處理（同 TTL 方式：檢查付款狀態等）
        String orderId = extractOrderId(content);
        log.info("[延遲Plugin] 處理訂單 {} 的超時檢查", orderId);

        channel.basicAck(tag, false);
    }

    private String extractOrderId(String content) {
        int start = content.indexOf("\"orderId\"");
        if (start == -1) {
            return "unknown";
        }
        int colonIndex = content.indexOf(":", start);
        int quoteStart = content.indexOf("\"", colonIndex + 1);
        int quoteEnd = content.indexOf("\"", quoteStart + 1);
        if (quoteStart == -1 || quoteEnd == -1) {
            return "unknown";
        }
        return content.substring(quoteStart + 1, quoteEnd);
    }
}
