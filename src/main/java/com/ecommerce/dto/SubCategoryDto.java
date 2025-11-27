package com.ecommerce.dto;

import lombok.Data;

@Data
public class SubCategoryDto {
    private Long subCategoryId;
    private String subCategoryName;
    private String description;
    private Long categoryId;
    private String categoryName;
}

