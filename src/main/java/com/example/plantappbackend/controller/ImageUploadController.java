package com.example.plantappbackend.controller;

import com.example.plantappbackend.service.AwsS3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageUploadController {

    private final AwsS3Service awsS3Service;

    public ImageUploadController(AwsS3Service awsS3Service) {
        this.awsS3Service = awsS3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file) {
        try {
            // S3에 파일 업로드 후 URL 반환
            String fileUrl = awsS3Service.uploadFileOnlyUrl(file);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            // 예외 발생 시 INTERNAL_SERVER_ERROR 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 업로드 실패: " + e.getMessage());
        }
    }
}