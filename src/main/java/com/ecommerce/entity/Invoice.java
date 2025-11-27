package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;
    
    private String invoiceNumber;
    private LocalDateTime issuedAt;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal subtotal;
    
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status = InvoiceStatus.PENDING;
    
    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "orderId")
    private Orders order;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private AppUser user;
    
    @PrePersist
    protected void onCreate() {
        this.issuedAt = LocalDateTime.now();
        if (this.invoiceNumber == null || this.invoiceNumber.isEmpty()) {
            this.invoiceNumber = generateInvoiceNumber();
        }
    }
    
    private String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis();
    }
}

