package com.ecommerce.service;

import com.ecommerce.dto.cartdto.AddToCartRequestDto;
import com.ecommerce.dto.cartdto.CartDto;
import com.ecommerce.entity.AppUser;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.exceptions.CartNotFoundException;
import com.ecommerce.exceptions.InsufficientStockException;
import com.ecommerce.exceptions.ProductNotFoundException;
import com.ecommerce.mapper.CartMapper;
import com.ecommerce.repository.CartItemRepo;
import com.ecommerce.repository.CartRepo;
import com.ecommerce.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;
    private final AppUserService appUserService;
    private final CartMapper cartMapper;

    public CartDto addItemToCart(Long customerId, AddToCartRequestDto request) {
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with ID: " + request.getProductId()));

        validateSufficientStock(product, request.getQuantity());

        Cart cart = getActiveCartOrCreateNewOne(customerId);

        if (productExistsInCart(request.getProductId(), cart)) {
            System.out.println("existing product in Cart");
            updateExistingItem(cart, request);
        } else {
            System.out.println("new product in Cart");
            addNewItem(cart, product, request.getQuantity());
        }

        recalculateCartTotal(cart);
        Cart savedCart = cartRepo.save(cart);

        return cartMapper.toDto(savedCart);
    }

    public CartDto getCartByCustomerId(Long customerId) {
        Cart cart = cartRepo.findByUser_UserIdAndActiveTrue(customerId)
                .orElseThrow(() -> new CartNotFoundException(
                        "No active cart found for user with ID: " + customerId));
        return cartMapper.toDto(cart);
    }
    public CartDto deleteCartItem(Long customerId, Long cartItemId) {
        Cart cart = cartRepo.findByUser_UserIdAndActiveTrue(customerId)
                .orElseThrow(() -> new CartNotFoundException(
                        "No active cart found for user with ID: " + customerId));

        CartItem itemToDelete = cart.getCartItems().stream()
                .filter(item -> item.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new CartNotFoundException(
                        "Cart item not found with ID: " + cartItemId));

        cart.getCartItems().remove(itemToDelete);
        recalculateCartTotal(cart);
        Cart savedCart = cartRepo.save(cart);

        return cartMapper.toDto(savedCart);
    }

    public CartDto updateCartItemQuantity(Long customerId, Long cartItemId, Long newQuantity) {
        Cart cart = cartRepo.findByUser_UserIdAndActiveTrue(customerId)
                .orElseThrow(() -> new CartNotFoundException(
                        "No active cart found for user with ID: " + customerId));

        CartItem itemToUpdate = cart.getCartItems().stream()
                .filter(item -> item.getCartItemId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new CartNotFoundException(
                        "Cart item not found with ID: " + cartItemId));

        Product product = itemToUpdate.getProduct();
        validateSufficientStock(product, newQuantity);

        itemToUpdate.setQuantity(newQuantity);
        itemToUpdate.setTotalPrice(itemToUpdate.getUnitPrice().multiply(BigDecimal.valueOf(newQuantity)));

        recalculateCartTotal(cart);
        Cart savedCart = cartRepo.save(cart);

        return cartMapper.toDto(savedCart);
    }

    public CartDto clearCart(Long customerId) {
        Cart cart = cartRepo.findByUser_UserIdAndActiveTrue(customerId)
                .orElseThrow(() -> new CartNotFoundException(
                        "No active cart found for user with ID: " + customerId));

        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        Cart savedCart = cartRepo.save(cart);

        return cartMapper.toDto(savedCart);
    }

    // ---------- Private Helpers ----------

    private void validateSufficientStock(Product product, Long quantity) {
        if (product.getStock() < quantity) {
            throw new InsufficientStockException(String.format(
                    "Insufficient stock: only %d items available for product: %s",
                    product.getStock(), product.getProductName()));
        }
    }

    private Cart getActiveCartOrCreateNewOne(Long customerId) {
        AppUser appUser = appUserService.getUserById(customerId);
        Optional<Cart> exists = cartRepo.findByUser_UserIdAndActiveTrue(customerId);
        if(exists.isPresent()) {
            System.out.println("existing cart");
        }
        else {
            System.out.println("new cart");
        }
        return cartRepo.findByUser_UserIdAndActiveTrue(customerId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(appUser);
                    newCart.setActive(true);
                    return cartRepo.save(newCart);
                });
    }

    private boolean productExistsInCart(Long productId, Cart cart) {
        return cart.getCartItems() != null &&
                cart.getCartItems().stream()
                        .anyMatch(item -> item.getProduct().getProductId().equals(productId));
    }

    private void updateExistingItem(Cart cart, AddToCartRequestDto request) {
        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct().getProductId().equals(request.getProductId())) {
                item.setQuantity(item.getQuantity() + request.getQuantity());
                item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                return;
            }
        }
    }

    private void addNewItem(Cart cart, Product product, Long quantity) {
        CartItem newItem = new CartItem();
        newItem.setProduct(product);
        newItem.setCart(cart);
        newItem.setUnitPrice(product.getPrice());
        newItem.setQuantity(quantity);
        newItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        cart.getCartItems().add(newItem);
    }

    private void recalculateCartTotal(Cart cart) {
        BigDecimal total = cart.getCartItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);
    }
}
