package com.ecommerce.service;

import com.ecommerce.dto.SubCategoryDto;
import com.ecommerce.dto.SubCategoryRequestDto;
import com.ecommerce.dto.SubCategoryUpdateDto;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.SubCategory;
import com.ecommerce.mapper.SubCategoryMapper;
import com.ecommerce.repository.CategoryRepo;
import com.ecommerce.repository.SubCategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SubCategoryService {
    private final SubCategoryRepo subCategoryRepo;
    private final CategoryRepo categoryRepo;
    private final SubCategoryMapper subCategoryMapper;

    public SubCategoryDto addSubCategory(SubCategoryRequestDto subCategoryRequestDto) {
        // Check if category exists
        Category category = categoryRepo.findById(subCategoryRequestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + subCategoryRequestDto.getCategoryId()));

        // Check if subcategory name already exists
        boolean alreadyExists = subCategoryRepo.existsBySubCategoryName(subCategoryRequestDto.getSubCategoryName());
        if (alreadyExists) {
            throw new RuntimeException("SubCategory already exists with name: " + subCategoryRequestDto.getSubCategoryName());
        }

        SubCategory subCategory = subCategoryMapper.requestDtoToEntity(subCategoryRequestDto);
        subCategory.setCategory(category);
        SubCategory savedSubCategory = subCategoryRepo.save(subCategory);
        return subCategoryMapper.entityToDto(savedSubCategory);
    }

    public SubCategoryDto updateSubCategory(SubCategoryUpdateDto subCategoryUpdateDto) {
        Long subCategoryId = subCategoryUpdateDto.getSubCategoryId();
        SubCategory existingSubCategory = subCategoryRepo.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + subCategoryId));

        // If category is being changed, validate the new category exists
        if (!existingSubCategory.getCategory().getCategoryId().equals(subCategoryUpdateDto.getCategoryId())) {
            Category category = categoryRepo.findById(subCategoryUpdateDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + subCategoryUpdateDto.getCategoryId()));
            existingSubCategory.setCategory(category);
        }

        subCategoryMapper.updateEntityFromDto(subCategoryUpdateDto, existingSubCategory);
        SubCategory updatedSubCategory = subCategoryRepo.save(existingSubCategory);
        return subCategoryMapper.entityToDto(updatedSubCategory);
    }

    public List<SubCategoryDto> getAllSubCategories() {
        List<SubCategory> subCategories = subCategoryRepo.findAll();
        return subCategories.stream()
                .map(subCategoryMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public SubCategoryDto getSubCategoryById(Long subCategoryId) {
        SubCategory subCategory = subCategoryRepo.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + subCategoryId));
        return subCategoryMapper.entityToDto(subCategory);
    }

    public List<SubCategoryDto> getSubCategoriesByCategoryId(Long categoryId) {
        List<SubCategory> subCategories = subCategoryRepo.findByCategoryCategoryId(categoryId);
        return subCategories.stream()
                .map(subCategoryMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public String deleteSubCategory(Long subCategoryId) {
        SubCategory subCategory = subCategoryRepo.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found with ID: " + subCategoryId));
        subCategoryRepo.delete(subCategory);
        return "SubCategory deleted successfully";
    }
}

