package com.example.plantappbackend.service;

import com.example.plantappbackend.Repository.WateringRecordRepository;
import com.example.plantappbackend.model.WateringRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WateringRecordService {

    private final WateringRecordRepository wateringRecordRepository;

    public WateringRecordService(WateringRecordRepository wateringRecordRepository) {
        this.wateringRecordRepository = wateringRecordRepository;
    }

    public List<WateringRecord> getWateringRecords(Long plantId) {
        return wateringRecordRepository.findByPlantIdOrderByWateredAtDesc(plantId);
    }

    public WateringRecord saveWateringRecord(WateringRecord record) {
        return wateringRecordRepository.save(record);
    }
}