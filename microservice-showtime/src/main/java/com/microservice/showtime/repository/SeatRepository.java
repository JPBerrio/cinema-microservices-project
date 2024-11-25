package com.microservice.showtime.repository;

import com.microservice.showtime.model.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
    Optional<SeatEntity> findById(Long id);
}
