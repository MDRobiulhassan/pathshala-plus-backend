package com.example.pathshala_plus_backend.dto.response.test.testresult;

import java.util.List;

public record SubjectResultResponse(
        Long subjectId,
        String subjectName,
        List<ChapterResultResponse> chapters
) {
}