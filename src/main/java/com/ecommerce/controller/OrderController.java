package com.ecommerce.controller;

import com.ecommerce.dto.OrderRequestDto;
import com.ecommerce.dto.OrderResponseDto;
import com.ecommerce.dto.OrderStatusUpdateDto;
import com.ecommerce.service.OrderService;
import com.ecommerce.utility.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto request) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        OrderResponseDto order = orderService.createOrderFromCart(customerId, request);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        List<OrderResponseDto> orders = orderService.getAllOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        OrderResponseDto order = orderService.getOrderById(orderId, customerId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateDto statusUpdate) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        OrderResponseDto order = orderService.updateOrderStatus(orderId, customerId, statusUpdate);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long orderId) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        OrderResponseDto order = orderService.cancelOrder(orderId, customerId);
        return ResponseEntity.ok(order);
    }
}

