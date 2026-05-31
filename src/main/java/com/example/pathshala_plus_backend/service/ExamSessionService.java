package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.examsession.ExamSessionResponse;

import java.util.List;

public interface ExamSessionService {
    List<ExamSessionResponse> getAllExamSessionByInstituteAndUnit(Long instituteId, Long unitId);
    List<ExamSessionResponse> getAllExamSessionByInstitute(Long instituteId);
}
