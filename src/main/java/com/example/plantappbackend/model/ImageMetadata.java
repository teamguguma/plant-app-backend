package com.example.plantappbackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "image_metadata")
public class ImageMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "PLANT_ID", nullable = false) // Plant 테이블의 외래 키
    private Plant planft;
    pp

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "recognition_type", nullable = false)
    private RecognitionType recognitionType; // Enum 타입으로 변경

    // Enum 정의: NAME_ONLY 또는 NAME_AND_STATUS 값 사용
    public enum RecognitionType {
        NAME_ONLY,
        NAME_AND_STATUS
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public userId getUser() {
        return user;
    }

    public void userId(userId user) {
        this.user = user;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public RecognitionType getRecognitionType() {
        return recognitionType;
    }

    public void setRecognitionType(RecognitionType recognitionType) {
        this.recognitionType = recognitionType;
    }
}