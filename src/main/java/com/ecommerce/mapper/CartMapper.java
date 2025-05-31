package com.ecommerce.mapper;

import com.ecommerce.dto.cartdto.CartDto;
import com.ecommerce.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {
    @Mapping(source = "user.userId", target = "userId")
    CartDto toDto(Cart cart);

    @Mapping(source = "userId", target = "user.userId")
    Cart toEntity(CartDto cartDto);

    List<CartDto> toDtoList(List<Cart> carts);

    List<Cart> toEntityList(List<CartDto> cartDtos);
}
