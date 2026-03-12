package com.example.springbootrabbitmq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent implements Serializable {

    private String orderId;
    private String userId;
    private String productName;
    private Integer quantity;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
