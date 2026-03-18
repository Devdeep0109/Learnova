package com.example.Learnova.testCreator.service.groq;

import com.example.Learnova.testCreator.dto.GenerateTestRequest;
import org.springframework.stereotype.Service;
@Service
public class PromptBuilder {

    public String buildPrompt(String text, GenerateTestRequest req) {

        return """
        You are an AI exam generator.
        
        Generate %d MCQ questions.
        
        Focus topic: %s
        Difficulty: %s
        
        Rules:
        - Return ONLY JSON
        - No explanation
        - Each question must contain 4 options
        - correctAnswer must match option text
        
        Format:
        
        {
         "questions":[
          {
           "question":"",
           "options":["","","",""],
           "correctAnswer":"",
           "difficulty":""
          }
         ]
        }
        
        Content:
        %s
        """.formatted(
                    req.getNumberOfQuestions(),
                    req.getFocusTopic(),
                    req.getDifficulty(),
                    text.substring(0, Math.min(text.length(), 7000))
            );
    }
}