package com.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DelayOrderConsumer {

    /**
     * 模擬已付款的訂單集合
     * 實際業務中應該查詢資料庫
     */
    private final Set<String> paidOrders = ConcurrentHashMap.newKeySet();

    /**
     * 提供給外部模擬付款用（例如 Controller 呼叫）
     */
    public void markAsPaid(String orderId) {
        paidOrders.add(orderId);
        log.info("[延遲佇列] 訂單 {} 已標記為已付款", orderId);
    }

    /**
     * 監聽延遲處理佇列
     * 訊息從 ORDER_DELAY_QUEUE（TTL 過期）經由 ORDER_DELAY_DLX 轉發而來
     */
    @RabbitListener(queues = "ORDER_DELAY_PROCESS_QUEUE")
    public void process(String content, Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();

        log.info("[延遲佇列] 收到超時檢查訊息: {}", content);

        // 從訊息內容解析 orderId（簡化處理，實際應反序列化為 OrderEvent）
        String orderId = extractOrderId(content);

        if (paidOrders.contains(orderId)) {
            log.info("[延遲佇列] 訂單 {} 已付款，無需取消", orderId);
        } else {
            log.warn("[延遲佇列] 訂單 {} 超時未付款，執行自動取消！", orderId);
            // 實際業務：更新訂單狀態為 CANCELLED、恢復庫存、退回優惠券等
        }

        channel.basicAck(tag, false);
    }

    private String extractOrderId(String content) {
        // 簡易解析 JSON 中的 orderId
        // 實際應使用 Jackson ObjectMapper
        int start = content.indexOf("\"orderId\"");
        if (start == -1) return "unknown";
        int colonIndex = content.indexOf(":", start);
        int quoteStart = content.indexOf("\"", colonIndex + 1);
        int quoteEnd = content.indexOf("\"", quoteStart + 1);
        if (quoteStart == -1 || quoteEnd == -1) return "unknown";
        return content.substring(quoteStart + 1, quoteEnd);
    }
}
