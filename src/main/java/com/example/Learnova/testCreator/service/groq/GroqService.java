package com.example.Learnova.testCreator.service.groq;

import com.example.Learnova.testCreator.dto.GroqRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroqService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateMcq(String prompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        //build object (NOT STRING)
        GroqRequest request = new GroqRequest(
                "llama-3.3-70b-versatile",
                List.of(new GroqRequest.Message("user", prompt)),
                0.3
        );

        HttpEntity<GroqRequest> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(apiUrl, entity, String.class);

        return response.getBody();
    }
}