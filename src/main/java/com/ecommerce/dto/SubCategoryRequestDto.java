package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubCategoryRequestDto {
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "SubCategory name is required")
    private String subCategoryName;

    private String description;
}

