package com.example.plantappbackend.controller;

import com.example.plantappbackend.dto.UserCreateRequest;
import com.example.plantappbackend.model.User;
import com.example.plantappbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/read")
    public ResponseEntity<Map<String, Object>> checkUserExists(@RequestParam String userUuid) {
        try {
            User user = userService.findByUserUuid(userUuid);
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("message", "User exists");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>(); // 타입을 일관되게 유지
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /* 유저 생성
    POST /api/plants/create
    Content-Type: application/json
    {
        "name": "Rose",
        "nickname": "Beautiful Flower",
        "userUuid": "user-uuid-1234"
    }
    */
    // 신규 유저 생성
    @PostMapping("/create")
    public ResponseEntity<?> userCreate(@RequestBody UserCreateRequest request) {
        try {
            User user = userService.userCreate(request.getUserUuid(), request.getUsername());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    // 닉네임 변경
//    @PutMapping("/update/nickname")
//    public ResponseEntity<User> updateUserNickname(
//            @RequestParam Long id,
//            @RequestParam String newNickname
//    ) {
//        User updatedUser = userService.updateNicknameById(id, newNickname);
//        return ResponseEntity.ok(updatedUser);
//    }

    // 사용자 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String userUuid) {
        userService.deleteUser(userUuid);
        return ResponseEntity.ok("유저 삭제 완료");
    }
}