package com.example.plantappbackend.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CameraService {

    private static final long MAX_IMAGE_SIZE = 1024 * 1024; // 최대 이미지 크기 1MB
    private static final float MIN_QUALITY = 0.1f; // 최소 이미지 품질

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    /**
     * 이미지를 반복적으로 압축하여 최대 크기 제한 아래로 줄입니다.
     */
    private byte[] compressImageUntilBelowLimit(byte[] imageBytes) {
        try {
            float quality = 0.8f; // 초기 품질 설정
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

            while (imageBytes.length > MAX_IMAGE_SIZE) {
                ByteArrayOutputStream compressedOutputStream = new ByteArrayOutputStream();

                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                if (!writers.hasNext()) {
                    throw new IOException("JPEG 포맷의 ImageWriter를 찾을 수 없습니다.");
                }
                ImageWriter writer = writers.next();

                try (ImageOutputStream outputStream = ImageIO.createImageOutputStream(compressedOutputStream)) {
                    writer.setOutput(outputStream);

                    ImageWriteParam params = writer.getDefaultWriteParam();
                    params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    params.setCompressionQuality(quality);

                    writer.write(null, new javax.imageio.IIOImage(originalImage, null, null), params);
                } finally {
                    writer.dispose();
                }

                imageBytes = compressedOutputStream.toByteArray();
                System.out.println("압축 후 이미지 크기: " + imageBytes.length / 1024 + "KB");

                quality -= 0.1f;
                if (quality < MIN_QUALITY) {
                    throw new IOException("최소 품질로 압축해도 크기를 줄일 수 없습니다.");
                }
            }
            return imageBytes;
        } catch (IOException e) {
            throw new RuntimeException("이미지 압축 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     * 이미지 크기 확인 및 필요 시 압축 실행.
     */
    private byte[] validateAndCompressImage(MultipartFile image) {
        try {
            byte[] imageBytes = image.getBytes();
            if (imageBytes.length > MAX_IMAGE_SIZE) {
                System.out.println("이미지 크기가 1MB를 초과하여 압축합니다.");
                return compressImageUntilBelowLimit(imageBytes);
            }
            return imageBytes;
        } catch (IOException e) {
            throw new RuntimeException("이미지 처리 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     * OpenAI GPT API 호출.
     */
    private Map<String, String> callGptApi(List<Map<String, Object>> messages) throws IOException {
        String gptEndpoint = "https://api.openai.com/v1/chat/completions";

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
                    return parseResponseContent((String) message.get("content"));
                }
            }
        }
        return Map.of("name", "식별 실패", "status", "분석 실패");
    }

    /**
     * 식물 분석 요청 로직.
     */
    private Map<String, String> detectPlant(MultipartFile image, String systemPrompt, String userPrompt) {
        try {
            // 이미지 압축 및 Base64 변환
            byte[] compressedImage = validateAndCompressImage(image);
            String base64Image = Base64.encodeBase64String(compressedImage);

            // GPT 메시지 생성
            List<Map<String, Object>> messages = List.of(
                    Map.of("role", "system", "content", systemPrompt),
                    Map.of("role", "user", "content", userPrompt + "\n[이미지 데이터: " + base64Image.substring(0, 50) + "...]")
            );

            // API 호출
            return callGptApi(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "처리 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 식물 이름만 요청.
     */
    public Map<String, String> detectPlantName(MultipartFile image) {
        return detectPlant(image,
                "당신은 식물의 이름을 명확하게 알려주는 도우미입니다. 응답은 '이름: [식물 이름]' 형식으로 제공하세요.",
                "이 이미지는 식물입니다. 반드시 식물의 이름을 알려주세요.");
    }

    /**
     * 식물 이름과 상태 요청.
     */
    public Map<String, String> detectPlantNameAndStatus(MultipartFile image) {
        return detectPlant(image,
                "당신은 식물의 이름, 상태 및 대처법을 알려주는 도우미입니다. 다음 형식을 따르세요:\n" +
                        "이름: [식물 이름]\n상태: [식물 상태]\n대처법: [대처 방법]",
                "이 이미지는 식물입니다. 이름, 상태 및 대처법을 알려주세요.");
    }

    /**
     * GPT 응답 파싱.
     */
    private Map<String, String> parseResponseContent(String content) {
        String name = "이름 정보 없음";
        String status = "상태 정보 없음";
        String remedy = "대처법 정보 없음";

        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("이름:")) {
                name = line.replace("이름:", "").trim();
            } else if (line.startsWith("상태:")) {
                status = line.replace("상태:", "").trim();
            } else if (line.startsWith("대처법:")) {
                remedy = line.replace("대처법:", "").trim();
            }
        }

        return Map.of(
                "name", name,
                "status", status,
                "remedy", remedy
        );
    }
}