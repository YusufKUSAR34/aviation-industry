package com.turkish.airlines.aviation.industry.validator.impl;


import com.turkish.airlines.aviation.industry.model.Transportation;
import com.turkish.airlines.aviation.industry.validator.RouteValidator;
import com.turkish.airlines.aviation.industry.validator.core.ValidationContext;
import com.turkish.airlines.aviation.industry.validator.core.ValidationError;
import com.turkish.airlines.aviation.industry.validator.core.ValidationResult;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(2)
public class ConnectionValidator implements RouteValidator {

    @Override
    public ValidationResult validate(ValidationContext context) {
        if (context.getRoute() == null || context.getRoute().size() <= 1) {
            return ValidationResult.valid();
        }

        List<ValidationError> errors = new ArrayList<>();
        
        for (int i = 0; i < context.getRoute().size() - 1; i++) {
            Transportation current = context.getRoute().get(i);
            Transportation next = context.getRoute().get(i + 1);

            if (!current.getDestinationLocation().getId().equals(next.getOriginLocation().getId())) {
                errors.add(new ValidationError(
                    "INVALID_CONNECTION",
                    String.format("Invalid connection between transportations at positions %d and %d: " +
                        "destination location %d does not match origin location %d",
                        i, i + 1,
                        current.getDestinationLocation().getId(),
                        next.getOriginLocation().getId())
                ));
            }
        }

        return errors.isEmpty() ? ValidationResult.valid() : ValidationResult.invalid(errors);
    }

    @Override
    public int getOrder() {
        return 2;
    }
} 