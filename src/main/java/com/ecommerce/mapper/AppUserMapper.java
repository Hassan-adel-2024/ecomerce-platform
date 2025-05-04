package com.ecommerce.mapper;

import com.ecommerce.dto.AppUserDtoRequest;
import com.ecommerce.dto.AppUserDtoResponse;
import com.ecommerce.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AppUserMapper {
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "addressList", ignore = true)
    @Mapping(target = "role", ignore = true)
    AppUser dtoToEntity(AppUserDtoRequest dto);

    AppUserDtoResponse entityToResponse(AppUser entity);
}
