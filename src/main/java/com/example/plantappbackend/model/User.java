package com.example.plantappbackend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "user") // 테이블 이름 지정
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false) // 사용자 이름
    private String username;

    @Column(name = "client_uuid", unique = true, nullable = false) // 고유 UUID
    private String clientUuid;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Plant> plants;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientUuid() {
        return clientUuid;
    }

    public void setClientUuid(String clientUuid) {
        this.clientUuid = clientUuid;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }
}