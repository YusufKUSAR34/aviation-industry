package com.turkish.airlines.aviation.industry.controller;

import com.turkish.airlines.aviation.industry.request.CreateTransportationRequest;
import com.turkish.airlines.aviation.industry.request.UpdateTransportationRequest;
import com.turkish.airlines.aviation.industry.response.TransportationResponse;
import com.turkish.airlines.aviation.industry.service.TransportationService;
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
@RequestMapping("/api/v1/transportations")
@RequiredArgsConstructor
@Tag(name = "Transportation Controller", description = "APIs for managing transportations")
@SecurityRequirement(name = "Bearer Authentication")
public class TransportationController {
    private final TransportationService transportationService;

    @PostMapping
    @Operation(summary = "Create a new transportation")
    public ResponseEntity<TransportationResponse> createTransportation(@Valid @RequestBody CreateTransportationRequest request) {
        return new ResponseEntity<>(transportationService.createTransportation(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a transportation by ID")
    public ResponseEntity<TransportationResponse> getTransportation(@PathVariable("id") Long id) {
        return ResponseEntity.ok(transportationService.getTransportation(id));
    }

    @GetMapping
    @Operation(summary = "Get all transportations")
    public ResponseEntity<List<TransportationResponse>> getAllTransportations() {
        return ResponseEntity.ok(transportationService.getAllTransportations());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a transportation")
    public ResponseEntity<TransportationResponse> updateTransportation(@PathVariable("id") Long id,
                                                                       @Valid @RequestBody UpdateTransportationRequest request) {
        return ResponseEntity.ok(transportationService.updateTransportation(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a transportation")
    public ResponseEntity<Void> deleteTransportation(@PathVariable("id") Long id) {
        transportationService.deleteTransportation(id);
        return ResponseEntity.noContent().build();
    }
} 