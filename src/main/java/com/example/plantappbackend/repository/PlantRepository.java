package com.example.plantappbackend.repository;

import com.example.plantappbackend.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Integer> {
    List<Plant> findByUserId(int userId); // 특정 사용자 ID로 식물 목록 조회
}