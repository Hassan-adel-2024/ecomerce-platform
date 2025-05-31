package com.ecommerce.dto.cartdto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddToCartRequestDto {
    @Positive(message = "Product ID must be a positive number")
    @NotNull(message = "Product ID cannot be null")
    private Long productId;
    @Positive(message = "Quantity must be a positive number")
    private Long quantity=1L;
}
