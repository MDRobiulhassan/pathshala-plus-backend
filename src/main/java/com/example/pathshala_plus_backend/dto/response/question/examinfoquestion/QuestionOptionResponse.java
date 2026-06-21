package com.example.pathshala_plus_backend.dto.response.question.examinfoquestion;

import com.example.pathshala_plus_backend.enums.OptionLabel;

public record QuestionOptionResponse(
        Long optionId,
        OptionLabel optionLabel,
        String optionText,
        Boolean isCorrect,
        String optionImageUrl
) {
}
