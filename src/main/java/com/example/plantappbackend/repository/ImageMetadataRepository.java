package com.example.plantappbackend.repository;

import com.example.plantappbackend.model.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, Long> {
}