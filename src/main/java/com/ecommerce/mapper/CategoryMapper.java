package com.ecommerce.mapper;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CategoryMapper {
    @Mapping(target = "categoryId" , ignore = true)
    @Mapping(target = "categoryName" , source = "categoryName")
    @Mapping(target = "description" , source = "description")
    Category dtoToEntity(CategoryDto dto);
}
