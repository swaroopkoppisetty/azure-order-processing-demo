package com.self.order_service.service;

import com.self.order_service.dto.CreateOrderRequest;
import com.self.order_service.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrder(Long id);
}
