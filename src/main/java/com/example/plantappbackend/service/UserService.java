package com.example.plantappbackend.service;

import com.example.plantappbackend.repository.UserRepository;
import com.example.plantappbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findOrCreateUser(String client_uuidd) {
        return userRepository.findByClientUuid(client_uuidd)
                .orElseGet(() -> {
                    User user = new User();
                    user.setClientUuid(client_uuidd);
                    return userRepository.save(user);
                });
    }
}