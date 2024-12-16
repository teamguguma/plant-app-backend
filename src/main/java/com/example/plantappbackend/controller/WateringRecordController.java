package com.example.plantappbackend.controller;

import com.example.plantappbackend.model.WateringRecord;
import com.example.plantappbackend.service.WateringRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watering")
public class WateringRecordController {
//    물 준 기록 불러오기
//    마지막 물 준 기록
//    물 줘야하는 시기 알람
    private final WateringRecordService wateringRecordService;

    public WateringRecordController(WateringRecordService wateringRecordService) {
        this.wateringRecordService = wateringRecordService;
    }

    @PostMapping
    public ResponseEntity<WateringRecord> addWateringRecord(@RequestBody WateringRecord record) {
        return ResponseEntity.ok(wateringRecordService.saveWateringRecord(record));
    }

    @GetMapping("/{plantId}")
    public ResponseEntity<List<WateringRecord>> getRecords(@PathVariable Long plantId) {
        return ResponseEntity.ok(wateringRecordService.getWateringRecords(plantId));
    }
}