package com.ecommerce.mapper;

import com.ecommerce.dto.AddressDto;
import com.ecommerce.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AddressMapper {
    @Mapping(target = "user" , ignore = true)
    @Mapping(target = "addressId" , ignore = true)
    Address dtoToEntity(AddressDto addressDto) ;
}
