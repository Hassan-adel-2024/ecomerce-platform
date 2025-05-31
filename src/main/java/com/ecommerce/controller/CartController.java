package com.ecommerce.controller;

import com.ecommerce.dto.cartdto.AddToCartRequestDto;
import com.ecommerce.dto.cartdto.CartDto;
import com.ecommerce.service.CartService;
import com.ecommerce.utility.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody AddToCartRequestDto request) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        CartDto cartDto = cartService.addItemToCart(customerId, request);
        return ResponseEntity.ok(cartDto);
    }
}
