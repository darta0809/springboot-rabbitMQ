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
  FANOUT_QUEUE_2("FANOUT_QUEUE_2", "fanout 測試 queue 2"),

  // 業務場景：訂單事件
  ORDER_INVENTORY_QUEUE("ORDER_INVENTORY_QUEUE", "訂單-庫存服務"),
  ORDER_NOTIFICATION_QUEUE("ORDER_NOTIFICATION_QUEUE", "訂單-通知服務"),
  ORDER_LOG_QUEUE("ORDER_LOG_QUEUE", "訂單-日誌服務"),

  // 業務場景：訂單事件 Topic 路由
  ORDER_ALL_EVENT_QUEUE("ORDER_ALL_EVENT_QUEUE", "訂單-所有事件日誌"),
  ORDER_CREATED_QUEUE("ORDER_CREATED_QUEUE", "訂單-建立事件"),
  ORDER_PAID_QUEUE("ORDER_PAID_QUEUE", "訂單-付款事件"),
  ORDER_CANCELLED_QUEUE("ORDER_CANCELLED_QUEUE", "訂單-取消事件"),

  // 業務場景：Dead Letter Queue
  ORDER_NOTIFICATION_DLQ("ORDER_NOTIFICATION_DLQ", "訂單-通知服務死信佇列"),

  // 業務場景：延遲佇列 — 訂單超時取消
  ORDER_DELAY_QUEUE("ORDER_DELAY_QUEUE", "訂單延遲佇列（TTL，無消費者）"),
  ORDER_DELAY_PROCESS_QUEUE("ORDER_DELAY_PROCESS_QUEUE", "訂單延遲處理佇列（實際消費端）"),

  // 業務場景：延遲佇列 — Plugin 方式
  ORDER_DELAYED_PLUGIN_QUEUE("ORDER_DELAYED_PLUGIN_QUEUE", "訂單延遲佇列（Plugin 方式）");

  private final String code;
  private final String name;
}
