package com.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class ProductResponseDto {
    private Long productId;
    private String productName;
    private String description;
    private String brand;
    private BigDecimal price;
    private String sku;
    private Long quantity;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
