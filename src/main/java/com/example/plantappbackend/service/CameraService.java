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
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CameraService {
    private static final long MAX_IMAGE_SIZE = 1024 * 1024; // 1MB

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    /**
     * 이미지 크기 확인 및 GPT API 호출
     *
     * @param image MultipartFile 이미지
     * @return 식물 이름 (혹은 오류 메시지)
     */
    public String detectPlant(MultipartFile image) {
        try {
            // 이미지 크기 확인
            validateImageSize(image);

            // GPT API 호출로 식물 이름 얻기
            return callGpt4oApiForRecognition(image);
        } catch (IllegalArgumentException e) {
            // 크기 초과 시 오류 메시지 반환
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "이미지 처리 오류 발생";
        }
    }

    /**
     * 이미지 크기 확인
     *
     * @param image MultipartFile 이미지
     */
    private void validateImageSize(MultipartFile image) {
        if (image.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("이미지 크기가 1MB를 초과합니다. 파일 크기를 줄여주세요.");
        }
    }

    /**
     * GPT-4 API 호출
     *
     * @param image MultipartFile 이미지
     * @return 식물 이름
     * @throws IOException 이미지 처리 중 예외
     */
    private String callGpt4oApiForRecognition(MultipartFile image) throws IOException {
        String gptEndpoint = "https://api.openai.com/v1/chat/completions";

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 이미지 파일을 Base64로 인코딩
        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());

        // JSON 요청 바디 구성
        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content", "당신은 식물 종 이름을 명사로만 알려주는 도우미입니다."),
                Map.of("role", "user", "content", "이 이미지는 식물입니다. 이름만 알려주세요."),
                Map.of("role", "user", "content", "data:image/png;base64," + base64Image)
        );

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.0);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // GPT API 호출
        ResponseEntity<Map> response = restTemplate.postForEntity(gptEndpoint, requestEntity, Map.class);

        // 응답 처리
        Map responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("choices")) {
            List<Map> choices = (List<Map>) responseBody.get("choices");
            if (!choices.isEmpty()) {
                Map choice = choices.get(0); // 첫 번째 응답 선택
                Map message = (Map) choice.get("message"); // 메시지 내용 추출
                if (message != null && message.containsKey("content")) {
                    return (String) message.get("content"); // 텍스트 응답 반환
                }
            }
        }
        return "식물 식별 실패"; // 응답이 없을 경우 기본 메시지 반환
    }
}