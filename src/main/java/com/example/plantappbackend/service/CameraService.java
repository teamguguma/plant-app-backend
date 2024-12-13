package com.example.plantappbackend.service;

import org.apache.tomcat.util.codec.binary.Base64;
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

    @Autowired
    private RestTemplate restTemplate; // REST API 호출을 위한 RestTemplate 객체

    @Value("${openai.api.key}")
    private String openAiApiKey;

    // detectPlant 메서드: 이미지를 GPT-4o 모델에 전송하여 식물 이름을 반환하는 메서드
    public String detectPlant(MultipartFile image) {
        try {
            // GPT API 호출로 식물 이름을 얻음
            return callGpt4oApiForRecognition(image);
        } catch (IOException e) {
            e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
            return "이미지 처리 오류 발생";
        }
    }

    private String callGpt4oApiForRecognition(MultipartFile image) throws IOException {
        String gptEndpoint = "https://api.openai.com/v1/chat/completions"; // ChatGPT API 엔드포인트

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey); // API 키 설정
        headers.setContentType(MediaType.APPLICATION_JSON); // Content-Type을 JSON으로 설정

        // MultipartFile 이미지를 Base64로 인코딩
        String base64Image = Base64.encodeBase64String(image.getBytes());
        String imageUrl = "data:image/png;base64," + base64Image;

        // JSON 요청 바디 구성
        Map<String, Object> imageContent = new HashMap<>();
        imageContent.put("type", "image_url");
        imageContent.put("image_url", Map.of("url", imageUrl));

        List<Map<String, Object>> messages = List.of(
                Map.of("role", "system", "content", "당신은 식물 종 이름을 명사로만 알려주는 도우미입니다."),
                Map.of("role", "user", "content", List.of(
                        Map.of("type", "text", "text", "이 이미지는 식물입니다. 식물 이름과 식물 상태, 대처법에 대해서 알려줘"),
                        imageContent
                ))
        );

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.0);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // GPT API 호출
        ResponseEntity<Map> response = restTemplate.postForEntity(gptEndpoint, requestEntity, Map.class);

        // GPT 응답에서 식물 이름 추출
        Map responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("choices")) {
            List<Map> choices = (List<Map>) responseBody.get("choices");
            Map choice = choices.get(0); // 첫 번째 응답 선택
            Map message = (Map) choice.get("message"); // 메시지 내용 추출
            return (String) message.get("content"); // 텍스트 응답 반환 (식물 이름만)
        }
        return "식물 식별 실패"; // 응답이 없을 경우 기본 메시지 반환
    }
}