package com.ecommerce.service;

import com.ecommerce.dto.OrderRequestDto;
import com.ecommerce.dto.OrderResponseDto;
import com.ecommerce.dto.OrderStatusUpdateDto;
import com.ecommerce.entity.*;
import com.ecommerce.exceptions.CartNotFoundException;
import com.ecommerce.exceptions.InvalidOrderStatusException;
import com.ecommerce.exceptions.OrderNotFoundException;
import com.ecommerce.exceptions.ProductNotFoundException;
import com.ecommerce.mapper.OrderMapper;
import com.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;
    private final AddressRepo addressRepo;
    private final AppUserService appUserService;
    private final OrderMapper orderMapper;

    public OrderResponseDto createOrderFromCart(Long customerId, OrderRequestDto request) {
        // Get active cart
        Cart cart = cartRepo.findByUser_UserIdAndActiveTrue(customerId)
                .orElseThrow(() -> new CartNotFoundException(
                        "No active cart found. Please add items to your cart first."));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new CartNotFoundException("Cart is empty. Cannot create an order.");
        }

        // Validate shipping address
        Address shippingAddress = addressRepo.findById(request.getAddressId())
                .orElseThrow(() -> new OrderNotFoundException(
                        "Shipping address not found with ID: " + request.getAddressId()));

        // Verify address belongs to user
        if (!shippingAddress.getUser().getUserId().equals(customerId)) {
            throw new InvalidOrderStatusException("Shipping address does not belong to the user.");
        }

        // Create order
        Orders order = new Orders();
        order.setUser(appUserService.getUserById(customerId));
        order.setShippingAddress(shippingAddress);
        order.setOrderedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderItems(new ArrayList<>());

        // Convert cart items to order items and validate stock
        List<OrderItems> orderItems = new ArrayList<>();
        Long totalQuantity = 0L;

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = productRepo.findById(cartItem.getProduct().getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(
                            "Product not found: " + cartItem.getProduct().getProductId()));

            // Final stock validation before creating order
            if (product.getStock() < cartItem.getQuantity()) {
                throw new ProductNotFoundException(
                        String.format("Insufficient stock for product %s. Available: %d, Requested: %d",
                                product.getProductName(), product.getStock(), cartItem.getQuantity()));
            }

            // Create order item
            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setTotalPrice(cartItem.getTotalPrice());

            orderItems.add(orderItem);
            totalQuantity += cartItem.getQuantity();

            // Update product stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepo.save(product);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(cart.getTotalPrice());
        order.setQuantity(totalQuantity);

        // Save order
        Orders savedOrder = orderRepo.save(order);

        // Deactivate cart (clear it)
        cart.setActive(false);
        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepo.save(cart);

        return orderMapper.toDto(savedOrder);
    }

    public OrderResponseDto getOrderById(Long orderId, Long customerId) {
        Orders order = orderRepo.findByOrderIdAndUser_UserId(orderId, customerId)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Order not found with ID: " + orderId));
        return orderMapper.toDto(order);
    }

    public List<OrderResponseDto> getAllOrdersByCustomerId(Long customerId) {
        List<Orders> orders = orderRepo.findByUser_UserIdOrderByOrderedAtDesc(customerId);
        return orderMapper.toDtoList(orders);
    }

    public OrderResponseDto updateOrderStatus(Long orderId, Long customerId, OrderStatusUpdateDto statusUpdate) {
        Orders order = orderRepo.findByOrderIdAndUser_UserId(orderId, customerId)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Order not found with ID: " + orderId));

        OrderStatus newStatus = statusUpdate.getOrderStatus();
        OrderStatus currentStatus = order.getOrderStatus();

        // Validate status transition
        validateStatusTransition(currentStatus, newStatus);

        order.setOrderStatus(newStatus);
        Orders updatedOrder = orderRepo.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    public OrderResponseDto cancelOrder(Long orderId, Long customerId) {
        Orders order = orderRepo.findByOrderIdAndUser_UserId(orderId, customerId)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Order not found with ID: " + orderId));

        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException("Cannot cancel a delivered order.");
        }

        if (order.getOrderStatus() == OrderStatus.CANCELED) {
            throw new InvalidOrderStatusException("Order is already canceled.");
        }

        // Restore product stock if order was confirmed or shipped
        if (order.getOrderStatus() == OrderStatus.CONFIRMED || order.getOrderStatus() == OrderStatus.SHIPPED) {
            for (OrderItems orderItem : order.getOrderItems()) {
                Product product = orderItem.getProduct();
                product.setStock(product.getStock() + orderItem.getQuantity());
                productRepo.save(product);
            }
        }

        order.setOrderStatus(OrderStatus.CANCELED);
        Orders canceledOrder = orderRepo.save(order);

        return orderMapper.toDto(canceledOrder);
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // Allow status updates based on business rules
        if (currentStatus == OrderStatus.CANCELED) {
            throw new InvalidOrderStatusException("Cannot update status of a canceled order.");
        }

        if (currentStatus == OrderStatus.DELIVERED && newStatus != OrderStatus.RETURNED) {
            throw new InvalidOrderStatusException("Delivered orders can only be marked as returned.");
        }

        // Allow direct transitions for admin updates (in production, check user role)
        // For now, allow any valid transition
    }
}

