package com.turkish.airlines.aviation.industry.repository;

import com.turkish.airlines.aviation.industry.enums.TransportationType;
import com.turkish.airlines.aviation.industry.model.Location;
import com.turkish.airlines.aviation.industry.model.Transportation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportationRepository extends JpaRepository<Transportation, Long> {

    @Query("SELECT t FROM Transportation t " +
            "WHERE t.originLocation.id = :originId " +
            "OR t.destinationLocation.id = :destinationId")
    List<Transportation> findByOriginLocationIdOrDestinationLocationId(
            @Param("originId") Long originId,
            @Param("destinationId") Long destinationId);
} 