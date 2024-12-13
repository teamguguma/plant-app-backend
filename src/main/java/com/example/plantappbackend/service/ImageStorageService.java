package com.example.plantappbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageStorageService {

    private final String IMAGE_STORAGE_DIR = "/var/www/images"; // EC2 서버의 저장 경로
    private final String IMAGE_BASE_URL = "http://<EC2_PUBLIC_IP>:8080/images/"; // EC2 이미지 접근 URL

    public String saveImage(MultipartFile image) throws IOException {
        // 파일명 생성
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

        // 파일 저장 경로
        Path filePath = Paths.get(IMAGE_STORAGE_DIR, fileName);

        // 디렉토리 존재 확인 및 생성
        Files.createDirectories(filePath.getParent());

        // 파일 저장
        Files.write(filePath, image.getBytes());

        // 저장된 이미지의 URL 반환
        return IMAGE_BASE_URL + fileName;
    }
}