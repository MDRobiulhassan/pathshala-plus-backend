package com.example.pathshala_plus_backend.dto.response.question;

import java.util.List;

public record PassageQuestionsResponse(
        Long passageId,
        String passageText,
        String passageImageUrl,
        List<QuestionResponse> questions
) {}