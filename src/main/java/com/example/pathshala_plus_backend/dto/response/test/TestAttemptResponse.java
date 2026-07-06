package com.example.pathshala_plus_backend.dto.response.test;

import java.time.Instant;

public record TestAttemptResponse(
        Long attemptId,
        Long testId,
        Instant startedAt,
        Instant finishedAt,
        Float score,
        Boolean completed,
        Boolean resumed
) {}
