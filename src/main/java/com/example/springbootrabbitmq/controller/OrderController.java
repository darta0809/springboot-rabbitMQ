package com.example.springbootrabbitmq.controller;

import com.example.springbootrabbitmq.consumer.DelayOrderConsumer;
import com.example.springbootrabbitmq.dto.OrderEvent;
import com.example.springbootrabbitmq.enums.RoutingKeyEnum;
import com.example.springbootrabbitmq.producer.OrderEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderEventProducer orderEventProducer;
    private final DelayOrderConsumer delayOrderConsumer;

    /**
     * 場景 1：Fanout 廣播 — 下單後通知所有下游服務
     */
    @PostMapping
    public OrderEvent createOrder(
            @RequestParam String userId,
            @RequestParam String productName,
            @RequestParam Integer quantity,
            @RequestParam BigDecimal amount) {

        OrderEvent orderEvent = buildOrderEvent(userId, productName, quantity, amount);
        orderEventProducer.sendOrderEvent(orderEvent);
        return orderEvent;
    }

    /**
     * 場景 2：Topic 路由 — 模擬訂單建立事件
     */
    @PostMapping("/created")
    public OrderEvent orderCreated(
            @RequestParam String userId,
            @RequestParam String productName,
            @RequestParam Integer quantity,
            @RequestParam BigDecimal amount) {

        OrderEvent orderEvent = buildOrderEvent(userId, productName, quantity, amount);
        orderEventProducer.sendOrderTopicEvent(RoutingKeyEnum.ORDER_CREATED.getCode(), orderEvent);
        return orderEvent;
    }

    /**
     * 場景 2：Topic 路由 — 模擬訂單付款事件
     */
    @PostMapping("/paid")
    public OrderEvent orderPaid(@RequestParam String orderId) {

        OrderEvent orderEvent = OrderEvent.builder()
                .orderId(orderId)
                .createdAt(LocalDateTime.now())
                .build();
        orderEventProducer.sendOrderTopicEvent(RoutingKeyEnum.ORDER_PAID.getCode(), orderEvent);
        return orderEvent;
    }

    /**
     * 場景 2：Topic 路由 — 模擬訂單取消事件
     */
    @PostMapping("/cancelled")
    public OrderEvent orderCancelled(@RequestParam String orderId) {

        OrderEvent orderEvent = OrderEvent.builder()
                .orderId(orderId)
                .createdAt(LocalDateTime.now())
                .build();
        orderEventProducer.sendOrderTopicEvent(RoutingKeyEnum.ORDER_CANCELLED.getCode(), orderEvent);
        return orderEvent;
    }

    /**
     * 場景 4：延遲佇列 — 下單後 10 秒檢查是否已付款，未付款則自動取消
     */
    @PostMapping("/delay")
    public OrderEvent createDelayOrder(
            @RequestParam String userId,
            @RequestParam String productName,
            @RequestParam Integer quantity,
            @RequestParam BigDecimal amount) {

        OrderEvent orderEvent = buildOrderEvent(userId, productName, quantity, amount);
        orderEventProducer.sendDelayOrderEvent(orderEvent);
        return orderEvent;
    }

    /**
     * 場景 4：模擬付款 — 在 10 秒內呼叫此端點，訂單就不會被取消
     */
    @PostMapping("/delay/pay")
    public String payOrder(@RequestParam String orderId) {
        delayOrderConsumer.markAsPaid(orderId);
        return "訂單 " + orderId + " 已付款";
    }

    private OrderEvent buildOrderEvent(String userId, String productName, Integer quantity, BigDecimal amount) {
        return OrderEvent.builder()
                .orderId(UUID.randomUUID().toString().substring(0, 8))
                .userId(userId)
                .productName(productName)
                .quantity(quantity)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
