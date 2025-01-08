package com.turkish.airlines.aviation.industry.request;

import com.turkish.airlines.aviation.industry.enums.TransportationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateTransportationRequest {

    @NotNull(message = "Origin location id cannot be null")
    private Long originLocationId;

    @NotNull(message = "Destination location id cannot be null")
    private Long destinationLocationId;

    @NotNull(message = "Transportation type cannot be null")
    private TransportationType transportationType;

    @Positive(message = "Duration must be positive")
    private Double duration;

    @Positive(message = "Price must be positive")
    private BigDecimal price;
} 