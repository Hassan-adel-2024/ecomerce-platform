package com.ecommerce.mapper;

import com.ecommerce.dto.SubCategoryDto;
import com.ecommerce.dto.SubCategoryRequestDto;
import com.ecommerce.dto.SubCategoryUpdateDto;
import com.ecommerce.entity.SubCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SubCategoryMapper {
    @Mapping(target = "subCategoryId", ignore = true)
    @Mapping(target = "category", ignore = true)
    SubCategory requestDtoToEntity(SubCategoryRequestDto dto);
    
    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "categoryName", source = "category.categoryName")
    SubCategoryDto entityToDto(SubCategory subCategory);
    
    @Mapping(target = "subCategoryId", ignore = true)
    @Mapping(target = "category", ignore = true)
    SubCategory updateEntityFromDto(SubCategoryUpdateDto dto, @MappingTarget SubCategory entity);
}

