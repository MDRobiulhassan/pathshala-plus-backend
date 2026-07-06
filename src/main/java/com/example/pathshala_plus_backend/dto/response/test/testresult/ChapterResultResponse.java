package com.example.pathshala_plus_backend.dto.response.test.testresult;

import java.util.List;

public record ChapterResultResponse(
        Long chapterId,
        String chapterName,
        List<PassageResultResponse> passages
) {
}