package com.ecommerce.service;

import com.ecommerce.dto.ProductRequestDto;
import com.ecommerce.dto.ProductResponseDto;
import com.ecommerce.dto.ProductUpdateDto;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.exceptions.ProductNotFoundException;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.repository.CategoryRepo;
import com.ecommerce.repository.ProductRepo;
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
    private final CategoryRepo categoryRepo;
    private final ProductMapper productMapper;
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        Long categoryId = productRequestDto.getCategoryId();
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = productMapper.toEntity(productRequestDto);
        product.setCategory(category);
        Product savedProduct = productRepo.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    public ProductResponseDto updateProduct(ProductUpdateDto productUpdateDto) {
        Long productId = productUpdateDto.getProductId();
        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
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
