package com.example.plantappbackend.service;

import com.example.plantappbackend.model.Plant;
import com.example.plantappbackend.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.example.plantappbackend.dto.PlantDto;
import java.util.List;

@Service
public class PlantService {

    @Autowired
    private PlantRepository plantRepository;

    public List<Plant> getPlantsByUser(int userId) {
        return plantRepository.findByUserId(userId);
    }

    // Plant -> PlantDto로 변환하는 메서드
    public List<PlantDto> getPlantDtosByUser(int userId) {
        List<Plant> plants = getPlantsByUser(userId); // 기존 메서드 호출
        return plants.stream()
                .map(plant -> new PlantDto(
                        plant.getName(),
                        plant.getNickname(),
                        plant.getImageUrl()))
                .collect(Collectors.toList());
    }

    // Create (삽입)
    public Plant savePlant(Plant plant) {
        return plantRepository.save(plant); // 저장
    }

    // Read (닉네임으로 조회)
    public List<Plant> getPlantsByNickname(String nickname) {
        return plantRepository.findByNickname(nickname); // 닉네임으로 조회
    }

    // Update (업데이트)
    public Plant updatePlant(int id, String updatedNickname) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found"));
        plant.setNickname(updatedNickname); // 닉네임 업데이트
        return plantRepository.save(plant); // 저장
    }

    // Delete (ID로 삭제)
    public void deletePlantById(int id) {
        plantRepository.deleteById(id); // ID로 삭제
    }

    // Delete (객체로 삭제)
    public void deletePlant(Plant plant) {
        plantRepository.delete(plant); // 객체로 삭제
    }
}