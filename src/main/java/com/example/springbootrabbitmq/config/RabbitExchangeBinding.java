package com.example.springbootrabbitmq.config;

import com.example.springbootrabbitmq.enums.ExchangeEnum;
import com.example.springbootrabbitmq.enums.QueueEnum;
import com.example.springbootrabbitmq.enums.RoutingKeyEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;

import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitExchangeBinding {

    // ==================== Exchange ====================

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(ExchangeEnum.DIRECT_EXCHANGE.getCode());
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(ExchangeEnum.TOPIC_EXCHANGE.getCode());
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(ExchangeEnum.FANOUT_EXCHANGE.getCode());
    }

    // ==================== Queue ====================

    @Bean
    Queue directQueue1() {
        return new Queue(QueueEnum.DIRECT_QUEUE_1.getCode());
    }

    @Bean
    Queue topicQueue1() {
        return new Queue(QueueEnum.TOPIC_QUEUE_1.getCode());
    }

    @Bean
    Queue topicQueue2() {
        return new Queue(QueueEnum.TOPIC_QUEUE_2.getCode());
    }

    @Bean
    Queue fanoutQueue1() {
        return new Queue(QueueEnum.FANOUT_QUEUE_1.getCode());
    }

    @Bean
    Queue fanoutQueue2() {
        return new Queue(QueueEnum.FANOUT_QUEUE_2.getCode());
    }

    // ==================== Binding ====================

    @Bean
    Binding bindingDirectQueue1(Queue directQueue1, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue1).to(directExchange)
                .with(RoutingKeyEnum.DIRECT_KEY_1.getCode());
    }

    @Bean
    Binding bindingTopicQueue1(Queue topicQueue1, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue1).to(topicExchange)
                .with(RoutingKeyEnum.TOPIC_KEY_1.getCode());
    }

    @Bean
    Binding bindingTopicQueue2(Queue topicQueue2, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue2).to(topicExchange)
                .with(RoutingKeyEnum.TOPIC_KEY_2.getCode());
    }

    @Bean
    Binding bindingFanoutQueue1(Queue fanoutQueue1, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    @Bean
    Binding bindingFanoutQueue2(Queue fanoutQueue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }

    // ==================== 業務場景：訂單事件廣播 ====================

    @Bean
    FanoutExchange orderFanoutExchange() {
        return new FanoutExchange(ExchangeEnum.ORDER_FANOUT_EXCHANGE.getCode());
    }

    @Bean
    Queue orderInventoryQueue() {
        return new Queue(QueueEnum.ORDER_INVENTORY_QUEUE.getCode());
    }

    @Bean
    Queue orderNotificationQueue() {
        // 訊息被 NACK(requeue=false) 時，自動轉發到 ORDER_DLX
        return QueueBuilder.durable(QueueEnum.ORDER_NOTIFICATION_QUEUE.getCode())
                .deadLetterExchange(ExchangeEnum.ORDER_DLX.getCode())
                .build();
    }

    @Bean
    Queue orderLogQueue() {
        return new Queue(QueueEnum.ORDER_LOG_QUEUE.getCode());
    }

    @Bean
    Binding bindingOrderInventory(Queue orderInventoryQueue, FanoutExchange orderFanoutExchange) {
        return BindingBuilder.bind(orderInventoryQueue).to(orderFanoutExchange);
    }

    @Bean
    Binding bindingOrderNotification(Queue orderNotificationQueue, FanoutExchange orderFanoutExchange) {
        return BindingBuilder.bind(orderNotificationQueue).to(orderFanoutExchange);
    }

    @Bean
    Binding bindingOrderLog(Queue orderLogQueue, FanoutExchange orderFanoutExchange) {
        return BindingBuilder.bind(orderLogQueue).to(orderFanoutExchange);
    }

    // ==================== 業務場景：訂單事件 Topic 路由 ====================

    @Bean
    TopicExchange orderTopicExchange() {
        return new TopicExchange(ExchangeEnum.ORDER_TOPIC_EXCHANGE.getCode());
    }

    @Bean
    Queue orderAllEventQueue() {
        return new Queue(QueueEnum.ORDER_ALL_EVENT_QUEUE.getCode());
    }

    @Bean
    Queue orderCreatedQueue() {
        return new Queue(QueueEnum.ORDER_CREATED_QUEUE.getCode());
    }

    @Bean
    Queue orderPaidQueue() {
        return new Queue(QueueEnum.ORDER_PAID_QUEUE.getCode());
    }

    @Bean
    Queue orderCancelledQueue() {
        return new Queue(QueueEnum.ORDER_CANCELLED_QUEUE.getCode());
    }

    @Bean
    Binding bindingOrderAllEvent(Queue orderAllEventQueue, TopicExchange orderTopicExchange) {
        // order.* 匹配所有 order 開頭的事件
        return BindingBuilder.bind(orderAllEventQueue).to(orderTopicExchange)
                .with(RoutingKeyEnum.ORDER_ALL.getCode());
    }

    @Bean
    Binding bindingOrderCreated(Queue orderCreatedQueue, TopicExchange orderTopicExchange) {
        return BindingBuilder.bind(orderCreatedQueue).to(orderTopicExchange)
                .with(RoutingKeyEnum.ORDER_CREATED.getCode());
    }

    @Bean
    Binding bindingOrderPaid(Queue orderPaidQueue, TopicExchange orderTopicExchange) {
        return BindingBuilder.bind(orderPaidQueue).to(orderTopicExchange)
                .with(RoutingKeyEnum.ORDER_PAID.getCode());
    }

    @Bean
    Binding bindingOrderCancelled(Queue orderCancelledQueue, TopicExchange orderTopicExchange) {
        return BindingBuilder.bind(orderCancelledQueue).to(orderTopicExchange)
                .with(RoutingKeyEnum.ORDER_CANCELLED.getCode());
    }

    // ==================== 業務場景：Dead Letter Exchange + DLQ ====================

    @Bean
    FanoutExchange orderDlx() {
        return new FanoutExchange(ExchangeEnum.ORDER_DLX.getCode());
    }

    @Bean
    Queue orderNotificationDlq() {
        return new Queue(QueueEnum.ORDER_NOTIFICATION_DLQ.getCode());
    }

    @Bean
    Binding bindingOrderNotificationDlq(Queue orderNotificationDlq, FanoutExchange orderDlx) {
        return BindingBuilder.bind(orderNotificationDlq).to(orderDlx);
    }

    // ==================== 業務場景：延遲佇列 — 訂單超時取消 ====================

    /**
     * 延遲用的 DLX（Direct Exchange）
     * 當 ORDER_DELAY_QUEUE 中的訊息 TTL 過期，會被轉發到這個交換機
     */
    @Bean
    DirectExchange orderDelayDlx() {
        return new DirectExchange(ExchangeEnum.ORDER_DELAY_DLX.getCode());
    }

    /**
     * 延遲佇列：設定 TTL = 10 秒，無消費者監聽
     * 訊息過期後自動轉發到 ORDER_DELAY_DLX，routing key 為 "order.delay.process"
     */
    @Bean
    Queue orderDelayQueue() {
        return QueueBuilder.durable(QueueEnum.ORDER_DELAY_QUEUE.getCode())
                .ttl(10_000) // 10 秒
                .deadLetterExchange(ExchangeEnum.ORDER_DELAY_DLX.getCode())
                .deadLetterRoutingKey("order.delay.process")
                .build();
    }

    /**
     * 實際處理佇列：Consumer 監聽這個佇列，處理超時訂單
     */
    @Bean
    Queue orderDelayProcessQueue() {
        return new Queue(QueueEnum.ORDER_DELAY_PROCESS_QUEUE.getCode());
    }

    /**
     * 將處理佇列綁定到延遲 DLX，routing key 對應 deadLetterRoutingKey
     */
    @Bean
    Binding bindingOrderDelayProcess(Queue orderDelayProcessQueue, DirectExchange orderDelayDlx) {
        return BindingBuilder.bind(orderDelayProcessQueue).to(orderDelayDlx)
                .with("order.delay.process");
    }

    // ==================== 業務場景：延遲佇列 — Plugin 方式 ====================

    /**
     * x-delayed-message 類型的 Exchange
     * 訊息會在 Exchange 層暫存，到達延遲時間後才路由到 Queue
     * 內部路由類型設為 direct
     */
    @Bean
    CustomExchange orderDelayedExchange() {
        return new CustomExchange(
                ExchangeEnum.ORDER_DELAYED_EXCHANGE.getCode(),
                "x-delayed-message",    // 插件提供的 Exchange 類型
                true,                    // durable
                false,                   // autoDelete
                Map.of("x-delayed-type", "direct")  // 實際路由方式
        );
    }

    @Bean
    Queue orderDelayedPluginQueue() {
        return new Queue(QueueEnum.ORDER_DELAYED_PLUGIN_QUEUE.getCode());
    }

    @Bean
    Binding bindingOrderDelayedPlugin(Queue orderDelayedPluginQueue, CustomExchange orderDelayedExchange) {
        return BindingBuilder.bind(orderDelayedPluginQueue).to(orderDelayedExchange)
                .with("order.delayed")
                .noargs();
    }
}
