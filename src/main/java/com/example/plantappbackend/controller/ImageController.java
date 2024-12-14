package com.example.plantappbackend.controller;

import com.example.plantappbackend.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageStorageService imageStorageService;

    @Autowired
    public ImageController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            // 이미지 저장 및 URL 반환
            String imageUrl = imageStorageService.saveImage(image);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("이미지 저장 실패: " + e.getMessage());
        }
    }
}