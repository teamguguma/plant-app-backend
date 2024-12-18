package com.example.plantappbackend.controller;

import com.example.plantappbackend.model.Plant;
import com.example.plantappbackend.model.User;
import com.example.plantappbackend.service.PlantService;
import com.example.plantappbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService plantService;
    private final UserService userService;

    @Autowired
    public PlantController(PlantService plantService, UserService userService) {
        this.plantService = plantService;
        this.userService = userService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Plant>> getPlantsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(plantService.getPlantsByUser(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> addPlant(@RequestBody Plant plant) {
        System.out.println("Received Plant Data: " + plant);
        try {
            User user = userService.findByUserUuid(plant.getUserUuid());
            plant.setUser(user);

            Plant savedPlant = plantService.savePlant(plant);
            System.out.println("Saved Plant: " + savedPlant);
            return ResponseEntity.ok(savedPlant);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePlant(@PathVariable Long id) {
        try {
            plantService.deletePlantById(id);
            return ResponseEntity.ok("Plant deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}