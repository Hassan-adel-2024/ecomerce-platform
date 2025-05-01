package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal totalPrice;
    // relations
    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "userId")
    private AppUser user;
    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true , mappedBy = "cartItemId")
    private List<CartItem> cartItems;
}
