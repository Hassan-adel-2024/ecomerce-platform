package com.ecommerce.dto.cartdto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class CartItemDto {
    private Long cartItemId;
    private BigDecimal unitPrice;
    private Long quantity;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private Long productId;
    private String productName;
    private String productImage;
}
