package com.example.pathshala_plus_backend.dto.response.test.testresult;

import java.util.List;

public record PassageResultResponse(
        Long passageId,
        String passageText,
        String passageImageUrl,
        List<QuestionResultResponse> questions
) {
}