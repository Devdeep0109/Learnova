package com.example.Learnova.testCreator.service;

import com.example.Learnova.testCreator.dto.GenerateTestRequest;
import com.example.Learnova.testCreator.dto.McqResponse;
import com.example.Learnova.testCreator.dto.QuestionDTO;
import com.example.Learnova.testCreator.model.Documents;
import com.example.Learnova.testCreator.model.Questions;
import com.example.Learnova.testCreator.model.TestQuestions;
import com.example.Learnova.testCreator.model.Tests;
import com.example.Learnova.testCreator.repository.QuestionsRepository;
import com.example.Learnova.testCreator.repository.TestQuestionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionsRepository questionsRepo;
    private final TestQuestionsRepository testQuestionsRepo;

    // ================= SAVE =================
    public void saveQuestions(Tests test,
                              McqResponse mcqs,
                              Documents doc,
                              GenerateTestRequest req) {

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
    }

    // ================= FETCH =================
    public List<QuestionDTO> getQuestionsByTest(Long testId) {

        List<TestQuestions> testQuestions =
                testQuestionsRepo.findByTestId(testId);

        return testQuestions.stream()
                .map(tq -> mapToDTO(tq.getQuestion()))
                .toList();
    }

    private QuestionDTO mapToDTO(Questions q) {

        QuestionDTO dto = new QuestionDTO();

        dto.setQuestion(q.getQuestionText());
        dto.setOptions(List.of(
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC(),
                q.getOptionD()
        ));
        dto.setCorrectAnswer(q.getCorrectAnswer());
        dto.setDifficulty(q.getDifficulty());

        return dto;
    }
}