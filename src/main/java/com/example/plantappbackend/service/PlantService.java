package com.example.plantappbackend.service;

import com.example.plantappbackend.model.Plant;
import com.example.plantappbackend.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantService {

    @Autowired
    private PlantRepository plantRepository;

    public List<Plant> getPlantsByUser(int userId) {
        return plantRepository.findByUserId(userId);
    }

    public Plant savePlant(Plant plant) {
        return plantRepository.save(plant);
    }
}