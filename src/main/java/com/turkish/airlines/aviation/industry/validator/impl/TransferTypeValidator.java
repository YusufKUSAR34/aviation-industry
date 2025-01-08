package com.turkish.airlines.aviation.industry.validator.impl;


import com.turkish.airlines.aviation.industry.enums.TransportationType;
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
@Order(5)
public class TransferTypeValidator implements RouteValidator {

    @Override
    public ValidationResult validate(ValidationContext context) {
        if (context.getRoute() == null || context.getRoute().isEmpty()) {
            return ValidationResult.valid();
        }

        List<ValidationError> errors = new ArrayList<>();
        
        // Find flight index
        int flightIndex = -1;
        for (int i = 0; i < context.getRoute().size(); i++) {
            if (context.getRoute().get(i).getTransportationType() == TransportationType.FLIGHT) {
                flightIndex = i;
                break;
            }
        }

        if (flightIndex == -1) {
            return ValidationResult.valid(); // FlightRequirementValidator will handle this case
        }

        // Validate before flight transfers
        int beforeFlightTransfers = 0;
        for (int i = 0; i < flightIndex; i++) {
            Transportation transfer = context.getRoute().get(i);
            if (transfer.getTransportationType() != TransportationType.OTHER) {
                errors.add(new ValidationError(
                    "INVALID_BEFORE_TRANSFER_TYPE",
                    "Only OTHER type transportations are allowed before flight"
                ));
            }
            beforeFlightTransfers++;
        }
        
        if (beforeFlightTransfers > 1) {
            errors.add(new ValidationError(
                "MULTIPLE_BEFORE_TRANSFERS",
                "Multiple before flight transfers are not allowed"
            ));
        }

        // Validate after flight transfers
        int afterFlightTransfers = 0;
        for (int i = flightIndex + 1; i < context.getRoute().size(); i++) {
            Transportation transfer = context.getRoute().get(i);
            if (transfer.getTransportationType() != TransportationType.OTHER) {
                errors.add(new ValidationError(
                    "INVALID_AFTER_TRANSFER_TYPE",
                    "Only OTHER type transportations are allowed after flight"
                ));
            }
            afterFlightTransfers++;
        }
        
        if (afterFlightTransfers > 1) {
            errors.add(new ValidationError(
                "MULTIPLE_AFTER_TRANSFERS",
                "Multiple after flight transfers are not allowed"
            ));
        }

        return errors.isEmpty() ? ValidationResult.valid() : ValidationResult.invalid(errors);
    }

    @Override
    public int getOrder() {
        return 5;
    }
} 