package com.example.pathshala_plus_backend.dto.response.dashboard.student;

public record ContinueLearningResponse(
        Long attemptId,
        Long testId,
        String testName,
        Integer answeredQuestions,
        Integer totalQuestions,
        Integer remainingTimeInMinutes
) {
}