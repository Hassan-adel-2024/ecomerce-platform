package com.ecommerce.controller;

import com.ecommerce.dto.cartdto.AddToCartRequestDto;
import com.ecommerce.dto.cartdto.CartDto;
import com.ecommerce.dto.cartdto.UpdateCartItemDto;
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

    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        CartDto cartDto = cartService.getCartByCustomerId(customerId);
        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartDto> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody UpdateCartItemDto updateDto) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        CartDto cartDto = cartService.updateCartItemQuantity(customerId, cartItemId, updateDto.getQuantity());
        return ResponseEntity.ok(cartDto);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartDto> removeCartItem(@PathVariable Long cartItemId) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        CartDto cartDto = cartService.deleteCartItem(customerId, cartItemId);
        return ResponseEntity.ok(cartDto);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CartDto> clearCart() {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        CartDto cartDto = cartService.clearCart(customerId);
        return ResponseEntity.ok(cartDto);
    }
}
