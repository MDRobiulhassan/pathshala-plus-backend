package com.example.pathshala_plus_backend.dto.response.test.testhistory;

import java.time.Instant;

public record TestAttemptHistoryItem(
        Long attemptId,
        Long testId,
        String testName,
        Float score,
        Boolean passed,
        Instant finishedAt,
        Integer timeTakenInSeconds
) {
}