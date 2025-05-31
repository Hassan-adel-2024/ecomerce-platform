package com.ecommerce.mapper;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.dto.ProductRequestDto;
import com.ecommerce.dto.ProductResponseDto;
import com.ecommerce.dto.ProductUpdateDto;
import com.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {
    @Mapping(target = "productId", ignore = true)  // Ignored for new entities
    @Mapping(target = "createdAt", ignore = true)  // Will be set in @PrePersist
    @Mapping(target = "updatedAt", ignore = true)  // Will be set in @PreUpdate
    @Mapping(target = "cartItems", ignore = true)  // Not part of DTO
    @Mapping(target = "category", ignore = true)   // You'll handle this in service
    Product toEntity(ProductRequestDto dto);
    ProductResponseDto toResponseDto(Product product);

    @Mapping(target = "productId", ignore = true)  // Ignored for new entities
    @Mapping(target = "createdAt", ignore = true)  // Will be set in @PrePersist
    @Mapping(target = "updatedAt", ignore = true)  // Will be set in @PreUpdate
    @Mapping(target = "cartItems", ignore = true)  // Not part of DTO
    @Mapping(target = "category", ignore = true)   // You'll handle this in service
    Product updateEntityFromDto(ProductUpdateDto dto, @MappingTarget Product entity);




}
