package com.example.plantappbackend.controller;

import com.example.plantappbackend.model.Plant;
import com.example.plantappbackend.service.PlantService;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.plantappbackend.dto.PlantDto;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService plantService; // final로 설정하여 불변성 보장

    // 생성자를 통해 의존성 주입
    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    // @Autowired
    //    private PlantService plantService;
    //원래 16줄~21줄 전에 있었던 코드, 필드 주입 방식 -> 생성자 주입 방식 (@Autowired 경고가 뜨길래,,, 불안해서 바꾼 것, 원래대로 돌려도 괜춘)

    // 특정 사용자의 식물 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlantDto>> getPlantDtos(@PathVariable int userId) {
        List<PlantDto> plantDtos = plantService.getPlantDtosByUser(userId);
        return ResponseEntity.ok(plantDtos);
    }

    // 식물 등록
    @PostMapping("/register")
    public ResponseEntity<?> addPlant(@RequestBody Plant plant) {
        try {
            // 유효성 검사: user_id가 포함되어 있는지 확인
            if (plant.getUser() == null || plant.getUser().getId() == 0) {
                return ResponseEntity.badRequest().body("user_id 정보가 필요합니다.");
            }

            Plant savedPlant = plantService.savePlant(plant);
            return ResponseEntity.ok(savedPlant);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("요청 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // Create (삽입) - 간단한 POST 요청으로 데이터 저장
    @PostMapping
    public ResponseEntity<Plant> addPlantBasic(@RequestBody Plant plant) {
        Plant newPlant = plantService.savePlant(plant);
        return ResponseEntity.ok(newPlant); // 저장된 데이터 반환
    }

    // Read (닉네임으로 조회)
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<List<Plant>> getPlantsByNickname(@PathVariable String nickname) {
        List<Plant> plants = plantService.getPlantsByNickname(nickname);
        return ResponseEntity.ok(plants);
    }

    // Update (업데이트)
    @PutMapping("/{id}")
    public ResponseEntity<Plant> updatePlant(@PathVariable int id, @RequestBody String updatedNickname) {
        Plant updatedPlant = plantService.updatePlant(id, updatedNickname);
        return ResponseEntity.ok(updatedPlant); // 업데이트된 데이터 반환
    }

    // Delete (ID로 삭제)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlantById(@PathVariable int id) {
        plantService.deletePlantById(id);
        return ResponseEntity.ok("Plant deleted successfully");
    }

    // Delete (객체로 삭제)
    @DeleteMapping("/object")
    public ResponseEntity<String> deletePlant(@RequestBody Plant plant) {
        plantService.deletePlant(plant);
        return ResponseEntity.ok("Plant deleted successfully");
    }
}