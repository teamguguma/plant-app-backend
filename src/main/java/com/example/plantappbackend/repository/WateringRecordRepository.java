package com.example.plantappbackend.repository;

import com.example.plantappbackend.model.WateringRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WateringRecordRepository extends JpaRepository<WateringRecord, Long> {
    List<WateringRecord> findByPlantIdOrderByWateredAtDesc(Long plantId);
}