package com.example.Learnova.testAccess.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PublishTestRequest {

    //Optional scheduling
    private LocalDateTime availableFrom;

    private LocalDateTime availableUntil;

    //Attempt restriction
    private Integer maxAttempts;
}
