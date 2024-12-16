//package com.example.plantappbackend.controller;
//
//import com.example.plantappbackend.model.ImageMetadata;
//import com.example.plantappbackend.service.AwsS3Service;
//import com.example.plantappbackend.service.ImageMetadataService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDateTime;
//
//@RestController
//@RequestMapping("/api/s3")
//public class AwsS3Controller {
//
//    private final AwsS3Service awsS3Service;
//    private final ImageMetadataService imageMetadataService;
//
//    @Autowired
//    public AwsS3Controller(AwsS3Service awsS3Service, ImageMetadataService imageMetadataService) {
//        this.awsS3Service = awsS3Service;
//        this.imageMetadataService = imageMetadataService;
//    }
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("user_id") int userId,
//            @RequestParam(value = "plant_id", required = false) Integer plantId,
//            @RequestParam("recognition_type") String recognitionType) {
//        try {
//            boolean isNameOnly = "NAME_ONLY".equalsIgnoreCase(recognitionType);
//            String fileUrl = awsS3Service.uploadFile(file, userId, plantId != null ? plantId : 0, isNameOnly);
//
//            // 업로드 기록 저장
//            ImageMetadata uploadRecord = new ImageMetadata();
//            uploadRecord.setUserId(userId);
//            uploadRecord.setPlantId(plantId != null ? plantId : 0); // 식물 ID가 없으면 0으로 설정
//            uploadRecord.setFileUrl(fileUrl);
//            uploadRecord.setUploadedAt(LocalDateTime.now());
//            uploadRecord.setRecognitionType(recognitionType);
//
//            imageMetadataService.saveImageMetadata(uploadRecord);
//
//            return ResponseEntity.ok("파일 업로드 성공: " + fileUrl);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("파일 업로드 실패: " + e.getMessage());
//        }
//    }
//}