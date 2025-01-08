package com.turkish.airlines.aviation.industry.validator.core;

import com.turkish.airlines.aviation.industry.model.Transportation;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ValidationContext {
    private final List<Transportation> route;
    private final Long requestedOriginId;
    private final Long requestedDestinationId;
    private final boolean isDirectFlightRequired;
} 