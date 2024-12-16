package com.example.plantappbackend.service;

import com.example.plantappbackend.model.User;
import com.example.plantappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // UUID를 기반으로 사용자 조회 또는 등록
    public User findOrCreateUser(String clientUuid, String nickname) {
        Optional<User> existingUser = userRepository.findByClientUuid(clientUuid);

        if (existingUser.isPresent()) {
            return existingUser.get(); // 기존 사용자 반환
        } else {
            // 새로운 사용자 등록
            User newUser = new User();
            newUser.setClientUuid(clientUuid);
            newUser.setUsername(nickname);
            newUser.setRole("USER");
            return userRepository.save(newUser);
        }
    }

    // UUID를 기반으로 사용자 조회
    public User findUser(String clientUuid) {
        return userRepository.findByClientUuid(clientUuid)
                .orElseThrow(() -> new RuntimeException("User not found with UUID: " + clientUuid));
    }

    // 닉네임 변경
    public User updateNickname(String clientUuid, String newNickname) {
        User user = findUser(clientUuid);
        user.setUsername(newNickname);
        return userRepository.save(user);
    }

    // 유저 탈퇴
    public void deleteUser(String clientUuid) {
        User user = findUser(clientUuid);
        userRepository.delete(user);
    }
}