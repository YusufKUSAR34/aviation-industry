package com.turkish.airlines.aviation.industry.response;

import com.turkish.airlines.aviation.industry.enums.LocationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponse {
    private Long id;
    private String name;
    private LocationType type;
    private Double latitude;
    private Double longitude;
    private String city;
    private String country;
} 