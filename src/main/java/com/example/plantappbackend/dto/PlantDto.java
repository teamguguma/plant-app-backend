package com.example.plantappbackend.dto;

public class PlantDto {

    private String name;      // 식물 이름
    private String nickname;  // 식물 닉네임
    private String imageUrl;  // 이미지 URL

    // 생성자
    public PlantDto(String name, String nickname, String imageUrl) {
        this.name = name;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
