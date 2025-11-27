package com.ecommerce.dto.cartdto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateCartItemDto {
    @Positive(message = "Quantity must be a positive number")
    private Long quantity;
}

