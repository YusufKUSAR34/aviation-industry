package com.turkish.airlines.aviation.industry.service;


import com.turkish.airlines.aviation.industry.request.CreateLocationRequest;
import com.turkish.airlines.aviation.industry.request.UpdateLocationRequest;
import com.turkish.airlines.aviation.industry.response.LocationResponse;

import java.util.List;

public interface LocationService {
    LocationResponse createLocation(CreateLocationRequest request);
    
    LocationResponse getLocation(Long id);
    
    List<LocationResponse> getAllLocations();
    
    LocationResponse updateLocation(Long id, UpdateLocationRequest request);
    
    void deleteLocation(Long id);
} 