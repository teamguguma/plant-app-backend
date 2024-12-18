package com.example.plantappbackend.service;

import com.example.plantappbackend.repository.UserRepository;
import com.example.plantappbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUser(String user_uuid) {
        return userRepository.findUserByClientUuid(user_uuid)
                .orElseGet(() -> {
                    User user = new User();
                    user.setClientUuid(user_uuid);
                    return userRepository.save(user);
                });
    }
}