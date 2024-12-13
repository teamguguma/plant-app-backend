package com.example.plantappbackend.service;

import com.example.plantappbackend.Repository.PlantRepository;
import com.example.plantappbackend.model.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantService {

    @Autowired
    private PlantRepository plantRepository;

    public List<Plant> getPlantsByUser(Long userId) {
        return plantRepository.findByUserId(userId);
    }

    public Plant savePlant(Plant plant) {
        return plantRepository.save(plant);
    }
}