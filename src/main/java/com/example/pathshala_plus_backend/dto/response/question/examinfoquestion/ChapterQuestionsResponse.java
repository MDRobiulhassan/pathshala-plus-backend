package com.example.pathshala_plus_backend.dto.response.question.examinfoquestion;

import java.util.List;

public record ChapterQuestionsResponse(
        Long chapterId,
        String chapterName,
        List<PassageQuestionsResponse> passages
) {}