package com.turkish.airlines.aviation.industry.service.impl;

import com.turkish.airlines.aviation.industry.exception.LocationNotFoundException;
import com.turkish.airlines.aviation.industry.exception.LocationOperationException;
import com.turkish.airlines.aviation.industry.mapper.LocationMapper;
import com.turkish.airlines.aviation.industry.model.Location;
import com.turkish.airlines.aviation.industry.repository.LocationRepository;
import com.turkish.airlines.aviation.industry.request.CreateLocationRequest;
import com.turkish.airlines.aviation.industry.request.UpdateLocationRequest;
import com.turkish.airlines.aviation.industry.response.LocationResponse;
import com.turkish.airlines.aviation.industry.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    @Transactional
    public LocationResponse createLocation(CreateLocationRequest request) {
        try {
            log.info("Creating new location with name: {} and type: {}", request.getName(), request.getType());
            Location location = locationMapper.mapToLocationFromCreateRequest(request);
            location = locationRepository.save(location);
            log.debug("Location created with ID: {}", location.getId());
            return locationMapper.mapToLocationResponse(location);
        } catch (Exception e) {
            String message = String.format("Error creating location with name: %s", request.getName());
            log.error(message, e);
            throw new LocationOperationException(message);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public LocationResponse getLocation(Long id) {
        log.debug("Fetching location with ID: {}", id);
        return locationRepository.findById(id)
                .map(locationMapper::mapToLocationResponse)
                .orElseThrow(() -> {
                    String message = String.format("Location not found with ID: %d", id);
                    log.error(message);
                    return new LocationNotFoundException(message);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationResponse> getAllLocations() {
        try {
            log.debug("Fetching all locations");
            List<LocationResponse> locations = locationRepository.findAll().stream()
                    .map(locationMapper::mapToLocationResponse)
                    .collect(Collectors.toList());
            log.debug("Found {} locations", locations.size());
            return locations;
        } catch (Exception e) {
            String message = "Error fetching all locations";
            log.error(message, e);
            throw new LocationOperationException(message);
        }
    }

    @Override
    @Transactional
    public LocationResponse updateLocation(Long id, UpdateLocationRequest request) {
        try {
            log.info("Updating location with ID: {}", id);
            Location location = locationRepository.findById(id)
                    .orElseThrow(() -> {
                        String message = String.format("Location not found with ID: %d", id);
                        log.error(message);
                        return new LocationNotFoundException(message);
                    });
            
            locationMapper.mapToLocationFromUpdateRequest(request, location);
            location = locationRepository.save(location);
            log.debug("Location updated successfully: {}", location.getId());
            return locationMapper.mapToLocationResponse(location);
        } catch (LocationNotFoundException e) {
            throw e;
        } catch (Exception e) {
            String message = String.format("Error updating location with ID: %d", id);
            log.error(message, e);
            throw new LocationOperationException(message);
        }
    }

    @Override
    @Transactional
    public void deleteLocation(Long id) {
        log.info("Deleting location with ID: {}", id);
        if (!locationRepository.existsById(id)) {
            String message = String.format("Location not found with ID: %d", id);
            log.error(message);
            throw new LocationNotFoundException(message);
        }
        try {
            locationRepository.deleteById(id);
            log.debug("Location deleted successfully: {}", id);
        } catch (Exception e) {
            String message = String.format("Error deleting location with ID: %d", id);
            log.error(message, e);
            throw new LocationOperationException(message);
        }
    }
} 