package com.example.plantappbackend.controller;

import com.example.plantappbackend.model.WateringRecord;
import com.example.plantappbackend.service.WateringRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watering")
public class WateringRecordController {

    @Autowired
    private WateringRecordService wateringRecordService;

    @PostMapping
    public ResponseEntity<WateringRecord> addWateringRecord(@RequestBody WateringRecord record) {
        return ResponseEntity.ok(wateringRecordService.saveWateringRecord(record));
    }

    @GetMapping("/{plantId}")
    public ResponseEntity<List<WateringRecord>> getRecords(@PathVariable int plantId) {
        return ResponseEntity.ok(wateringRecordService.getWateringRecords(plantId));
    }
}