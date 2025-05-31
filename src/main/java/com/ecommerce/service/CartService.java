package com.ecommerce.service;

import com.ecommerce.dto.cartdto.AddToCartRequestDto;
import com.ecommerce.dto.cartdto.CartDto;
import com.ecommerce.entity.AppUser;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.exceptions.InsufficientStockException;
import com.ecommerce.exceptions.ProductNotFoundException;
import com.ecommerce.mapper.CartMapper;
import com.ecommerce.repository.CartItemRepo;
import com.ecommerce.repository.CartRepo;
import com.ecommerce.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {
 private final CartRepo cartRepo;
 private final CartItemRepo cartItemRepo;
 private final ProductRepo productRepo;
 private final AppUserService appUserService;
 private final CartMapper cartMapper;

public CartDto addItemToCart(Long customerId , AddToCartRequestDto request ){
    Product product = productRepo.findById(request.getProductId()).
            orElseThrow(()-> new ProductNotFoundException
                    ("Product not found with ID: " + request.getProductId()));
    if (!isSufficientStock(product, request.getQuantity())) {
        throw new InsufficientStockException(
                String.format("Insufficient stock: only %d items available for product: %s",
                        product.getStock(), product.getProductName()));
    }

    Cart cart = getActiveCartOrCreateNewOne(customerId);

    CartItem cartItem = new CartItem();
    cartItem.setProduct(product);
    cartItem.setQuantity(request.getQuantity());
    cartItem.setUnitPrice(product.getPrice());
    cartItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
    cartItem.setCart(cart);
    cart.getCartItems().add(cartItem);
    cart.setTotalPrice(cart.getTotalPrice() != null ?
            cart.getTotalPrice().add(cartItem.getTotalPrice()) : cartItem.getTotalPrice());
    Cart savedCart = cartRepo.save(cart);
    return cartMapper.toDto(savedCart);




}










    private Cart getActiveCartOrCreateNewOne(Long customerId) {
        // Logic to get or create a cart for the user
        // This could involve checking if a cart already exists for the user
        // If not, create a new cart and return it
        AppUser appUser = appUserService.getUserById(customerId);
        return cartRepo.findByUser_UserIdAndActiveTrue(customerId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setActive(true);
                    newCart.setUser(appUser);
                    // Set other necessary fields for the new cart
                    return cartRepo.save(newCart);
                });
    }

    private boolean isSufficientStock(Product product, Long quantity) {
        // Logic to check if the product has sufficient stock
        return product.getStock()>= quantity;
    }

}
