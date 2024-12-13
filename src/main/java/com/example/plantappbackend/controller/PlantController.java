package com.example.plantappbackend.controller;

import com.example.plantappbackend.model.Plant;
import com.example.plantappbackend.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    @Autowired
    private PlantService plantService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Plant>> getPlants(@PathVariable Long userId) {
        return ResponseEntity.ok(plantService.getPlantsByUser(userId));
    }

    @PostMapping
    public ResponseEntity<Plant> addPlant(@RequestBody Plant plant) {
        return ResponseEntity.ok(plantService.savePlant(plant));
    }
}