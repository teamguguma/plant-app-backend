package com.example.plantappbackend.service;

import com.example.plantappbackend.model.ImageMetadata;
import com.example.plantappbackend.repository.ImageMetadataRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageMetadataService {

    private final ImageMetadataRepository imageMetadataRepository;

    public ImageMetadataService(ImageMetadataRepository imageMetadataRepository) {
        this.imageMetadataRepository = imageMetadataRepository;
    }

    public void saveImageMetadata(ImageMetadata uploadRecord) {
        imageMetadataRepository.save(uploadRecord);
    }
}