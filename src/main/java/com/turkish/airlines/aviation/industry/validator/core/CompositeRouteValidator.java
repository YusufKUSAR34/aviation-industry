package com.turkish.airlines.aviation.industry.validator.core;

import com.turkish.airlines.aviation.industry.validator.RouteValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
public class CompositeRouteValidator implements RouteValidator {
    private final List<RouteValidator> validators;

    public CompositeRouteValidator(List<RouteValidator> validators) {
        this.validators = validators;
    }

    @Override
    public ValidationResult validate(ValidationContext context) {
        log.debug("Starting composite validation with {} validators", validators.size());
        
        return validators.stream()
            .sorted(Comparator.comparingInt(RouteValidator::getOrder))
            .map(validator -> {
                try {
                    log.debug("Running validator: {}", validator.getValidatorName());
                    ValidationResult result = validator.validate(context);
                    if (!result.isValid()) {
                        log.debug("Validator {} found {} errors", 
                            validator.getValidatorName(), 
                            result.getErrors().size());
                    }
                    return result;
                } catch (Exception e) {
                    log.error("Error in validator {}: {}", validator.getValidatorName(), e.getMessage());
                    return ValidationResult.invalid(
                        new ValidationError(
                            "VALIDATOR_ERROR",
                            String.format("Validator %s failed: %s", 
                                validator.getValidatorName(), 
                                e.getMessage())
                        )
                    );
                }
            })
            .reduce(ValidationResult.valid(), ValidationResult::merge);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE; // Composite validator should run last
    }
} 