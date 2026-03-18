package com.example.Learnova.testPerformer.model;

import com.example.Learnova.testCreator.model.Tests;
import com.example.Learnova.user.model.UserInfo;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TestAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "test_id")
    private Tests test;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo user;

    private Integer score;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
