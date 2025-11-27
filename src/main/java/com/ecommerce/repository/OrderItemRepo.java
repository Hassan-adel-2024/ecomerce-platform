package com.ecommerce.repository;

import com.ecommerce.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItems, Long> {
    List<OrderItems> findByOrder_OrderId(Long orderId);
}

