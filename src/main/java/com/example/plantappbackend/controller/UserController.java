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
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String client_uuid) {
        User user = userService.findOrCreateUser(client_uuid);
        return ResponseEntity.ok(user);
    }
}