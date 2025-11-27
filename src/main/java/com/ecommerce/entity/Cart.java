package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
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
    private boolean active;
    // relations
    @ManyToOne()
    @JoinColumn(name = "customer_id", referencedColumnName = "userId")
    private AppUser user;
    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true , mappedBy = "cart",fetch = FetchType.EAGER)
    private List<CartItem> cartItems = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", totalPrice=" + totalPrice +
                ", active=" + active +
                ", user=" + (user != null ? user.getUserId() : null) +
                ", cartItemsCount=" + (cartItems != null ? cartItems.size() : 0) +
                '}';
    }
}
