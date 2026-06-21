package com.example.pathshala_plus_backend.dto.response.question.subjectquestion;

import java.util.List;

public record PassageWithTagResponse(
        Long passageId,
        String passageText,
        String passageImageUrl,
        List<QuestionWithTagResponse> questions
) {}