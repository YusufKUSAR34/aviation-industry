package com.turkish.airlines.aviation.industry.validator.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidationError {
    private final String code;
    private final String message;
    private final ValidationSeverity severity;

    public ValidationError(String code, String message) {
        this(code, message, ValidationSeverity.ERROR);
    }

    public enum ValidationSeverity {
        WARNING,
        ERROR
    }
} 