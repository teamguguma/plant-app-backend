package com.example.plantappbackend.dto;
// 촬영 후 사용자에게 반환될 응답
public class CameraResponse {
    private String plantName;
    private String source;

    public CameraResponse(String plantName, String source) {
        this.plantName = plantName;
        this.source = source;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}