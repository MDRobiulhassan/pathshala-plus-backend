package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.dto.response.examsession.ExamSessionResponse;
import com.example.pathshala_plus_backend.entity.ExamSession;
import com.example.pathshala_plus_backend.repository.ExamSessionRepository;
import com.example.pathshala_plus_backend.service.ExamSessionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ExamSessionServiceImpl implements ExamSessionService {

    ExamSessionRepository examSessionRepository;

    @Override
    public List<ExamSessionResponse> getAllExamSessionByInstituteAndUnit(Long instituteId, Long unitId) {
        List<ExamSession> examSessions = examSessionRepository.findByInstituteIdAndUnitId(instituteId, unitId);
        return examSessions.stream()
                .map(examSession -> new ExamSessionResponse(
                        examSession.getId(),
                        examSession.getSessionLabel(),
                        examSession.getStartYear(),
                        examSession.getEndYear(),
                        examSession.getInstitute().getId(),
                        examSession.getUnit().getId()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExamSessionResponse> getAllExamSessionByInstitute(Long instituteId) {
        List<ExamSession> examSessions = examSessionRepository.findByInstituteIdAndUnitIsNull(instituteId);
        return examSessions.stream()
                .map(examSession -> new ExamSessionResponse(
                        examSession.getId(),
                        examSession.getSessionLabel(),
                        examSession.getStartYear(),
                        examSession.getEndYear(),
                        examSession.getInstitute().getId(),
                        examSession.getUnit() != null ? examSession.getUnit().getId() : null
                ))
                .collect(Collectors.toList());
    }
}
