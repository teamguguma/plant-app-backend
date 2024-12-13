package com.example.plantappbackend.controller;

import com.example.plantappbackend.service.CameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/plants")
public class CameraController {

    private final CameraService cameraService;

    @Autowired
    public CameraController(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    // OpenAI API 식물 이름 인식
    @PostMapping("/recognize")
    public ResponseEntity<String> recognizePlant(@RequestParam("image") MultipartFile image) {
        // Service 호출 후, plantName 문자열을 직접 반환
        String plantName = cameraService.detectPlant(image);
        return ResponseEntity.ok(plantName); // 이름 반환
    }
}