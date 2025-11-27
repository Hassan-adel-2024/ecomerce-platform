package com.ecommerce.repository;

import com.ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser_UserIdAndActiveTrue(Long userId);
    List<Cart> findAllByUser_UserIdAndActiveFalse(Long userId);
}
