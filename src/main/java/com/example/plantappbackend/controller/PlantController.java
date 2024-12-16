package com.example.plantappbackend.controller;

import com.example.plantappbackend.model.Plant;
import com.example.plantappbackend.service.AwsS3Service;
import com.example.plantappbackend.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    @Autowired
    private PlantService plantService;
    private AwsS3Service awsS3Service;

    // 특정 사용자의 식물 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Plant>> getPlants(@PathVariable Long userId) {
        return ResponseEntity.ok(plantService.getPlantsByUser(userId));
    }

    // 식물 등록
    @PostMapping("/register")
    public ResponseEntity<?> addPlant(
            @RequestBody Plant plant,
            @RequestPart("image") MultipartFile image // 업로드된 이미지 파일
    ) {
        try {
            // 유효성 검사: user_id가 포함되어 있는지 확인
            if (plant.getUser() == null || plant.getUser().getId() == 0) {
                return ResponseEntity.badRequest().body("user_id 정보가 필요합니다.");
            }
            // S3에 이미지 업로드
            String imageUrl = awsS3Service.uploadFile(image, plant.getUser().getId(), plant.getId(), true);
            plant.setImageUrl(imageUrl); // Plant 객체에 S3 URL 설정

            // DB에 저장
            Plant savedPlant = plantService.savePlant(plant);
            return ResponseEntity.ok(savedPlant);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("요청 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}