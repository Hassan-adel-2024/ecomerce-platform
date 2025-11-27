package com.ecommerce.dto;

import com.ecommerce.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusUpdateDto {
    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus;
}

