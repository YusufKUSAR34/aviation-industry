package com.turkish.airlines.aviation.industry.validator.impl;


import com.turkish.airlines.aviation.industry.enums.TransportationType;
import com.turkish.airlines.aviation.industry.validator.RouteValidator;
import com.turkish.airlines.aviation.industry.validator.core.ValidationContext;
import com.turkish.airlines.aviation.industry.validator.core.ValidationError;
import com.turkish.airlines.aviation.industry.validator.core.ValidationResult;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class FlightRequirementValidator implements RouteValidator {

    @Override
    public ValidationResult validate(ValidationContext context) {
        if (context.getRoute() == null || context.getRoute().isEmpty()) {
            return ValidationResult.valid();
        }

        long flightCount = context.getRoute().stream()
                .filter(t -> t.getTransportationType() == TransportationType.FLIGHT)
                .count();

        if (flightCount != 1) {
            return ValidationResult.invalid(
                new ValidationError(
                    "FLIGHT_REQUIREMENT",
                    String.format("Route must contain exactly one flight, found: %d", flightCount)
                )
            );
        }

        return ValidationResult.valid();
    }

    @Override
    public int getOrder() {
        return 3;
    }
} 