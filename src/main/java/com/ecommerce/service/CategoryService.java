package com.ecommerce.service;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.entity.Category;
import com.ecommerce.mapper.CategoryMapper;
import com.ecommerce.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
