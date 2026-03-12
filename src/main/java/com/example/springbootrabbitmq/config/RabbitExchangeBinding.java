package com.example.springbootrabbitmq.config;

import com.example.springbootrabbitmq.enums.ExchangeEnum;
import com.example.springbootrabbitmq.enums.QueueEnum;
import com.example.springbootrabbitmq.enums.RoutingKeyEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
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
}
