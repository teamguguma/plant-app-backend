package com.example.plantappbackend.repository;

import com.example.plantappbackend.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Integer> {
    List<Plant> findByUserId(Long userId); // 특정 사용자 ID로 식물 목록 조회

    // 특정 닉네임으로 식물 조회 (Room의 @Query 스타일)
    @Query("SELECT p FROM Plant p WHERE p.nickname = :nickname")
    List<Plant> findByNickname(@Param("nickname") String nickname);
}