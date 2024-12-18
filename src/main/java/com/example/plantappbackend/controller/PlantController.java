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

    /**
     * 특정 사용자의 식물 목록 조회
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Plant>> getPlantsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(plantService.getPlantsByUser(userId));
    }

    /**
     * 새로운 식물 데이터 생성
     */
    @PostMapping("/create")
    public ResponseEntity<?> addPlant(@RequestBody Plant plant) {
        try {
            User user = userService.findByUserUuid(plant.getUserUuid());
            plant.setUser(user);

            Plant savedPlant = plantService.savePlant(plant);
            return ResponseEntity.ok(savedPlant);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * 식물 데이터 삭제
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlantById(@PathVariable Long id) {
        try {
            plantService.deletePlantById(id);
            return ResponseEntity.ok("Plant deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 식물 데이터 업데이트
     */
//    @PutMapping("/update/{id}")
//    public ResponseEntity<?> updatePlant(@PathVariable Long id, @RequestBody String updatedNickname) {
//        try {
//            Plant updatedPlant = plantService.updatePlant(id, updatedNickname);
//            return ResponseEntity.ok(updatedPlant);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("업데이트 중 오류가 발생했습니다: " + e.getMessage());
//        }
//    }
}