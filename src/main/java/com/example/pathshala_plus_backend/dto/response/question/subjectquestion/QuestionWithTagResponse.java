package com.example.pathshala_plus_backend.dto.response.question.subjectquestion;

import com.example.pathshala_plus_backend.dto.response.question.examinfoquestion.QuestionOptionResponse;
import com.example.pathshala_plus_backend.enums.Difficulty;

import java.util.List;

public record QuestionWithTagResponse(
        Long questionId,
        String questionText,
        String questionImageUrl,
        String explanation,
        Difficulty difficulty,
        Boolean isApproved,
        String instituteName,
        String unitName,
        String sessionLabel,
        List<QuestionOptionResponse> options
) {}