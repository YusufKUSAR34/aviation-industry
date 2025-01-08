package com.turkish.airlines.aviation.industry.controller;

import com.turkish.airlines.aviation.industry.request.RouteSearchRequest;
import com.turkish.airlines.aviation.industry.response.RouteResponse;
import com.turkish.airlines.aviation.industry.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
@Tag(name = "Route Controller", description = "APIs for searching and managing routes")
@SecurityRequirement(name = "Bearer Authentication")
public class RouteController {
    
    private final RouteService routeService;

    @PostMapping("/search")
    @Operation(summary = "Search for all possible routes between two locations",
            description = "Finds all valid routes between origin and destination locations, including direct flights " +
                    "and routes with transfers. Each route can include: before flight transfer (optional), " +
                    "main flight (mandatory), and after flight transfer (optional).")
    public ResponseEntity<List<RouteResponse>> searchRoutes(@Valid @RequestBody RouteSearchRequest request) {
        return ResponseEntity.ok(routeService.findRoutes(request));
    }
} 