package com.example.pathshala_plus_backend.dto.response.question;

import com.example.pathshala_plus_backend.enums.ExamType;

public record QuestionExamInfoResponse(
        Long examInfoId,
        ExamType examType,
        Integer totalMarks,
        Integer durationInMinutes
) {
}