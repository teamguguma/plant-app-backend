package com.example.plantappbackend.repository;

import com.example.plantappbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // clientUuid를 기준으로 사용자 조회
    Optional<User> findByClientUuid(String clientUuid);
}