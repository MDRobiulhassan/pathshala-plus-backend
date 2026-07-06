package com.example.pathshala_plus_backend.dto.response.test.testresult;

import com.example.pathshala_plus_backend.enums.OptionLabel;

public record QuestionOptionResultResponse(
        Long optionId,
        OptionLabel optionLabel,
        String optionText,
        String optionImageUrl,
        Boolean selected,
        Boolean correctOption
) {
}