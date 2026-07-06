package com.example.pathshala_plus_backend.dto.response.test.testresult;

import java.util.List;

public record TestResultResponse(
        TestResultSummaryResponse summary,
        List<SubjectResultResponse> subjects
) {
}