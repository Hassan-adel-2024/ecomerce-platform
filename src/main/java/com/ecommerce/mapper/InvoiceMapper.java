package com.ecommerce.mapper;

import com.ecommerce.dto.InvoiceResponseDto;
import com.ecommerce.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderMapper.class, AddressMapper.class})
public interface InvoiceMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.email", target = "userName") // use email instead of username
    @Mapping(source = "order.orderId", target = "orderId")
    @Mapping(source = "order.shippingAddress", target = "billingAddress")
    @Mapping(source = "order", target = "orderDetails")
    InvoiceResponseDto toDto(Invoice invoice);

    List<InvoiceResponseDto> toDtoList(List<Invoice> invoices);
}
