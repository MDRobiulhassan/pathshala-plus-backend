package com.example.pathshala_plus_backend.dto.response.test.testresult;

import java.time.Instant;

public record TestResultSummaryResponse(
        Long attemptId,
        Long testId,
        String testName,
        Float score,
        Integer totalMarks,
        Float passMarks,
        Boolean passed,
        Integer totalQuestions,
        Integer correctAnswers,
        Integer incorrectAnswers,
        Integer unansweredQuestions,
        Instant startedAt,
        Instant finishedAt
) {
}