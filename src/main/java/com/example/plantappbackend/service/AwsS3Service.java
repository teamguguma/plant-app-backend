package com.example.plantappbackend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.plantappbackend.model.ImageMetadata;
import com.example.plantappbackend.model.Plant;
import com.example.plantappbackend.repository.ImageMetadataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AwsS3Service {

    private final AmazonS3 amazonS3;
    private final ImageMetadataRepository imageMetadataRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public AwsS3Service(AmazonS3 amazonS3, ImageMetadataRepository imageMetadataRepository) {
        this.amazonS3 = amazonS3;
        this.imageMetadataRepository = imageMetadataRepository;
    }


    public String uploadFile(MultipartFile file, Long userId, Long plantId, boolean isNameOnly) {
        // MultipartFile을 File 객체로 변환
        File convertedFile = convertMultipartFileToFile(file);
        // 고유한 파일 이름 생성
        String fileName = generateFileName(file.getOriginalFilename());

        try {
            // S3에 파일 업로드
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, convertedFile));
            // 업로드된 파일의 URL 생성
            String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();

            // 메타데이터 저장
            saveMetadata(userId, plantId, fileUrl, isNameOnly);

            return fileUrl;
        } finally {
            // 임시 파일 삭제
            if (convertedFile.exists()) {
                boolean deleted = convertedFile.delete();
                if (!deleted) {
                    System.err.println("임시 파일 삭제 실패: " + convertedFile.getAbsolutePath());
                }
            }
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) {
        // 로컬 파일 이름은 원본 파일 이름과 동일하게 설정
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            // 파일 내용을 복사
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("파일 변환 중 오류가 발생했습니다.", e);
        }
        return convertedFile;
    }

    private String generateFileName(String originalFilename) {
        // UUID를 사용하여 고유한 이름 생성
        String uuid = UUID.randomUUID().toString();
        return uuid + "_" + originalFilename.replace(" ", "_");
    }

    private void saveMetadata(Long userId, Long plantId, String fileUrl, boolean isNameOnly) {
        ImageMetadata metadata = new ImageMetadata();

        // Plant 객체 생성 및 설정
        Plant plant = new Plant();
        plant.setId(plantId); // Plant 엔티티 ID 설정

        metadata.setPlant(plant);
        metadata.setFileUrl(fileUrl);
        metadata.setUploadedAt(LocalDateTime.now());
        metadata.setRecognitionType(isNameOnly ? ImageMetadata.RecognitionType.NAME_ONLY : ImageMetadata.RecognitionType.NAME_AND_STATUS);

        imageMetadataRepository.save(metadata);
    }


    public String uploadFileOnlyUrl(MultipartFile file) {
        File convertedFile = convertMultipartFileToFile(file);
        String fileName = generateFileName(file.getOriginalFilename());

        try {
            // S3에 파일 업로드
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, convertedFile));
            // 업로드된 파일의 URL 반환
            return amazonS3.getUrl(bucketName, fileName).toString();
        } finally {
            // 임시 파일 삭제
            if (convertedFile.exists()) {
                boolean deleted = convertedFile.delete();
                if (!deleted) {
                    System.err.println("임시 파일 삭제 실패: " + convertedFile.getAbsolutePath());
                }
            }
        }
    }
}