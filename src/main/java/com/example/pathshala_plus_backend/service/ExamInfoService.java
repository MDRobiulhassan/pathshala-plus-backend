package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.examinfo.ExamInfoResponse;

import java.util.List;

public interface ExamInfoService {
    List<ExamInfoResponse> getExamInfoByExamSession(Long examSessionId);
    List<ExamInfoResponse> getExamInfoByUnitAndExamSession(Long unitId, Long examSessionId);
    List<ExamInfoResponse> getExamInfoByExamSessionAndShift(Long examSessionId, Long shiftId);
}
