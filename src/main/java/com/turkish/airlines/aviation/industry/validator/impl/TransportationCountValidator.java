package com.turkish.airlines.aviation.industry.validator.impl;


import com.turkish.airlines.aviation.industry.validator.RouteValidator;
import com.turkish.airlines.aviation.industry.validator.core.ValidationContext;
import com.turkish.airlines.aviation.industry.validator.core.ValidationError;
import com.turkish.airlines.aviation.industry.validator.core.ValidationResult;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class TransportationCountValidator implements RouteValidator {
    private static final int MAX_TRANSPORTATION_COUNT = 3;

    @Override
    public ValidationResult validate(ValidationContext context) {
        if (context.getRoute() == null || context.getRoute().size() > MAX_TRANSPORTATION_COUNT) {
            return ValidationResult.invalid(
                new ValidationError(
                    "TRANSPORTATION_COUNT",
                    String.format("Route cannot have more than %d transportations, found: %d",
                        MAX_TRANSPORTATION_COUNT,
                        context.getRoute() != null ? context.getRoute().size() : 0)
                )
            );
        }
        return ValidationResult.valid();
    }

    @Override
    public int getOrder() {
        return 1;
    }
} 