package com.example.pathshala_plus_backend.dto.response.test.testquestion;

import com.example.pathshala_plus_backend.enums.ExamType;
import com.example.pathshala_plus_backend.enums.TestTypeName;

public record TestInfoResponse(
        Long attemptId,
        Long examInfoId,
        String testName,
        TestTypeName testTypeName,
        ExamType examType,
        Integer totalQuestions,
        Integer totalMarks,
        Integer durationInMinutes,
        Float marksPerQuestion,
        Float negativeMarksPerQuestion,
        Float passingMarks
) {
}