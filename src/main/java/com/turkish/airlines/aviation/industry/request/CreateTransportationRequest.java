package com.turkish.airlines.aviation.industry.request;

import com.turkish.airlines.aviation.industry.enums.TransportationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransportationRequest {

    @NotNull(message = "Origin location ID is required")
    private Long originLocationId;
    
    @NotNull(message = "Destination location ID is required")
    private Long destinationLocationId;
    
    @NotNull(message = "Transportation type is required")
    private TransportationType transportationType;

    @Positive(message = "Duration must be positive")
    private Double duration;

    @Positive(message = "Price must be positive")
    private BigDecimal price;
} 