package com.ecommerce.mapper;
import com.ecommerce.dto.cartdto.CartItemDto;
import com.ecommerce.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.imageUrl", target = "productImage")
    @Mapping(source = "createdAt", target = "createdAt")  // explicitly map createdAt
    CartItemDto toDto(CartItem entity);

    CartItem toEntity(CartItemDto dto);

    List<CartItemDto> toDtoList(List<CartItem> entities);

    List<CartItem> toEntityList(List<CartItemDto> dtos);
}
