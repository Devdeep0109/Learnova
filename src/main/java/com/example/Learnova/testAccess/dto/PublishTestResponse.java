package com.example.Learnova.testAccess.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PublishTestResponse {

    private Long accessId;

    private String accessToken;

    private String shareLink;

    private LocalDateTime availableFrom;

    private LocalDateTime availableUntil;

    private Integer maxAttempts;

    private Boolean active;
}