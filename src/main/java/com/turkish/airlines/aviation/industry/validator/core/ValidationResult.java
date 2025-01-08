package com.turkish.airlines.aviation.industry.validator.core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ValidationResult {
    private final boolean valid;
    private final List<ValidationError> errors;

    private ValidationResult(boolean valid, List<ValidationError> errors) {
        this.valid = valid;
        this.errors = Collections.unmodifiableList(errors);
    }

    public static ValidationResult valid() {
        return new ValidationResult(true, new ArrayList<>());
    }

    public static ValidationResult invalid(List<ValidationError> errors) {
        return new ValidationResult(false, errors);
    }

    public static ValidationResult invalid(ValidationError error) {
        return new ValidationResult(false, Collections.singletonList(error));
    }

    public ValidationResult merge(ValidationResult other) {
        if (this.valid && other.valid) {
            return ValidationResult.valid();
        }

        List<ValidationError> mergedErrors = new ArrayList<>(this.errors);
        mergedErrors.addAll(other.errors);
        return ValidationResult.invalid(mergedErrors);
    }
} 