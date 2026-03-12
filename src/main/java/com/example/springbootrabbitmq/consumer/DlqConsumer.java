package com.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DlqConsumer {

    @RabbitListener(queues = "ORDER_NOTIFICATION_DLQ")
    public void process(String content, Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();

        log.error("==================== DLQ 收到死信 ====================");
        log.error("[DLQ] 訊息內容: {}", content);

        // 印出 x-death 資訊：死亡原因、原始 Queue、死亡次數
        List<Map<String, Object>> xDeath = getXDeathHeader(message);
        if (xDeath != null) {
            for (Map<String, Object> death : xDeath) {
                log.error("[DLQ] 死亡原因: {}, 原始 Queue: {}, 次數: {}",
                        death.get("reason"),
                        death.get("queue"),
                        death.get("count"));
            }
        }

        log.error("[DLQ] 此訊息需要人工介入處理或發送告警通知");
        log.error("======================================================");

        // DLQ 的訊息確認後就結束了，不再重試
        channel.basicAck(tag, false);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getXDeathHeader(Message message) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();
        if (headers == null) {
            return null;
        }
        return (List<Map<String, Object>>) headers.get("x-death");
    }
}
