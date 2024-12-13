package com.example.plantappbackend.Repository;

import com.example.plantappbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByClientUuid(String clientUuid);
}