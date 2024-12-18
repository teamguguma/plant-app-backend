package com.example.plantappbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // User와의 관계 (외래키)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "water_interval")
    private Integer waterInterval;

    @Column(name = "last_watered_date")
    private LocalDate lastWateredDate;

    @Column(name = "characteristics")
    private String characteristics;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Transient
    private String userUuid; // 요청 시 사용 (DB에는 저장하지 않음)
}