package com.example.plantappbackend.service;
import org.springframework.transaction.annotation.Transactional;

import com.example.plantappbackend.model.User;
import com.example.plantappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUserUuid(String userUuid) {
        return userRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new RuntimeException("User not found with UUID: " + userUuid));
    }

    public User userCreate(String userUuid, String nickname) {
        if (userRepository.findByUserUuid(userUuid).isPresent()) {
            throw new RuntimeException("User already exists with UUID: " + userUuid);
        }

        User user = new User(userUuid, nickname != null ? nickname : "New User");
        return userRepository.save(user);
    }
    @Transactional
    public void deleteUser(String userUuid) {
        if (!userRepository.findByUserUuid(userUuid).isPresent()) {
            throw new RuntimeException("User not found with UUID: " + userUuid);
        }
        userRepository.deleteByUserUuid(userUuid);
    }
}