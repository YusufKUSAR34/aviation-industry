package com.turkish.airlines.aviation.industry.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportationResponse {
    private Long id;
    private Long originLocationId;
    private Long destinationLocationId;
    private String transportationType;
    private String originLocationName;
    private String destinationLocationName;
} 