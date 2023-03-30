package com.example.springbootrabbitmq.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExchangeEnum {

  DIRECT_EXCHANGE("DIRECT_EXCHANGE", "直連模式"),
  TOPIC_EXCHANGE("TOPIC_EXCHANGE", "主題模式"),
  FANOUT_EXCHANGE("FANOUT_EXCHANGE", "廣播模式");

  private final String code;
  private final String name;
}
