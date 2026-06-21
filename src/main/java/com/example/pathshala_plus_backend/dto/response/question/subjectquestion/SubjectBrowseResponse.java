package com.example.pathshala_plus_backend.dto.response.question.subjectquestion;

import java.util.List;

public record SubjectBrowseResponse(
        Long subjectId,
        String subjectName,
        int page,
        int size,
        long totalElements,
        int totalPages,
        List<PassageWithTagResponse> passages
) {}