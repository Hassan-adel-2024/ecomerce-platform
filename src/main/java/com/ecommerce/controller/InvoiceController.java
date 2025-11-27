package com.ecommerce.controller;

import com.ecommerce.dto.InvoiceResponseDto;
import com.ecommerce.entity.InvoiceStatus;
import com.ecommerce.service.InvoiceService;
import com.ecommerce.utility.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @PostMapping("/generate/{orderId}")
    public ResponseEntity<InvoiceResponseDto> generateInvoice(@PathVariable Long orderId) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        InvoiceResponseDto invoice = invoiceService.generateInvoiceForOrder(orderId, customerId);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponseDto>> getAllInvoices() {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        List<InvoiceResponseDto> invoices = invoiceService.getAllInvoicesByCustomerId(customerId);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceById(@PathVariable Long invoiceId) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        InvoiceResponseDto invoice = invoiceService.getInvoiceById(invoiceId, customerId);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceByOrderId(@PathVariable Long orderId) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        InvoiceResponseDto invoice = invoiceService.getInvoiceByOrderId(orderId, customerId);
        return ResponseEntity.ok(invoice);
    }

    @PutMapping("/{invoiceId}/status")
    public ResponseEntity<InvoiceResponseDto> updateInvoiceStatus(
            @PathVariable Long invoiceId,
            @RequestParam InvoiceStatus status) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        InvoiceResponseDto invoice = invoiceService.updateInvoiceStatus(invoiceId, customerId, status);
        return ResponseEntity.ok(invoice);
    }
}

