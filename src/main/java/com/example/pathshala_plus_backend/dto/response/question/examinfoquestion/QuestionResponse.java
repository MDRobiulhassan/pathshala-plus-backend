package com.example.pathshala_plus_backend.dto.response.question.examinfoquestion;

import com.example.pathshala_plus_backend.enums.Difficulty;

import java.util.List;

public record QuestionResponse(
        Long questionId,
        String questionText,
        String questionImageUrl,
        String explanation,
        Difficulty difficulty,
        Boolean isApproved,
        List<QuestionOptionResponse> options
) {}