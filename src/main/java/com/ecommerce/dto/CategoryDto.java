package com.ecommerce.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private Long categoryId;
    private String categoryName;
    private String description;
}
