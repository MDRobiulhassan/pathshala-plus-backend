package com.example.pathshala_plus_backend.dto.response.question;

public record QuestionPassageResponse(
        Long passageId,
        String passageText,
        String passageImageUrl
) {
}
