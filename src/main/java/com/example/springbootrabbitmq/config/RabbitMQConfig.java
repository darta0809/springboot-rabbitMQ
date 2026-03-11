package com.example.springbootrabbitmq.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.addresses}")
    private String address;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    private final ConfirmCallBackHandler confirmCallBackHandler;
    private final ReturnCallBackHandler returnCallBackHandler;

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setAddresses(address);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        cachingConnectionFactory.setVirtualHost(virtualHost);
        // 啟用生產者確認（送出訊息後 RabbitMQ 會回報成功/失敗）
        cachingConnectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);
        // 訊息無法路由時會退回
        cachingConnectionFactory.setPublisherReturns(true);
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory rabbitConnectionFactory) {
        return new RabbitAdmin(rabbitConnectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory rabbitConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory);
        // 生產者確認回調
        rabbitTemplate.setConfirmCallback(confirmCallBackHandler);
        // 訊息不可路由時必須退回（不會靜默丟棄）
        rabbitTemplate.setMandatory(true);
        // 退回時的回調
        rabbitTemplate.setReturnsCallback(returnCallBackHandler);
        // 訊息用 JSON 格式序列化
        rabbitTemplate.setMessageConverter(new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter()));
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory rabbitConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(rabbitConnectionFactory);
        // 同時只有 1 個 Consumer 在跑
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        // 手動 ACK，Consumer 必須自己決定訊息是成功還是失敗
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setReceiveTimeout(2000L);
        factory.setFailedDeclarationRetryInterval(3000L);
        return factory;
    }
}
