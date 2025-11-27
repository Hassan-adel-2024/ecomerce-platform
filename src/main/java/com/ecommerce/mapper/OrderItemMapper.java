package com.ecommerce.mapper;

import com.ecommerce.dto.OrderItemResponseDto;
import com.ecommerce.entity.OrderItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.imageUrl", target = "productImage")
    OrderItemResponseDto toDto(OrderItems entity);

    List<OrderItemResponseDto> toDtoList(List<OrderItems> entities);
}

