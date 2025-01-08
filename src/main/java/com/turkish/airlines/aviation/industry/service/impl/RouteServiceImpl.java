package com.turkish.airlines.aviation.industry.service.impl;

import com.turkish.airlines.aviation.industry.constants.CacheConstants;
import com.turkish.airlines.aviation.industry.enums.TransportationType;
import com.turkish.airlines.aviation.industry.exception.*;
import com.turkish.airlines.aviation.industry.mapper.TransportationMapper;
import com.turkish.airlines.aviation.industry.model.Transportation;
import com.turkish.airlines.aviation.industry.repository.TransportationRepository;
import com.turkish.airlines.aviation.industry.request.RouteSearchRequest;
import com.turkish.airlines.aviation.industry.response.RouteResponse;
import com.turkish.airlines.aviation.industry.service.RouteService;
import com.turkish.airlines.aviation.industry.validator.core.CompositeRouteValidator;
import com.turkish.airlines.aviation.industry.validator.core.ValidationContext;
import com.turkish.airlines.aviation.industry.validator.core.ValidationError;
import com.turkish.airlines.aviation.industry.validator.core.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final TransportationRepository transportationRepository;
    private final TransportationMapper transportationMapper;
    private final CompositeRouteValidator routeValidator;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CacheConstants.ROUTES,
            key = "{#request?.originLocationId, #request?.destinationLocationId}",
            unless = "#result.isEmpty()")
    public List<RouteResponse> findRoutes(RouteSearchRequest request) {
        log.info("Searching routes from location {} to location {}",
                request.getOriginLocationId(), request.getDestinationLocationId());

        List<Transportation> availableTransportations = findAvailableTransportations(request);
        validateAvailableTransportations(availableTransportations, request);

        ValidationContext baseContext = createBaseValidationContext(request);
        RouteSearchResult searchResult = findValidRoutes(availableTransportations, request, baseContext);

        if (searchResult.getValidRoutes().isEmpty()) {
            throw new RouteNotFoundException(createNotFoundMessage(request, searchResult.getValidationErrors()));
        }

        log.info("Found {} valid routes between locations {} and {}",
                searchResult.getValidRoutes().size(), request.getOriginLocationId(), request.getDestinationLocationId());

        return searchResult.getValidRoutes();
    }

    private ValidationContext createBaseValidationContext(RouteSearchRequest request) {
        return ValidationContext.builder()
                .requestedOriginId(request.getOriginLocationId())
                .requestedDestinationId(request.getDestinationLocationId())
                .build();
    }

    private void validateAvailableTransportations(List<Transportation> transportations, RouteSearchRequest request) {
        if (transportations.isEmpty()) {
            throw new RouteNotFoundException(
                    String.format("No transportations found between locations %d and %d",
                            request.getOriginLocationId(), request.getDestinationLocationId()));
        }
        log.debug("Found {} available transportations", transportations.size());
    }

    private RouteSearchResult findValidRoutes(List<Transportation> transportations,
                                              RouteSearchRequest request, ValidationContext baseContext) {

        List<RouteResponse> validRoutes = new ArrayList<>();
        Map<String, List<String>> validationErrors = new HashMap<>();

        // Generate all possible route combinations
        Stream<List<Transportation>> routeCombinations = generateRouteCombinations(transportations, request);

        // Validate and collect results
        routeCombinations.forEach(route ->
                validateAndAddRoute(route, validRoutes, validationErrors, baseContext));

        return new RouteSearchResult(validRoutes, validationErrors);
    }

    private Stream<List<Transportation>> generateRouteCombinations(
            List<Transportation> transportations, RouteSearchRequest request) {

        List<Transportation> flights = findFlights(transportations);
        List<Transportation> beforeTransfers = findBeforeTransfers(transportations, request.getOriginLocationId());
        List<Transportation> afterTransfers = findAfterTransfers(transportations, request.getDestinationLocationId());

        // Combine all possible route combinations
        return Stream.of(
                // Direct flights
                flights.stream()
                        .filter(t -> isDirectFlight(t, request))
                        .map(Collections::singletonList),

                // Routes with before transfer
                beforeTransfers.stream()
                        .flatMap(beforeTransfer ->
                                flights.stream()
                                        .filter(flight -> isValidBeforeTransferRoute(beforeTransfer, flight, request))
                                        .map(flight -> Arrays.asList(beforeTransfer, flight))),

                // Routes with after transfer
                flights.stream()
                        .flatMap(flight ->
                                afterTransfers.stream()
                                        .filter(afterTransfer -> isValidAfterTransferRoute(flight, afterTransfer, request))
                                        .map(afterTransfer -> Arrays.asList(flight, afterTransfer))),

                // Routes with both transfers
                beforeTransfers.stream()
                        .flatMap(beforeTransfer ->
                                flights.stream()
                                        .flatMap(flight ->
                                                afterTransfers.stream()
                                                        .filter(afterTransfer -> isValidFullRoute(beforeTransfer, flight, afterTransfer))
                                                        .map(afterTransfer -> Arrays.asList(beforeTransfer, flight, afterTransfer))))
        ).flatMap(stream -> stream);
    }

    private void validateAndAddRoute(List<Transportation> route, List<RouteResponse> validRoutes,
                                     Map<String, List<String>> validationErrors, ValidationContext baseContext) {

        ValidationContext context = ValidationContext.builder()
                .route(route)
                .requestedOriginId(baseContext.getRequestedOriginId())
                .requestedDestinationId(baseContext.getRequestedDestinationId())
                .isDirectFlightRequired(baseContext.isDirectFlightRequired())
                .build();

        ValidationResult result = routeValidator.validate(context);

        if (result.isValid()) {
            addValidRoute(route, validRoutes);
        } else {
            addValidationError(route, result, validationErrors);
        }
    }

    private void addValidRoute(List<Transportation> route, List<RouteResponse> validRoutes) {
        RouteResponse response = createRouteResponse(route);
        if (response != null) {
            validRoutes.add(response);
            log.debug("Added valid route with {} stops, duration: {}, price: {}",
                    response.getTotalStops(), response.getTotalDuration(), response.getTotalPrice());
        }
    }

    private void addValidationError(List<Transportation> route, ValidationResult result,
                                    Map<String, List<String>> validationErrors) {
        String routeDescription = describeRoute(route);
        validationErrors.put(routeDescription,
                result.getErrors().stream()
                        .map(ValidationError::getMessage)
                        .collect(Collectors.toList()));
        log.debug("Route validation failed for {}: {}", routeDescription,
                result.getErrors().stream()
                        .map(ValidationError::getMessage)
                        .collect(Collectors.joining(", ")));
    }

    private String createNotFoundMessage(RouteSearchRequest request, Map<String, List<String>> validationErrors) {
        return String.format("No valid routes found from location %d to location %d. Validation errors: %s",
                request.getOriginLocationId(), request.getDestinationLocationId(),
                formatValidationErrors(validationErrors));
    }

    private List<Transportation> findAvailableTransportations(RouteSearchRequest request) {
        log.debug("Finding available transportations for route from {} to {}",
                request.getOriginLocationId(), request.getDestinationLocationId());

        return transportationRepository.findByOriginLocationIdOrDestinationLocationId(
                request.getOriginLocationId(), request.getDestinationLocationId());
    }

    private List<Transportation> findBeforeTransfers(List<Transportation> transportations, Long originId) {
        return transportations.stream()
                .filter(t -> t.getTransportationType() == TransportationType.OTHER &&
                        t.getOriginLocation().getId().equals(originId))
                .collect(Collectors.toList());
    }

    private List<Transportation> findFlights(List<Transportation> transportations) {
        return transportations.stream()
                .filter(t -> t.getTransportationType() == TransportationType.FLIGHT)
                .collect(Collectors.toList());
    }

    private List<Transportation> findAfterTransfers(List<Transportation> transportations, Long destinationId) {
        return transportations.stream()
                .filter(t -> t.getTransportationType() == TransportationType.OTHER &&
                        t.getDestinationLocation().getId().equals(destinationId))
                .collect(Collectors.toList());
    }

    private boolean isDirectFlight(Transportation transportation, RouteSearchRequest request) {
        return transportation.getTransportationType() == TransportationType.FLIGHT
                && transportation.getOriginLocation().getId().equals(request.getOriginLocationId())
                && transportation.getDestinationLocation().getId().equals(request.getDestinationLocationId());
    }

    private boolean isValidBeforeTransferRoute(Transportation beforeTransfer, Transportation flight, RouteSearchRequest request) {
        return isValidConnection(beforeTransfer, flight)
                && flight.getDestinationLocation().getId().equals(request.getDestinationLocationId());
    }

    private boolean isValidAfterTransferRoute(Transportation flight, Transportation afterTransfer, RouteSearchRequest request) {
        return flight.getOriginLocation().getId().equals(request.getOriginLocationId())
                && isValidConnection(flight, afterTransfer);
    }

    private boolean isValidFullRoute(Transportation beforeTransfer, Transportation flight, Transportation afterTransfer) {
        return isValidConnection(beforeTransfer, flight) && isValidConnection(flight, afterTransfer);
    }

    private boolean isValidConnection(Transportation first, Transportation second) {
        return first.getDestinationLocation().getId().equals(second.getOriginLocation().getId());
    }

    private RouteResponse createRouteResponse(List<Transportation> route) {
        RouteResponse.RouteResponseBuilder builder = RouteResponse.builder()
                .totalStops(route.size() - 1)
                .totalDuration(calculateTotalDuration(route))
                .totalPrice(calculateTotalPrice(route));

        switch (route.size()) {
            case 1:
                return builder.flight(transportationMapper.mapToTransportationResponse(route.get(0))).build();
            case 2:
                if (route.get(0).getTransportationType() == TransportationType.FLIGHT) {
                    return builder.flight(transportationMapper.mapToTransportationResponse(route.get(0)))
                            .afterFlight(transportationMapper.mapToTransportationResponse(route.get(1)))
                            .build();
                } else {
                    return builder.beforeFlight(transportationMapper.mapToTransportationResponse(route.get(0)))
                            .flight(transportationMapper.mapToTransportationResponse(route.get(1)))
                            .build();
                }
            case 3:
                return builder.beforeFlight(transportationMapper.mapToTransportationResponse(route.get(0)))
                        .flight(transportationMapper.mapToTransportationResponse(route.get(1)))
                        .afterFlight(transportationMapper.mapToTransportationResponse(route.get(2)))
                        .build();
            default:
                log.error("Invalid route size: {}", route.size());
                return null;
        }
    }

    private int calculateTotalDuration(List<Transportation> route) {
        return (int) route.stream()
                .mapToDouble(Transportation::getDuration)
                .sum();
    }

    private BigDecimal calculateTotalPrice(List<Transportation> route) {
        return route.stream()
                .map(Transportation::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String formatValidationErrors(Map<String, List<String>> validationErrors) {
        return validationErrors.entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), String.join(", ", entry.getValue())))
                .collect(Collectors.joining("; "));
    }

    private String describeRoute(List<Transportation> route) {
        return route.stream()
                .map(t -> String.format("%s (%d -> %d)",
                        t.getTransportationType(),
                        t.getOriginLocation().getId(),
                        t.getDestinationLocation().getId()))
                .collect(Collectors.joining(" -> "));
    }

    @lombok.Value
    private static class RouteSearchResult {
        List<RouteResponse> validRoutes;
        Map<String, List<String>> validationErrors;
    }

}