package com.example.pathshala_plus_backend.dto.response.examsession;


public record ExamSessionResponse(
        Long id,
        String sessionLabel,
        Integer startYear,
        Integer endYear,
        Long instituteId,
        Long unitId
) {
}
