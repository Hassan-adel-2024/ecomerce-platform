package com.ecommerce.mapper;

import com.ecommerce.dto.OrderResponseDto;
import com.ecommerce.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, AddressMapper.class})
public interface OrderMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.email", target = "userName") // using email as username
    OrderResponseDto toDto(Orders order);

    List<OrderResponseDto> toDtoList(List<Orders> orders);
}
