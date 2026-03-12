package com.example.springbootrabbitmq.producer;

import com.example.springbootrabbitmq.dto.OrderEvent;
import com.example.springbootrabbitmq.enums.ExchangeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 場景 1：Fanout 廣播 — 所有下游服務都收到
     */
    public void sendOrderEvent(OrderEvent orderEvent) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("[訂單事件-Fanout] 發送訂單事件, correlationId: [{}], orderId: {}, product: {}, amount: {}",
                correlationData.getId(),
                orderEvent.getOrderId(),
                orderEvent.getProductName(),
                orderEvent.getAmount());

        rabbitTemplate.convertAndSend(
                ExchangeEnum.ORDER_FANOUT_EXCHANGE.getCode(),
                "",
                orderEvent,
                correlationData);
    }

    /**
     * 場景 2：Topic 路由 — 根據 routingKey 決定誰收到
     */
    public void sendOrderTopicEvent(String routingKey, OrderEvent orderEvent) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("[訂單事件-Topic] 發送事件, routingKey: {}, correlationId: [{}], orderId: {}",
                routingKey,
                correlationData.getId(),
                orderEvent.getOrderId());

        rabbitTemplate.convertAndSend(
                ExchangeEnum.ORDER_TOPIC_EXCHANGE.getCode(),
                routingKey,
                orderEvent,
                correlationData);
    }
}
