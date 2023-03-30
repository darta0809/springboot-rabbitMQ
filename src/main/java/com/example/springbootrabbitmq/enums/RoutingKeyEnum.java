package com.example.springbootrabbitmq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoutingKeyEnum {

  DIRECT_KEY_1("DIRECT_KEY_1", "direct 測試 routing key 1"),

  TOPIC_KEY_1("*.TOPIC.*", "topic 測試 routing key 1"),
  TOPIC_KEY_2("#.cc", "topic 測試 routing key 2");

  private final String code;
  private final String name;
}
