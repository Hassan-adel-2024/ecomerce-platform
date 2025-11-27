package com.ecommerce.repository;

import com.ecommerce.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepo extends JpaRepository<Invoice, Long> {
    List<Invoice> findByUser_UserIdOrderByIssuedAtDesc(Long userId);
    Optional<Invoice> findByInvoiceIdAndUser_UserId(Long invoiceId, Long userId);
    Optional<Invoice> findByOrder_OrderId(Long orderId);
}

