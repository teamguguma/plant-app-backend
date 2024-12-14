package com.example.plantappbackend.controller;

import com.example.plantappbackend.model.Plant;
import com.example.plantappbackend.model.User;
import com.example.plantappbackend.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants/register")
public class PlantController {

    @Autowired
    private PlantService plantService;

    // 특정 사용자의 식물 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Plant>> getPlants(@PathVariable int userId) {
        return ResponseEntity.ok(plantService.getPlantsByUser(userId));
    }

    // 식물 등록
    @PostMapping
    public ResponseEntity<Plant> addPlant(@RequestBody Plant plant) {
        // User 객체가 포함되어 있다고 가정
        User user = new User();
        user.setId(plant.getUser().getId());
        plant.setUser(user);

        Plant savedPlant = plantService.savePlant(plant);
        return ResponseEntity.ok(savedPlant);
    }
}