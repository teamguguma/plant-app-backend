package com.example.plantappbackend.service;

import com.example.plantappbackend.dto.PlantDto;
import com.example.plantappbackend.model.Plant;
import com.example.plantappbackend.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantService {

    private final PlantRepository plantRepository;

    @Autowired
    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    // 특정 사용자의 식물 목록 조회
    public List<Plant> getPlantsByUser(Long userId) {
        return plantRepository.findByUserId(userId);
    }

    // Plant -> PlantDto 변환
    public List<PlantDto> getPlantDtosByUser(Long userId) {
        List<Plant> plants = getPlantsByUser(userId);
        return plants.stream()
                .map(plant -> new PlantDto(
                        plant.getId(),
                        plant.getName(),
                        plant.getNickname(),
                        plant.getImageUrl()))
                .collect(Collectors.toList());
    }

    // 식물 데이터 저장
    public Plant savePlant(Plant plant) {
        return plantRepository.save(plant);
    }

    // 식물 데이터 삭제
    public void deletePlantById(Long plantId) {
        plantRepository.deleteById(plantId);
    }
}