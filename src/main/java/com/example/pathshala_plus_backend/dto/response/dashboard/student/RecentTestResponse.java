package com.example.pathshala_plus_backend.dto.response.dashboard.student;

import java.time.Instant;

public record RecentTestResponse(
        Long attemptId,
        Long testId,
        String testName,
        Float score,
        Boolean passed,
        Instant finishedAt
) {
}