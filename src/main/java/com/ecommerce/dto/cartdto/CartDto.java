package com.ecommerce.dto.cartdto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class CartDto {
    private Long cartId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal totalPrice;
    private Long userId;
    private List<CartItemDto> cartItems;
}
