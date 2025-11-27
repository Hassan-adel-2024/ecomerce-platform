package com.ecommerce.controller;

import com.ecommerce.dto.SubCategoryDto;
import com.ecommerce.dto.SubCategoryRequestDto;
import com.ecommerce.dto.SubCategoryUpdateDto;
import com.ecommerce.service.SubCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subcategory")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    @PostMapping("/add")
    public ResponseEntity<SubCategoryDto> addSubCategory(@Valid @RequestBody SubCategoryRequestDto subCategoryRequestDto) {
        SubCategoryDto savedSubCategory = subCategoryService.addSubCategory(subCategoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubCategory);
    }

    @PutMapping("/update")
    public ResponseEntity<SubCategoryDto> updateSubCategory(@Valid @RequestBody SubCategoryUpdateDto subCategoryUpdateDto) {
        SubCategoryDto updatedSubCategory = subCategoryService.updateSubCategory(subCategoryUpdateDto);
        return ResponseEntity.ok(updatedSubCategory);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubCategoryDto>> getAllSubCategories() {
        List<SubCategoryDto> subCategories = subCategoryService.getAllSubCategories();
        return ResponseEntity.ok(subCategories);
    }

    @GetMapping("/{subCategoryId}")
    public ResponseEntity<SubCategoryDto> getSubCategoryById(@PathVariable Long subCategoryId) {
        SubCategoryDto subCategory = subCategoryService.getSubCategoryById(subCategoryId);
        return ResponseEntity.ok(subCategory);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<SubCategoryDto>> getSubCategoriesByCategoryId(@PathVariable Long categoryId) {
        List<SubCategoryDto> subCategories = subCategoryService.getSubCategoriesByCategoryId(categoryId);
        return ResponseEntity.ok(subCategories);
    }

    @DeleteMapping("/{subCategoryId}")
    public ResponseEntity<String> deleteSubCategory(@PathVariable Long subCategoryId) {
        String response = subCategoryService.deleteSubCategory(subCategoryId);
        return ResponseEntity.ok(response);
    }
}

