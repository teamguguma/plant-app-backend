package com.example.plantappbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CameraService {

    private static final long MAX_IMAGE_SIZE = 1024 * 1024; // 이미지 최대 크기 1MB
    private final AwsS3Service awsS3Service;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Autowired
    public CameraService(AwsS3Service awsS3Service) {
        this.awsS3Service = awsS3Service;
    }

    // 이미지 크기 확인
    private void validateImageSize(MultipartFile image) {
        if (image.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("이미지 크기가 1MB를 초과합니다. 파일 크기를 줄여주세요.");
        }
    }

    // GPT API 호출
    private Map<String, String> callGptApi(String imageUrl, List<Map<String, String>> messages) throws IOException {
        String gptEndpoint = "https://api.openai.com/v1/chat/completions";

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4");
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.0);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(gptEndpoint, requestEntity, Map.class);

        Map responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("choices")) {
            List<Map> choices = (List<Map>) responseBody.get("choices");
            if (!choices.isEmpty()) {
                Map choice = choices.get(0);
                Map message = (Map) choice.get("message");
                if (message != null && message.containsKey("content")) {
                    String content = (String) message.get("content");
                    return parseResponseContent(content);
                }
            }
        }
        return Map.of("name", "식별 실패", "status", "분석 실패");
    }

    // 공통 식물 분석 로직
    private Map<String, String> detectPlant(MultipartFile image, String systemPrompt, String userPrompt) {
//        validateImageSize(image);
        String imageUrl = awsS3Service.uploadFile(image, 0, 0, true); // 기본값 전달

        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt),
                Map.of("role", "user", "content", imageUrl)
        );

        try {
            return callGptApi(imageUrl, messages);
        } catch (IOException e) {
            e.printStackTrace();
            return Map.of("error", "GPT API 호출 실패: " + e.getMessage());
        }
    }

    // 요청 바디 구성: 식물 이름만 요청
    public Map<String, String> detectPlantName(MultipartFile image) {
        return detectPlant(image,
                "당신은 식물 종 이름을 명사로만 알려주는 도우미입니다.",
                "이 이미지는 식물입니다. 식물 이름만 알려주세요.");
    }

    // 요청 바디 구성: 식물 이름 및 상태 요청
    public Map<String, String> detectPlantNameAndStatus(MultipartFile image) {
        return detectPlant(image,
                "당신은 식물 상태를 분석하고 이름과 상태를 알려주는 도우미입니다.",
                "이 이미지는 식물입니다. 식물의 질병과 대처방법을 알려주세요.");
    }

    // GPT 응답 파싱
    private Map<String, String> parseResponseContent(String content) {
        String[] parts = content.split("\n", 2); // "\n" 기준으로 나눔
        String name = parts.length > 0 ? parts[0].trim() : "이름 정보 없음";
        String status = parts.length > 1 ? parts[1].trim() : "상태 정보 없음";

        return Map.of("name", name, "status", status);
    }
}