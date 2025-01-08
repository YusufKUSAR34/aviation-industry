package com.turkish.airlines.aviation.industry.service.impl;

import com.turkish.airlines.aviation.industry.constants.CacheConstants;
import com.turkish.airlines.aviation.industry.exception.LocationNotFoundException;
import com.turkish.airlines.aviation.industry.exception.TransportationNotFoundException;
import com.turkish.airlines.aviation.industry.exception.TransportationOperationException;
import com.turkish.airlines.aviation.industry.mapper.TransportationMapper;
import com.turkish.airlines.aviation.industry.model.Location;
import com.turkish.airlines.aviation.industry.model.Transportation;
import com.turkish.airlines.aviation.industry.repository.LocationRepository;
import com.turkish.airlines.aviation.industry.repository.TransportationRepository;
import com.turkish.airlines.aviation.industry.request.CreateTransportationRequest;
import com.turkish.airlines.aviation.industry.request.UpdateTransportationRequest;
import com.turkish.airlines.aviation.industry.response.TransportationResponse;
import com.turkish.airlines.aviation.industry.service.TransportationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransportationServiceImpl implements TransportationService {
    private final TransportationRepository transportationRepository;
    private final LocationRepository locationRepository;
    private final TransportationMapper transportationMapper;

    @Override
    @Transactional
    public TransportationResponse createTransportation(CreateTransportationRequest request) {
        try {
            log.info("Creating new transportation from location {} to location {} of type {}",
                    request.getOriginLocationId(), request.getDestinationLocationId(), request.getTransportationType());

            Location originLocation = locationRepository.findById(request.getOriginLocationId())
                    .orElseThrow(() -> {
                        String message = String.format("Origin location not found with ID: %d", request.getOriginLocationId());
                        log.error(message);
                        return new LocationNotFoundException(message);
                    });

            Location destinationLocation = locationRepository.findById(request.getDestinationLocationId())
                    .orElseThrow(() -> {
                        String message = String.format("Destination location not found with ID: %d", request.getDestinationLocationId());
                        log.error(message);
                        return new LocationNotFoundException(message);
                    });

            Transportation transportation = transportationMapper.mapToTransportationFromCreateRequest(request);
            transportation.setOriginLocation(originLocation);
            transportation.setDestinationLocation(destinationLocation);

            transportation = transportationRepository.save(transportation);
            log.debug("Transportation created with ID: {}", transportation.getId());

            return transportationMapper.mapToTransportationResponse(transportation);
        } catch (LocationNotFoundException e) {
            throw e;
        } catch (Exception e) {
            String message = String.format("Error creating transportation from location %d to location %d",
                    request.getOriginLocationId(), request.getDestinationLocationId());
            log.error(message, e);
            throw new TransportationOperationException(message);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.SINGLE_TRANSPORTATION, unless = "#result == null")
    public TransportationResponse getTransportation(Long id) {
        log.debug("Fetching transportation with ID: {}", id);
        return transportationRepository.findById(id)
                .map(transportationMapper::mapToTransportationResponse)
                .orElseThrow(() -> {
                    String message = String.format("Transportation not found with ID: %d", id);
                    log.error(message);
                    return new TransportationNotFoundException(message);
                });
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.ALL_TRANSPORTATIONS, unless = "#result.isEmpty()")
    public List<TransportationResponse> getAllTransportations() {
        try {
            log.debug("Fetching all transportations");
            List<TransportationResponse> transportations = transportationRepository.findAll().stream()
                    .map(transportationMapper::mapToTransportationResponse)
                    .collect(Collectors.toList());
            log.debug("Found {} transportations", transportations.size());
            return transportations;
        } catch (Exception e) {
            String message = "Error fetching all transportations";
            log.error(message, e);
            throw new TransportationOperationException(message);
        }
    }

    @Override
    @Transactional
    public TransportationResponse updateTransportation(Long id, UpdateTransportationRequest request) {
        log.info("Updating transportation with ID: {}", id);
        try {
            Transportation transportation = transportationRepository.findById(id)
                    .orElseThrow(() -> {
                        String message = String.format("Transportation not found with ID: %d", id);
                        log.error(message);
                        return new TransportationNotFoundException(message);
                    });

            Location originLocation = locationRepository.findById(request.getOriginLocationId())
                    .orElseThrow(() -> {
                        String message = String.format("Origin location not found with ID: %d", request.getOriginLocationId());
                        log.error(message);
                        return new LocationNotFoundException(message);
                    });

            Location destinationLocation = locationRepository.findById(request.getDestinationLocationId())
                    .orElseThrow(() -> {
                        String message = String.format("Destination location not found with ID: %d", request.getDestinationLocationId());
                        log.error(message);
                        return new LocationNotFoundException(message);
                    });

            transportation.setOriginLocation(originLocation);
            transportation.setDestinationLocation(destinationLocation);
            transportation.setTransportationType(request.getTransportationType());
            transportation.setDuration(request.getDuration());
            transportation.setPrice(request.getPrice());

            transportation = transportationRepository.save(transportation);
            log.debug("Transportation updated successfully: {}", id);

            return transportationMapper.mapToTransportationResponse(transportation);
        } catch (LocationNotFoundException | TransportationNotFoundException e) {
            throw e;
        } catch (Exception e) {
            String message = String.format("Error updating transportation with ID: %d", id);
            log.error(message, e);
            throw new TransportationOperationException(message);
        }
    }

    @Override
    @Transactional
    public void deleteTransportation(Long id) {
        log.info("Deleting transportation with ID: {}", id);
        Transportation transportation = transportationRepository.findById(id)
                .orElseThrow(() -> {
                    String message = String.format("Transportation not found with ID: %d", id);
                    log.error(message);
                    return new TransportationNotFoundException(message);
                });
        try {
            transportation.setDeleted(true);
            transportationRepository.save(transportation);
            log.debug("Transportation deleted successfully: {}", id);
        } catch (Exception e) {
            String message = String.format("Error deleting transportation with ID: %d", id);
            log.error(message, e);
            throw new TransportationOperationException(message);
        }
    }
} 