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

    // UUID를 기반으로 사용자 조회
    public User findUser(String userUuid) {
        return userRepository.findByClientUuid(userUuid)
                .orElseThrow(() -> new RuntimeException("User not found with UUID: " + userUuid));
    }

    // 닉네임 변경
    public User updateNickname(String userUuid, String newNickname) {
        User user = findUser(userUuid);
        user.setUsername(newNickname);
        return userRepository.save(user);
    }

    // 유저 탈퇴
    public void deleteUser(String userUuid) {
        User user = findUser(userUuid);
        userRepository.delete(user);
    }

    // 새로운 사용자 생성
    public User userCreate(String userUuid, String nickname) {
        // 이미 존재하는 UUID인지 확인
        if (userRepository.findByClientUuid(userUuid).isPresent()) {
            throw new IllegalArgumentException("User with UUID already exists.");
        }

        // 새로운 사용자 생성
        User newUser = new User();
        newUser.setClientUuid(userUuid);
        newUser.setUsername(nickname != null ? nickname : "New User");
        newUser.setRole("USER");

        return userRepository.save(newUser);
    }

    // UUID를 기반으로 사용자 존재 여부 확인
    public boolean userExists(String clientUuid) {
        return userRepository.findByClientUuid(clientUuid).isPresent();
    }
}