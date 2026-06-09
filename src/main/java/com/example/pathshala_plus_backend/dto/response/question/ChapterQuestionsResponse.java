package com.example.pathshala_plus_backend.dto.response.question;

import java.util.List;

public record ChapterQuestionsResponse(
        Long chapterId,
        String chapterName,
        List<QuestionResponse> questions
) {
}