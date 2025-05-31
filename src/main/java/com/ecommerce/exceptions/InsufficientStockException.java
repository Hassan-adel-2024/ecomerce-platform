package com.ecommerce.exceptions;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(){
        super("Insufficient stock available for the requested product.");
    }
    public InsufficientStockException(String message) {
        super(message);
    }
}
