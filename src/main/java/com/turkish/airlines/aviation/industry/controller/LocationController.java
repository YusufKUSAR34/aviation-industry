package com.turkish.airlines.aviation.industry.controller;

import com.turkish.airlines.aviation.industry.request.CreateLocationRequest;
import com.turkish.airlines.aviation.industry.request.UpdateLocationRequest;
import com.turkish.airlines.aviation.industry.response.LocationResponse;
import com.turkish.airlines.aviation.industry.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Tag(name = "Location Controller", description = "APIs for managing locations")
@SecurityRequirement(name = "Bearer Authentication")
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    @Operation(summary = "Create a new location")
    public ResponseEntity<LocationResponse> createLocation(@Valid @RequestBody CreateLocationRequest request) {
        return new ResponseEntity<>(locationService.createLocation(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a location by ID")
    public ResponseEntity<LocationResponse> getLocation(@PathVariable("id") Long id) {
        return ResponseEntity.ok(locationService.getLocation(id));
    }

    @GetMapping
    @Operation(summary = "Get all locations")
    public ResponseEntity<List<LocationResponse>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a location")
    public ResponseEntity<LocationResponse> updateLocation(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateLocationRequest request) {
        return ResponseEntity.ok(locationService.updateLocation(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a location")
    public ResponseEntity<Void> deleteLocation(@PathVariable("id") Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
} 