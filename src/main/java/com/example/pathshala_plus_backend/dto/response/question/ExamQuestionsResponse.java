package com.example.pathshala_plus_backend.dto.response.question;

import java.util.List;

public record ExamQuestionsResponse(
        QuestionExamInfoResponse examInfo,
        String languageCode,
        List<SubjectQuestionsResponse> subjects
) {
}