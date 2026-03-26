package com.example.Learnova.testCreator.service;

import com.example.Learnova.testCreator.dto.GenerateTestRequest;
import com.example.Learnova.testCreator.dto.McqResponse;
import com.example.Learnova.testCreator.model.Documents;
import com.example.Learnova.testCreator.model.TestCreatedEvent;
import com.example.Learnova.testCreator.model.Tests;
import com.example.Learnova.testCreator.model.enums.Status;
import com.example.Learnova.testCreator.repository.DocumentsRepository;
import com.example.Learnova.testCreator.repository.TestsRepository;
import com.example.Learnova.testCreator.service.groq.GroqResponseParser;
import com.example.Learnova.testCreator.service.groq.GroqService;
import com.example.Learnova.testCreator.service.groq.PromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class AsyncTestGenerationService {

    private final TestsRepository testsRepo;
    private final DocumentsRepository documentsRepo;
    private final QuestionService questionService;
    private final GroqService groqService;
    private final GroqResponseParser parser;
    private final PromptBuilder promptBuilder;

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleTestCreated(TestCreatedEvent event) {

        Long testId = event.testId();
        GenerateTestRequest req = event.request();

        Tests test = testsRepo.findById(testId)
                .orElseThrow(() ->
                        new RuntimeException("Test not found after commit"));

        try {

            test.setStatus(Status.GENERATING);
            testsRepo.save(test);

            Documents doc =
                    documentsRepo.findById(req.getDocumentId())
                            .orElseThrow();

            String prompt =
                    promptBuilder.buildPrompt(
                            doc.getExtractedText(), req);

            String aiResponse =
                    groqService.generateMcq(prompt);

            McqResponse mcqs = parser.parse(aiResponse);

            questionService.saveQuestions(test, mcqs, doc, req);

            test.setStatus(Status.READY);

        } catch (Exception e) {
            test.setStatus(Status.FAILED);
        }

        testsRepo.save(test);
    }
}