package com.example.pathshala_plus_backend.dto.response.test.testquestion;

import com.example.pathshala_plus_backend.enums.OptionLabel;

public record QuestionOptionResponse(
        Long optionId,
        OptionLabel optionLabel,
        String optionText,
        String optionImageUrl,
        Boolean selected
) {
}
