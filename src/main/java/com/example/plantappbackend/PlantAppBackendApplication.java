package com.example.plantappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
@EntityScan(basePackages = "com.example.plantappbackend.model")
@SpringBootApplication
public class PlantAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantAppBackendApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
