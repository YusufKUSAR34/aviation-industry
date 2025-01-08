package com.turkish.airlines.aviation.industry.validator.impl;

import com.turkish.airlines.aviation.industry.enums.TransportationType;
import com.turkish.airlines.aviation.industry.model.Transportation;
import com.turkish.airlines.aviation.industry.validator.RouteValidator;
import com.turkish.airlines.aviation.industry.validator.core.ValidationContext;
import com.turkish.airlines.aviation.industry.validator.core.ValidationError;
import com.turkish.airlines.aviation.industry.validator.core.ValidationResult;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class FlightLocationValidator implements RouteValidator {

    @Override
    public ValidationResult validate(ValidationContext context) {
        if (context.getRoute() == null || context.getRoute().isEmpty() || 
            context.getRequestedOriginId() == null || context.getRequestedDestinationId() == null) {
            return ValidationResult.valid();
        }

        Transportation flight = context.getRoute().stream()
                .filter(t -> t.getTransportationType() == TransportationType.FLIGHT)
                .findFirst()
                .orElse(null);

        if (flight == null) {
            return ValidationResult.valid(); // FlightRequirementValidator will handle this case
        }

        if (!flight.getOriginLocation().getId().equals(context.getRequestedOriginId()) ||
            !flight.getDestinationLocation().getId().equals(context.getRequestedDestinationId())) {
            return ValidationResult.invalid(
                new ValidationError(
                    "FLIGHT_LOCATION",
                    String.format("Flight locations do not match requested route. Expected: %d -> %d, Found: %d -> %d",
                        context.getRequestedOriginId(), context.getRequestedDestinationId(),
                        flight.getOriginLocation().getId(), flight.getDestinationLocation().getId())
                )
            );
        }

        return ValidationResult.valid();
    }

    @Override
    public int getOrder() {
        return 4;
    }
} 