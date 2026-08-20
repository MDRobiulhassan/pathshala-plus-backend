package com.example.pathshala_plus_backend.dto.response.subject;

public record SubjectResponse(
        Long subjectId,
        String subjectName,
        Long educationLevelId,
        String logoUrl
) {
}
