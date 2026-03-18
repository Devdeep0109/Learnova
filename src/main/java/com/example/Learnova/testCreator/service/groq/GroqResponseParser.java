package com.example.Learnova.testCreator.service.groq;

import com.example.Learnova.testCreator.dto.McqResponse;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class GroqResponseParser {

    private final ObjectMapper mapper = new ObjectMapper();

    public McqResponse parse(String rawResponse) throws Exception {

        JsonNode root = mapper.readTree(rawResponse);

        String content =
                root.path("choices")
                        .get(0)
                        .path("message")
                        .path("content")
                        .asText();

        // ⭐ CLEAN AI RESPONSE (FIX HERE)
        content = cleanJson(content);

        return mapper.readValue(content, McqResponse.class);
    }
    private String cleanJson(String content) {

        if (content == null) return "";

        return content
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }
}