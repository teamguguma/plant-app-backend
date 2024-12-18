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

    /**
     * 닉네임으로 식물 조회
     */
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<List<Plant>> getPlantsByNickname(@PathVariable String nickname) {
        List<Plant> plants = plantService.getPlantsByNickname(nickname);
        return ResponseEntity.ok(plants);
    }

    /**
     * 식물 데이터 업데이트
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlant(
            @PathVariable Long id,
            @RequestBody String updatedNickname
    ) {
        try {
            Plant updatedPlant = plantService.updatePlant(id, updatedNickname);
            return ResponseEntity.ok(updatedPlant);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 식물 데이터 삭제 (ID 기반)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlantById(@PathVariable Long id) {

        try {
            plantService.deletePlantById(id);
            return ResponseEntity.ok("Plant deleted successfully");
        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
            return ResponseEntity.badRequest().body("삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 식물 데이터 삭제 (객체 기반)
     *
     *
     *     @DeleteMapping("/object")
     *     public ResponseEntity<String> deletePlant(@RequestBody Plant plant) {
     *         try {
     *             plantService.deletePlant(plant);
     *             return ResponseEntity.ok("Plant deleted successfully");
     *         } catch (Exception e) {
     *             return ResponseEntity.badRequest().body("삭제 중 오류가 발생했습니다: " + e.getMessage());
     *         }
     *     }
     */

}