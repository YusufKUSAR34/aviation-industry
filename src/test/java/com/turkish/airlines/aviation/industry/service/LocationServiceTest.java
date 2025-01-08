package com.turkish.airlines.aviation.industry.service;

import com.turkish.airlines.aviation.industry.enums.LocationType;
import com.turkish.airlines.aviation.industry.exception.LocationNotFoundException;
import com.turkish.airlines.aviation.industry.exception.LocationOperationException;
import com.turkish.airlines.aviation.industry.mapper.LocationMapper;
import com.turkish.airlines.aviation.industry.model.Location;
import com.turkish.airlines.aviation.industry.repository.LocationRepository;
import com.turkish.airlines.aviation.industry.request.CreateLocationRequest;
import com.turkish.airlines.aviation.industry.request.UpdateLocationRequest;
import com.turkish.airlines.aviation.industry.response.LocationResponse;
import com.turkish.airlines.aviation.industry.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationServiceImpl locationService;

    private Location location;
    private CreateLocationRequest createLocationRequest;
    private UpdateLocationRequest updateLocationRequest;
    private LocationResponse locationResponse;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setId(1L);
        location.setName("Test Location");

        createLocationRequest = new CreateLocationRequest();
        createLocationRequest.setName("Test Location");
        createLocationRequest.setType(LocationType.AIRPORT);

        updateLocationRequest = new UpdateLocationRequest();
        updateLocationRequest.setName("Updated Location");

        locationResponse = new LocationResponse();
        locationResponse.setId(1L);
        locationResponse.setName("Test Location");
    }

    @Test
    void testCreateLocation_Success() {
        when(locationMapper.mapToLocationFromCreateRequest(createLocationRequest)).thenReturn(location);
        when(locationRepository.save(location)).thenReturn(location);
        when(locationMapper.mapToLocationResponse(location)).thenReturn(locationResponse);

        LocationResponse response = locationService.createLocation(createLocationRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Test Location", response.getName());
        Mockito.verify(locationRepository, Mockito.times(1)).save(location);
    }

    @Test
    void testCreateLocation_Exception() {
        when(locationMapper.mapToLocationFromCreateRequest(createLocationRequest)).thenThrow(new RuntimeException());

        assertThrows(LocationOperationException.class, () -> locationService.createLocation(createLocationRequest));
        Mockito.verify(locationRepository, Mockito.never()).save(any());
    }

    @Test
    void testGetLocation_Success() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(locationMapper.mapToLocationResponse(location)).thenReturn(locationResponse);

        LocationResponse response = locationService.getLocation(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getId());
        Mockito.verify(locationRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testGetLocation_NotFound() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LocationNotFoundException.class, () -> locationService.getLocation(1L));
        Mockito.verify(locationRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testGetAllLocations_Success() {
        when(locationRepository.findAll()).thenReturn(Arrays.asList(location));
        when(locationMapper.mapToLocationResponse(location)).thenReturn(locationResponse);

        List<LocationResponse> responses = locationService.getAllLocations();

        Assertions.assertNotNull(responses);
        Assertions.assertEquals(1, responses.size());
        Mockito.verify(locationRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testUpdateLocation_Success() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        Mockito.doNothing().when(locationMapper).mapToLocationFromUpdateRequest(updateLocationRequest, location);
        when(locationRepository.save(location)).thenReturn(location);
        when(locationMapper.mapToLocationResponse(location)).thenReturn(locationResponse);

        LocationResponse response = locationService.updateLocation(1L, updateLocationRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getId());
        Mockito.verify(locationRepository, Mockito.times(1)).save(location);
    }

    @Test
    void testUpdateLocation_NotFound() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LocationNotFoundException.class, () -> locationService.updateLocation(1L, updateLocationRequest));
        Mockito.verify(locationRepository, Mockito.never()).save(any());
    }

    @Test
    void testDeleteLocation_Success() {
        when(locationRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(locationRepository).deleteById(1L);

        Assertions.assertDoesNotThrow(() -> locationService.deleteLocation(1L));
        Mockito.verify(locationRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testDeleteLocation_NotFound() {
        when(locationRepository.existsById(1L)).thenReturn(false);

        assertThrows(LocationNotFoundException.class, () -> locationService.deleteLocation(1L));
        Mockito.verify(locationRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }
}
