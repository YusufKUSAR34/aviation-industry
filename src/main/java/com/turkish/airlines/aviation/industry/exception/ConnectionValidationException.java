package com.turkish.airlines.aviation.industry.exception;

public class ConnectionValidationException extends RuntimeException {
    public ConnectionValidationException(String message) {
        super(message);
    }
} 