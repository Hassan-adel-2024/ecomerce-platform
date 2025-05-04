package com.ecommerce.mapper;

import com.ecommerce.dto.UserProfileDto;
import com.ecommerce.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserProfileMapper {
     @Mapping(target = "userId" , ignore = true)
     @Mapping(target = "email", ignore = true)
     @Mapping(target = "createdAt", ignore = true)
     @Mapping(target = "updatedAt", ignore = true)
     @Mapping(target = "user", ignore = true)
     @Mapping(source = "firstName", target = "firstName")
     @Mapping(source = "lastName", target = "lastName")
     @Mapping(source = "phoneNumber", target = "phoneNumber")
     @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    UserProfile dtoToEntity(UserProfileDto dto);

}
