package com.example.springbootrabbitmq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QueueEnum {

  DIRECT_QUEUE_1("DIRECT_QUEUE_1", "direct 測試 queue 1"),

  TOPIC_QUEUE_1("TOPIC_QUEUE_1", "topic 測試 queue 1"),
  TOPIC_QUEUE_2("TOPIC_QUEUE_2", "topic 測試 queue 2"),

  FANOUT_QUEUE_1("FANOUT_QUEUE_1", "fanout 測試 queue 1"),
  FANOUT_QUEUE_2("FANOUT_QUEUE_2", "fanout 測試 queue 2");

  private final String code;
  private final String name;
}
