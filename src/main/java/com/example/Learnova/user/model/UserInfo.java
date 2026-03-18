package com.example.Learnova.user.model;

import com.example.Learnova.testPerformer.model.TestAttempt;
import com.example.Learnova.testCreator.model.Tests;
import com.example.Learnova.user.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // STUDENT / FACULTY / ADMIN

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "createdBy")
    @JsonManagedReference
    private List<Tests> tests;

    @OneToMany(mappedBy = "user")
    private List<TestAttempt> attempts;
}
