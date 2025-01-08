package com.turkish.airlines.aviation.industry.exception;

public class TransportationNotFoundException extends RuntimeException {
    public TransportationNotFoundException(String message) {
        super(message);
    }
} 