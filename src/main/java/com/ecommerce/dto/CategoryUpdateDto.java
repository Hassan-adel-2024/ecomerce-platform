package com.ecommerce.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryUpdateDto extends CategoryDto {
    private Long categoryId;



}
