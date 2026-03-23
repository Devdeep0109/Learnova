package com.example.Learnova.testPerformer.model;

import com.example.Learnova.testCreator.model.Questions;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TestAttemptAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which attempt
    @ManyToOne
    @JoinColumn(name = "attempt_id")
    private TestAttempt attempt;

    // Which question
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Questions question;

    // What user selected (A/B/C/D)
    private String selectedOption;

    // Correct answer snapshot
    private String correctOption;

    // Marks for this question
    private Integer marks;

    // Whether correct
    private Boolean correct;
}