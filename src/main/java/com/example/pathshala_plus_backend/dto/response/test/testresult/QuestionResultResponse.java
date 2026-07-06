package com.example.pathshala_plus_backend.dto.response.test.testresult;

import com.example.pathshala_plus_backend.enums.Difficulty;

import java.util.List;

public record QuestionResultResponse(
        Long questionId,
        String questionText,
        String questionImageUrl,
        Difficulty difficulty,
        String explanation,
        Boolean isCorrect,
        Float marksAwarded,
        List<QuestionOptionResultResponse> options
) {
}