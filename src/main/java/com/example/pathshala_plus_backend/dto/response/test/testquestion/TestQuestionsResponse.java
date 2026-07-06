package com.example.pathshala_plus_backend.dto.response.test.testquestion;

import java.util.List;

public record TestQuestionsResponse(
        TestInfoResponse testInfo,
        String languageCode,
        List<SubjectQuestionsResponse> subjects
) {
}