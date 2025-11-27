package com.ecommerce.repository;

import com.ecommerce.entity.Orders;
import com.ecommerce.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Orders, Long> {
    List<Orders> findByUser_UserIdOrderByOrderedAtDesc(Long userId);
    Optional<Orders> findByOrderIdAndUser_UserId(Long orderId, Long userId);
    List<Orders> findByUser_UserIdAndOrderStatus(Long userId, OrderStatus status);
}

