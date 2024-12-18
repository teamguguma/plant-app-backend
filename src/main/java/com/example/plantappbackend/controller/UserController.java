package com.example.plantappbackend.controller;

import com.example.plantappbackend.model.User;
import com.example.plantappbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String user_uuid) {
        User user = userService.findUser(user_uuid);
        return ResponseEntity.ok(user);
    }

//    유저 추가
//    로그인
//    유저 탈퇴
//    유저 닉네임 변경
//    유저 환경설정 변경
//    핸드폰 바꾸기
}