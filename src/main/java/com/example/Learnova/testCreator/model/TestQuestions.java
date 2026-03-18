package com.example.Learnova.testCreator.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TestQuestions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Tests test;

    @ManyToOne
    private Questions question;

    private Integer marks;
}
