package com.example.pathshala_plus_backend.dto.response.test.testhistory;

import java.util.List;

public record TestAttemptHistory(
        List<TestAttemptHistoryItem> attempts
) {
}