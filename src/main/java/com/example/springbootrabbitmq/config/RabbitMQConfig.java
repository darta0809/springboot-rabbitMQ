package com.example.springbootrabbitmq.config;

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
import org.springframework.context.annotation.Scope;

@Configuration
public class RabbitMQConfig {

  @Value("${spring.rabbitmq.addresses}")
  private String address;

  @Value("${spring.rabbitmq.username}")
  private String username;

  @Value("${spring.rabbitmq.password}")
  private String password;

  @Value("${spring.rabbitmq.virtual-host}")
  private String virtualHost;

  @Bean
  public ConnectionFactory rabbitConnectionFactory() {
    CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
    cachingConnectionFactory.setAddresses(address);
    cachingConnectionFactory.setUsername(username);
    cachingConnectionFactory.setPassword(password);
    cachingConnectionFactory.setVirtualHost(virtualHost);
    cachingConnectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);
    cachingConnectionFactory.setPublisherReturns(true);
    return cachingConnectionFactory;
  }

  @Bean
  public RabbitAdmin rabbitAdmin(ConnectionFactory rabbitConnectionFactory) {
    return new RabbitAdmin(rabbitConnectionFactory);
  }

  @Bean
  @Scope("prototype")
  public RabbitTemplate rabbitTemplate() {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory());
    rabbitTemplate.setConfirmCallback(new ConfirmCallBackHandler());
    rabbitTemplate.setMandatory(true);
    rabbitTemplate.setReturnsCallback(new ReturnCallBackHandler());
    rabbitTemplate.setMessageConverter(
        new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter()));
    return rabbitTemplate;
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      ConnectionFactory rabbitConnectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(rabbitConnectionFactory);
    factory.setConcurrentConsumers(1);
    factory.setMaxConcurrentConsumers(1);
    factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    factory.setReceiveTimeout(2000L);
    factory.setFailedDeclarationRetryInterval(3000L);
    return factory;
  }
}
