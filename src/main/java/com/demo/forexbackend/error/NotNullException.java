package com.demo.forexbackend.error;

public class NotNullException extends RuntimeException {
    public NotNullException(String message) {
        super(message);
    }
}
