package com.ecommerce.exceptions;

public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String email) {
        super("email " + email + " already exists");
    }
}
