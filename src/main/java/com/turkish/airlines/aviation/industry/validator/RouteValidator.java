package com.turkish.airlines.aviation.industry.validator;

import com.turkish.airlines.aviation.industry.validator.core.ValidationContext;
import com.turkish.airlines.aviation.industry.validator.core.ValidationResult;

public interface RouteValidator {
    ValidationResult validate(ValidationContext context);
    int getOrder();
    default String getValidatorName() {
        return this.getClass().getSimpleName();
    }
} 