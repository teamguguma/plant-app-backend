package com.example.plantappbackend.service;

import com.example.plantappbackend.model.Plant;
import com.example.plantappbackend.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantService {

    private final PlantRepository plantRepository;

    @Autowired
    private PlantRepository plantRepository;
    public List<Plant> getPlantsByUser(Long userId) {
        return plantRepository.findByUserId(userId);
    }

    // Plant -> PlantDto로 변환하는 메서드
    public List<PlantDto> getPlantDtosByUser(Long userId) {
        List<Plant> plants = getPlantsByUser(userId); // 기존 메서드 호출
        return plants.stream()
                .map(plant -> new PlantDto(
                        plant.getId(), // ID 추가
                        plant.getName(),
                        plant.getNickname(),
                        plant.getImageUrl()))
                .collect(Collectors.toList());

    }

    public Plant savePlant(Plant plant) {
        return plantRepository.save(plant);
    }

    public List<Plant> getPlantsByUser(Long userId) {
        return plantRepository.findByUserId(userId);
    }

    public void deletePlantById(Long plantId) {
        plantRepository.deleteById(plantId);
    }
}