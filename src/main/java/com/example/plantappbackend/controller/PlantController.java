package com.example.plantappbackend.controller;

import com.example.plantappbackend.dto.PlantDto;
import com.example.plantappbackend.model.Plant;
import com.example.plantappbackend.service.AwsS3Service;
import com.example.plantappbackend.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final AwsS3Service awsS3Service;
    private final PlantService plantService;

    @Autowired
    public PlantController(AwsS3Service awsS3Service, PlantService plantService) {
        this.awsS3Service = awsS3Service;
        this.plantService = plantService;
    }

    /**
     * 특정 사용자의 식물 목록 조회
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlantDto>> getPlantDtos(@PathVariable Long userId) {
        List<PlantDto> plantDtos = plantService.getPlantDtosByUser(userId);
        return ResponseEntity.ok(plantDtos);
    }

    /**
     * 식물 등록
     */
    @PostMapping("/register")
    public ResponseEntity<?> addPlant(
            @RequestBody Plant plant // JSON 데이터를 Plant 객체로 받기
    ) {
        try {
            // 저장 처리
            Plant savedPlant = plantService.savePlant(plant);
            return ResponseEntity.ok(savedPlant);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("요청 처리 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 간단한 식물 데이터 저장
     */
    @PostMapping
    public ResponseEntity<Plant> addPlantBasic(@RequestBody Plant plant) {
        try {
            Plant newPlant = plantService.savePlant(plant);
            return ResponseEntity.ok(newPlant);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

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