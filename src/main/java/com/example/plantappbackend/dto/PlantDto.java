package com.example.plantappbackend.dto;

public class PlantDto {

    private Long id; // 추가된 필드
    private String name;      // 식물 이름
    private String nickname;  // 식물 닉네임
    private String imageUrl;  // 이미지 URL

    // 생성자
    public PlantDto(Long id, String name, String nickname, String imageUrl) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
