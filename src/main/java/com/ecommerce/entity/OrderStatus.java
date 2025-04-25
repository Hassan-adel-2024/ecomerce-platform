package com.ecommerce.entity;

public enum OrderStatus {
    PENDING,      // Order is placed but not yet processed
    CONFIRMED,    // Order is confirmed
    SHIPPED,      // Order has been shipped
    DELIVERED,    // Order has been delivered to the customer
    CANCELED,     // Order has been canceled
    RETURNED      // Order has been returned by the customer

}
