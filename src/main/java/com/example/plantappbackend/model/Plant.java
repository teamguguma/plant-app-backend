package com.example.plantappbackend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;
    private String nickname;
    private String imageUrl;

    private Integer waterInterval; // 물 주기
    private LocalDate lastWateredDate; // 마지막으로 물 준 날짜
    private String characteristics;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
}