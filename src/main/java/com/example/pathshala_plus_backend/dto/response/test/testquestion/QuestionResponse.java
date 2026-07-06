package com.example.pathshala_plus_backend.dto.response.test.testquestion;

import com.example.pathshala_plus_backend.enums.Difficulty;

import java.util.List;

public record QuestionResponse(
        Long questionId,
        String questionText,
        String questionImageUrl,
        Difficulty difficulty,
        List<QuestionOptionResponse> options
) {}