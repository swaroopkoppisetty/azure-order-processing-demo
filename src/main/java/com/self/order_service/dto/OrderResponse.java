package com.self.order_service.dto;


import com.self.order_service.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private String customerName;

    private String productName;

    private BigDecimal amount;

    private OrderStatus status;

    private LocalDateTime createdAt;
}
