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
    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
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