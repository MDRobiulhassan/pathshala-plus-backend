package com.example.pathshala_plus_backend.dto.response.examinfo;

import com.example.pathshala_plus_backend.enums.ExamType;

public record ExamInfoResponse(
        Long id,
        Long testId,
        ExamType examType,
        Integer totalMarks,
        Integer durationInMinutes
) {
}
