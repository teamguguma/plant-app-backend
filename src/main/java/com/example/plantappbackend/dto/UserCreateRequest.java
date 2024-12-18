package com.example.plantappbackend.dto;

public class UserCreateRequest {
    private String userUuid;
    private String username;

    public String getClientUuid() {
        return userUuid;
    }

    public void setClientUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    // Getters and Setters
    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}