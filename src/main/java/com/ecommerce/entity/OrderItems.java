package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;
    private Long quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    // relations
    @ManyToOne()
    @JoinColumn(name = "order_id" , referencedColumnName = "orderId")
    private Orders order;
    @ManyToOne
    @JoinColumn(name = "product_id" , referencedColumnName = "productId")
    private Product product;
}
