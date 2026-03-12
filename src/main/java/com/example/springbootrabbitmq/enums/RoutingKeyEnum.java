package com.example.springbootrabbitmq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoutingKeyEnum {

  DIRECT_KEY_1("DIRECT_KEY_1", "direct 測試 routing key 1"),

  TOPIC_KEY_1("*.TOPIC.*", "topic 測試 routing key 1"),
  TOPIC_KEY_2("#.cc", "topic 測試 routing key 2"),

  // 業務場景：訂單事件 routing key
  ORDER_CREATED("order.created", "訂單建立"),
  ORDER_PAID("order.paid", "訂單付款"),
  ORDER_CANCELLED("order.cancelled", "訂單取消"),
  ORDER_ALL("order.*", "所有訂單事件（通配符）");

  private final String code;
  private final String name;
}
