package com.example.Learnova.testCreator.service;

import com.example.Learnova.exception.FileProcessingException;
import com.example.Learnova.testCreator.dto.GenerateTestRequest;
import com.example.Learnova.testCreator.dto.McqResponse;
import com.example.Learnova.testCreator.service.groq.GroqResponseParser;
import com.example.Learnova.testCreator.service.groq.GroqService;
import com.example.Learnova.testCreator.service.groq.PromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class McqGenerationService {

    private final GroqService groqService;
    private final GroqResponseParser parser;
    private final PromptBuilder promptBuilder;

    public McqResponse generateMcqs(String text,
                                    GenerateTestRequest req) throws Exception {

        int expected = req.getNumberOfQuestions();

        // Initial generation
        McqResponse response = generateOnce(text, req);

        // Retry if AI returned fewer questions
        int attempts = 0;
        int maxRetries = 2; // safety limit

        while (response.getQuestions().size() < expected && attempts < maxRetries) {

            int missing = expected - response.getQuestions().size();

            System.out.println(
                    "AI returned fewer questions. Missing: " + missing
            );

            String retryPrompt =
                    "Generate ONLY " + missing +
                            " additional MCQ questions in the SAME JSON format.";

            String retryResponse = groqService.generateMcq(retryPrompt);

            McqResponse extra = parser.parse(retryResponse);

            response.getQuestions().addAll(extra.getQuestions());

            attempts++;
        }

        // Final safety validation
        if (response.getQuestions().size() < expected) {
            throw new FileProcessingException(
                    "AI failed to generate required number of questions"
            );
        }

        // Optional: trim if AI generated extra
        response.setQuestions(
                response.getQuestions().subList(0, expected)
        );

        return response;
    }

    // -----------------------------
    // Helper method
    // -----------------------------
    private McqResponse generateOnce(String text,
                                     GenerateTestRequest req) throws Exception {

        String prompt = promptBuilder.buildPrompt(text, req);

        String aiResponse = groqService.generateMcq(prompt);

        if (aiResponse == null || aiResponse.isBlank()) {
            throw new FileProcessingException("AI returned empty response");
        }

        McqResponse mcqs = parser.parse(aiResponse);

        if (mcqs.getQuestions().isEmpty()) {
            throw new FileProcessingException("No questions generated");
        }

        return mcqs;
    }
}