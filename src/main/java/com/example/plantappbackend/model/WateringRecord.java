package com.example.plantappbackend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class WateringRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    private LocalDate wateredAt;

    // Getters and Setters
}