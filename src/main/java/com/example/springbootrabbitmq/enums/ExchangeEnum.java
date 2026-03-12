package com.example.springbootrabbitmq.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExchangeEnum {

  DIRECT_EXCHANGE("DIRECT_EXCHANGE", "直連模式"),
  TOPIC_EXCHANGE("TOPIC_EXCHANGE", "主題模式"),
  FANOUT_EXCHANGE("FANOUT_EXCHANGE", "廣播模式"),

  // 業務場景：訂單事件廣播
  ORDER_FANOUT_EXCHANGE("ORDER_FANOUT_EXCHANGE", "訂單事件廣播"),

  // 業務場景：訂單事件 Topic 路由
  ORDER_TOPIC_EXCHANGE("ORDER_TOPIC_EXCHANGE", "訂單事件主題路由"),

  // 業務場景：Dead Letter Exchange
  ORDER_DLX("ORDER_DLX", "訂單死信交換機"),

  // 業務場景：延遲佇列 — 訂單超時取消用的 DLX
  ORDER_DELAY_DLX("ORDER_DELAY_DLX", "訂單延遲死信交換機");
  private final String code;
  private final String name;
}
