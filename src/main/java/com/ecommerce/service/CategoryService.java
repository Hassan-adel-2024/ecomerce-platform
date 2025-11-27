package com.ecommerce.service;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.dto.CategoryUpdateDto;
import com.ecommerce.entity.Category;
import com.ecommerce.mapper.CategoryMapper;
import com.ecommerce.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;
    public Category addCategory(CategoryDto categoryDto) {
         Category category = categoryMapper.dtoToEntity(categoryDto);
         boolean AlreadyExists = categoryRepo.existsByCategoryName(categoryDto.getCategoryName());
         if (AlreadyExists) {
             throw new RuntimeException("Category already exists");
         }
         return categoryRepo.save(category);

    }
    public CategoryDto updateCategory(CategoryUpdateDto categoryUpdateDto) {
        Long categoryId = categoryUpdateDto.getCategoryId();
        Category existingCategory = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryMapper.updateEntityFromDto(categoryUpdateDto, existingCategory);
        Category updatedCategory = categoryRepo.save(existingCategory);
        return categoryMapper.entityToDto(updatedCategory);
    }
    
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        return categories.stream()
                .map(categoryMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
