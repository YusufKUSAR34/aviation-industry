package com.turkish.airlines.aviation.industry.mapper;

import com.turkish.airlines.aviation.industry.model.Transportation;
import com.turkish.airlines.aviation.industry.request.CreateTransportationRequest;
import com.turkish.airlines.aviation.industry.request.UpdateTransportationRequest;
import com.turkish.airlines.aviation.industry.response.TransportationResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Version;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TransportationMapper {
    TransportationMapper INSTANCE = Mappers.getMapper(TransportationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "originLocation", ignore = true)
    @Mapping(target = "destinationLocation", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Transportation mapToTransportationFromCreateRequest(CreateTransportationRequest request);
    
    @Mapping(target = "originLocationId", source = "originLocation.id")
    @Mapping(target = "destinationLocationId", source = "destinationLocation.id")
    @Mapping(target = "originLocationName", source = "originLocation.name")
    @Mapping(target = "destinationLocationName", source = "destinationLocation.name")
    TransportationResponse mapToTransportationResponse(Transportation transportation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "originLocation", ignore = true)
    @Mapping(target = "destinationLocation", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void mapToTransportationFromUpdateRequest(UpdateTransportationRequest request, @MappingTarget Transportation transportation);
} 