package com.ecommerce.service;

import com.ecommerce.dto.InvoiceResponseDto;
import com.ecommerce.entity.*;
import com.ecommerce.exceptions.OrderNotFoundException;
import com.ecommerce.exceptions.UserNotFoundException;
import com.ecommerce.mapper.InvoiceMapper;
import com.ecommerce.repository.InvoiceRepo;
import com.ecommerce.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final InvoiceRepo invoiceRepo;
    private final OrderRepo orderRepo;
    private final InvoiceMapper invoiceMapper;

    private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); // 10% tax

    public InvoiceResponseDto generateInvoiceForOrder(Long orderId, Long customerId) {
        Orders order = orderRepo.findByOrderIdAndUser_UserId(orderId, customerId)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Order not found with ID: " + orderId));

        // Check if invoice already exists
        return invoiceRepo.findByOrder_OrderId(orderId)
                .map(invoiceMapper::toDto)
                .orElseGet(() -> {
                    // Create new invoice
                    Invoice invoice = new Invoice();
                    invoice.setOrder(order);
                    invoice.setUser(order.getUser());
                    invoice.setSubtotal(order.getTotalPrice());
                    invoice.setTaxAmount(order.getTotalPrice().multiply(TAX_RATE));
                    invoice.setTotalAmount(invoice.getSubtotal().add(invoice.getTaxAmount()));
                    invoice.setStatus(InvoiceStatus.PENDING);

                    Invoice savedInvoice = invoiceRepo.save(invoice);
                    return invoiceMapper.toDto(savedInvoice);
                });
    }

    public InvoiceResponseDto getInvoiceById(Long invoiceId, Long customerId) {
        Invoice invoice = invoiceRepo.findByInvoiceIdAndUser_UserId(invoiceId, customerId)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Invoice not found with ID: " + invoiceId));
        return invoiceMapper.toDto(invoice);
    }

    public List<InvoiceResponseDto> getAllInvoicesByCustomerId(Long customerId) {
        List<Invoice> invoices = invoiceRepo.findByUser_UserIdOrderByIssuedAtDesc(customerId);
        return invoiceMapper.toDtoList(invoices);
    }

    public InvoiceResponseDto getInvoiceByOrderId(Long orderId, Long customerId) {
        Invoice invoice = invoiceRepo.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Invoice not found for order ID: " + orderId));

        // Verify invoice belongs to user
        if (!invoice.getUser().getUserId().equals(customerId)) {
            throw new UserNotFoundException("Invoice does not belong to the user.");
        }

        return invoiceMapper.toDto(invoice);
    }

    public InvoiceResponseDto updateInvoiceStatus(Long invoiceId, Long customerId, InvoiceStatus newStatus) {
        Invoice invoice = invoiceRepo.findByInvoiceIdAndUser_UserId(invoiceId, customerId)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Invoice not found with ID: " + invoiceId));

        invoice.setStatus(newStatus);
        Invoice updatedInvoice = invoiceRepo.save(invoice);
        return invoiceMapper.toDto(updatedInvoice);
    }
}

