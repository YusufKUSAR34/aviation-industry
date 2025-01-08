package com.turkish.airlines.aviation.industry.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteSearchRequest {

    @NotNull(message = "Origin location ID is required")
    private Long originLocationId;
    
    @NotNull(message = "Destination location ID is required")
    private Long destinationLocationId;
} 