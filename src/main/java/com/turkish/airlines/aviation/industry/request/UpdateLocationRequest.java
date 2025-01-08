package com.turkish.airlines.aviation.industry.request;

import com.turkish.airlines.aviation.industry.enums.LocationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLocationRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Type is required")
    private LocationType type;

    private Double latitude;
    private Double longitude;
    private String city;
    private String country;
} 