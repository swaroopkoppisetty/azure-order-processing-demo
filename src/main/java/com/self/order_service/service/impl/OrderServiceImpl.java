package com.self.order_service.service.impl;


import com.self.order_service.dto.CreateOrderRequest;
import com.self.order_service.dto.OrderResponse;
import com.self.order_service.entity.Order;
import com.self.order_service.entity.OrderStatus;
import com.self.order_service.exception.ResourceNotFoundException;
import com.self.order_service.repository.OrderRepository;
import com.self.order_service.service.OrderEventPublisher;
import com.self.order_service.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        System.out.println("Inside createOrder()");
        log.info("Creating order for customer: {}", request.getCustomerName());

        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .productName(request.getProductName())
                .amount(request.getAmount())
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        orderEventPublisher.publishOrderCreated(
                savedOrder.getId(),
                savedOrder.getCustomerName(),
                savedOrder.getProductName(),
                savedOrder.getAmount().doubleValue()
        );

        return mapToResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with Id: " + id));

        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .productName(order.getProductName())
                .amount(order.getAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
