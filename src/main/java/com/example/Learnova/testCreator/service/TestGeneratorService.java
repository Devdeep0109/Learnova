package com.example.Learnova.testCreator.service;

import com.example.Learnova.exception.FileProcessingException;
import com.example.Learnova.exception.ResourceNotFoundException;
import com.example.Learnova.testCreator.dto.GenerateTestRequest;
import com.example.Learnova.testCreator.dto.McqResponse;
import com.example.Learnova.testCreator.dto.QuestionDTO;
import com.example.Learnova.testCreator.model.Documents;
import com.example.Learnova.testCreator.model.Questions;
import com.example.Learnova.testCreator.model.TestQuestions;
import com.example.Learnova.testCreator.model.Tests;
import com.example.Learnova.testCreator.repository.DocumentsRepository;
import com.example.Learnova.testCreator.repository.QuestionsRepository;
import com.example.Learnova.testCreator.repository.TestQuestionsRepository;
import com.example.Learnova.testCreator.repository.TestsRepository;
import com.example.Learnova.testCreator.service.groq.GroqResponseParser;
import com.example.Learnova.testCreator.service.groq.GroqService;
import com.example.Learnova.testCreator.service.groq.PromptBuilder;
import com.example.Learnova.user.model.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TestGeneratorService {

    private final DocumentsRepository documentsRepo;
    private final QuestionsRepository questionsRepo;
    private final TestsRepository testsRepo;
    private final TestQuestionsRepository testQuestionsRepo;

    private final GroqService groqService;
    private final GroqResponseParser parser;
    private final PromptBuilder promptBuilder;

    public Tests generateTest(GenerateTestRequest req, UserInfo user)
            throws Exception {

        Documents doc = documentsRepo.findById(req.getDocumentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Document not found"));

        try {

            // 2. Build Prompt
            String prompt =
                    promptBuilder.buildPrompt(doc.getExtractedText(), req);

            // 3. Call AI
            String aiResponse =
                    groqService.generateMcq(prompt);

            if (aiResponse == null || aiResponse.isBlank()) {
                throw new FileProcessingException("AI returned empty response");
            }

            // 4. Parse Response
            McqResponse mcqs = parser.parse(aiResponse);

            if (mcqs.getQuestions().isEmpty()) {
                throw new FileProcessingException("No questions generated");
            }

            // 5. Create Test
            Tests test = new Tests();
            test.setTitle(req.getTitle());
            test.setDurationMinutes(req.getDurationMinutes());
            test.setCreatedBy(user);
            test.setCreatedAt(LocalDateTime.now());

            testsRepo.save(test);

            // 6. Save Questions
            for (QuestionDTO qdto : mcqs.getQuestions()) {

                Questions q = new Questions();
                q.setQuestionText(qdto.getQuestion());
                q.setOptionA(qdto.getOptions().get(0));
                q.setOptionB(qdto.getOptions().get(1));
                q.setOptionC(qdto.getOptions().get(2));
                q.setOptionD(qdto.getOptions().get(3));
                q.setCorrectAnswer(qdto.getCorrectAnswer());
                q.setDifficulty(qdto.getDifficulty());
                q.setTopic(req.getFocusTopic());
                q.setDocument(doc);
                q.setAiGenerated(true);
                q.setCreatedAt(LocalDateTime.now());

                questionsRepo.save(q);

                TestQuestions tq = new TestQuestions();
                tq.setTest(test);
                tq.setQuestion(q);
                tq.setMarks(1);

                testQuestionsRepo.save(tq);
            }
            return test;

        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new FileProcessingException(
                    "Failed to generate test using AI: " + ex.getMessage()
            );
        }
    }
}