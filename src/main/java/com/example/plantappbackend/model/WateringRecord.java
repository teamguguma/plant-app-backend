package com.example.plantappbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "watering_record")
public class WateringRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false) // 외래키 매핑, NULL 허용 불가
    private Plant plant;

    @Column(name = "watered_at", nullable = false)
    private LocalDate wateredAt; // 물 준 날짜, NULL 허용 불가

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public LocalDate getWateredAt() {
        return wateredAt;
    }

    public void setWateredAt(LocalDate wateredAt) {
        this.wateredAt = wateredAt;
    }
}