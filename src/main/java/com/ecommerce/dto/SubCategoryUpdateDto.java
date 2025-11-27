package com.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SubCategoryUpdateDto extends SubCategoryRequestDto {
    @NotNull(message = "SubCategory ID is required")
    @Positive(message = "SubCategory ID must be positive")
    private Long subCategoryId;
}

