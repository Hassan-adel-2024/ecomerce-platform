package com.ecommerce.service;

import com.ecommerce.dto.ProductRequestDto;
import com.ecommerce.dto.ProductResponseDto;
import com.ecommerce.dto.ProductUpdateDto;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.SubCategory;
import com.ecommerce.exceptions.ProductNotFoundException;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.repository.ProductRepo;
import com.ecommerce.repository.SubCategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepo productRepo;
    private final SubCategoryRepo subCategoryRepo;
    private final ProductMapper productMapper;
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        Long subCategoryId = productRequestDto.getSubCategoryId();
        SubCategory subCategory = subCategoryRepo.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));
        Product product = productMapper.toEntity(productRequestDto);
        product.setSubCategory(subCategory);
        Product savedProduct = productRepo.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    public ProductResponseDto updateProduct(ProductUpdateDto productUpdateDto) {
        Long productId = productUpdateDto.getProductId();
        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // If subcategory is being changed, validate the new subcategory exists
        if (productUpdateDto.getSubCategoryId() != null && 
            (existingProduct.getSubCategory() == null || 
             !existingProduct.getSubCategory().getSubCategoryId().equals(productUpdateDto.getSubCategoryId()))) {
            SubCategory subCategory = subCategoryRepo.findById(productUpdateDto.getSubCategoryId())
                    .orElseThrow(() -> new RuntimeException("SubCategory not found"));
            existingProduct.setSubCategory(subCategory);
        }
        
        productMapper.updateEntityFromDto(productUpdateDto, existingProduct);
        Product updatedProduct = productRepo.save(existingProduct);
        return productMapper.toResponseDto(updatedProduct);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepo.findAll().stream().
                map(productMapper::toResponseDto).
                collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        return productMapper.toResponseDto(product);
    }
    public List<ProductResponseDto> getAllInStockProducts() {
        return productRepo.findAllInStock().stream()
                .map(productMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    public String softDeleteProduct(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        product.setDeleted(true);
        productRepo.save(product);
        return "Product soft deleted successfully";
    }

}
