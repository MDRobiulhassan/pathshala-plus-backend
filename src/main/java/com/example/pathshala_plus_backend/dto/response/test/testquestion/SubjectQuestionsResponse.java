package com.example.pathshala_plus_backend.dto.response.test.testquestion;

import java.util.List;

public record SubjectQuestionsResponse(
        Long subjectId,
        String subjectName,
        List<ChapterQuestionsResponse> chapters
) {}