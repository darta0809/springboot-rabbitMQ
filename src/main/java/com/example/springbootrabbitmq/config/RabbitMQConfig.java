package com.example.springbootrabbitmq.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final ConfirmCallBackHandler confirmCallBackHandler;
    private final ReturnCallBackHandler returnCallBackHandler;

    /**
     * 自訂 RabbitTemplate：設定回調與 JSON 序列化
     * 其餘連線、ACK 模式等設定由 application.properties + auto-configuration 處理
     */
    @Bean
    public RabbitTemplateCustomizer rabbitTemplateCustomizer() {
        return rabbitTemplate -> {
            rabbitTemplate.setConfirmCallback(confirmCallBackHandler);
            rabbitTemplate.setReturnsCallback(returnCallBackHandler);
            rabbitTemplate.setMessageConverter(
                    new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter()));
        };
    }
}
