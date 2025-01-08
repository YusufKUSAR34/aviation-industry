package com.turkish.airlines.aviation.industry.mapper;


import com.turkish.airlines.aviation.industry.model.Location;
import com.turkish.airlines.aviation.industry.request.CreateLocationRequest;
import com.turkish.airlines.aviation.industry.request.UpdateLocationRequest;
import com.turkish.airlines.aviation.industry.response.LocationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Location mapToLocationFromCreateRequest(CreateLocationRequest request);
    
    LocationResponse mapToLocationResponse(Location location);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void mapToLocationFromUpdateRequest(UpdateLocationRequest request, @MappingTarget Location location);
}