package com.example.plantappbackend.repository;

import com.example.plantappbackend.model.WateringRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WateringRecordRepository extends JpaRepository<WateringRecord, Long> {
    List<WateringRecord> findByPlantIdOrderByWateredAtDesc(Long plantId);
}