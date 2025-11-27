package com.ecommerce.dto;

import com.ecommerce.entity.InvoiceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InvoiceResponseDto {
    private Long invoiceId;
    private String invoiceNumber;
    private LocalDateTime issuedAt;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal subtotal;
    private InvoiceStatus status;
    private Long orderId;
    private Long userId;
    private String userName;
    private AddressDto billingAddress;
    private OrderResponseDto orderDetails;
}

