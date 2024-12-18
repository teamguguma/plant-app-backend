package com.example.plantappbackend.repository;

import com.example.plantappbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserUuid(String userUuid);
    void deleteByUserUuid(String userUuid);
}