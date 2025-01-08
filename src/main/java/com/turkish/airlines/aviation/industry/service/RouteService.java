package com.turkish.airlines.aviation.industry.service;

import com.turkish.airlines.aviation.industry.request.RouteSearchRequest;
import com.turkish.airlines.aviation.industry.response.RouteResponse;

import java.util.List;

public interface RouteService {
    List<RouteResponse> findRoutes(RouteSearchRequest request);
} 