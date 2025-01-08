package com.turkish.airlines.aviation.industry.repository;

import com.turkish.airlines.aviation.industry.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
} 