package com.ecommerce.controller;

import com.ecommerce.dto.ProductRequestDto;
import com.ecommerce.dto.ProductResponseDto;
import com.ecommerce.dto.ProductUpdateDto;
import com.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
//     private final ProductMapper productMapper;

    @PostMapping("/add")
    public ResponseEntity<ProductResponseDto> addProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto savedProduct = productService.addProduct(productRequestDto);

        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/update")
    public ResponseEntity<ProductResponseDto> updateProduct(@Valid @RequestBody ProductUpdateDto productUpdateDto) {
        ProductResponseDto updatedProduct = productService.updateProduct(productUpdateDto);
        return ResponseEntity.ok(updatedProduct);
    }
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        ProductResponseDto productResponseDto = productService.getProductById(productId);
        return ResponseEntity.ok(productResponseDto);
    }
    @GetMapping("/in-stock")
    public ResponseEntity<List<ProductResponseDto>> getAllInStockProducts() {
        return ResponseEntity.ok(productService.getAllInStockProducts());
    }
    @PutMapping("/delete/{productId}")
    public ResponseEntity<String> softDeleteProduct(@PathVariable Long productId) {
        String response = productService.softDeleteProduct(productId);
        return ResponseEntity.ok(response);
    }


}
