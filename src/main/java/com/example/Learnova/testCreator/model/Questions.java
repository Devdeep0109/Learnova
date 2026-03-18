package com.example.Learnova.testCreator.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String questionText;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String correctAnswer;

    private String difficulty;

    private String topic;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Documents document;

    private boolean aiGenerated;

    private LocalDateTime createdAt;
}
