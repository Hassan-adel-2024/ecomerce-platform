package com.ecommerce.dto;

import com.ecommerce.entity.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long orderId;
    private BigDecimal totalPrice;
    private LocalDateTime orderedAt;
    private Long quantity;
    private OrderStatus orderStatus;
    private Long userId;
    private String userName;
    private AddressDto shippingAddress;
    private List<OrderItemResponseDto> orderItems;
}

