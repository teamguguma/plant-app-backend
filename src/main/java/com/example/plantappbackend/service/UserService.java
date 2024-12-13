package com.example.plantappbackend.service;

import com.example.plantappbackend.Repository.UserRepository;
import com.example.plantappbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findOrCreateUser(String clientUuid) {
        return userRepository.findByClientUuid(clientUuid)
                .orElseGet(() -> {
                    User user = new User();
                    user.setClientUuid(clientUuid);
                    return userRepository.save(user);
                });
    }
}