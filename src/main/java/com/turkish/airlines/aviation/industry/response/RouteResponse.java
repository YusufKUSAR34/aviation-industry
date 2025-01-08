package com.turkish.airlines.aviation.industry.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
    private TransportationResponse beforeFlight;
    private TransportationResponse flight;
    private TransportationResponse afterFlight;
    private int totalStops;
    private int totalDuration;
    private BigDecimal totalPrice;
} 