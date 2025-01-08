package com.turkish.airlines.aviation.industry.service;

import com.turkish.airlines.aviation.industry.request.CreateTransportationRequest;
import com.turkish.airlines.aviation.industry.request.UpdateTransportationRequest;
import com.turkish.airlines.aviation.industry.response.TransportationResponse;

import java.util.List;

public interface TransportationService {
    TransportationResponse createTransportation(CreateTransportationRequest request);

    TransportationResponse getTransportation(Long id);

    List<TransportationResponse> getAllTransportations();

    TransportationResponse updateTransportation(Long id, UpdateTransportationRequest request);

    void deleteTransportation(Long id);
} 