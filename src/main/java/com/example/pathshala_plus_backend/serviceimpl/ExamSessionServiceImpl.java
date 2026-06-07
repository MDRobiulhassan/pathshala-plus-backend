package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.dto.response.examsession.ExamSessionResponse;
import com.example.pathshala_plus_backend.entity.ExamSession;
import com.example.pathshala_plus_backend.repository.ExamSessionRepository;
import com.example.pathshala_plus_backend.repository.ShiftRepository;
import com.example.pathshala_plus_backend.service.ExamSessionService;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class ExamSessionServiceImpl implements ExamSessionService {

    ExamSessionRepository examSessionRepository;
    ShiftRepository shiftRepository;

    @Override
    public List<ExamSessionResponse> getAllExamSessionByInstituteAndUnit(Long instituteId, Long unitId) {
        List<ExamSession> examSessions = examSessionRepository.findByInstituteIdAndUnitIdOrderByStartYearDesc(instituteId, unitId);
        Set<Long> examSessionIdsWithShifts = new HashSet<>(shiftRepository.findDistinctExamSessionsIds());
        return examSessions.stream()
                .map(examSession -> new ExamSessionResponse(
                        examSession.getId(),
                        examSession.getSessionLabel(),
                        examSession.getStartYear(),
                        examSession.getEndYear(),
                        examSession.getInstitute().getId(),
                        examSession.getUnit().getId(),
                        examSessionIdsWithShifts.contains(examSession.getId())
                ))
                .toList();
    }

    @Override
    public List<ExamSessionResponse> getAllExamSessionByInstitute(Long instituteId) {
        List<ExamSession> examSessions = examSessionRepository.findByInstituteIdAndUnitIsNullOrderByStartYearDesc(instituteId);
        return examSessions.stream()
                .map(examSession -> new ExamSessionResponse(
                        examSession.getId(),
                        examSession.getSessionLabel(),
                        examSession.getStartYear(),
                        examSession.getEndYear(),
                        examSession.getInstitute().getId(),
                        examSession.getUnit() != null ? examSession.getUnit().getId() : null,
                        false
                ))
                .toList();
    }
}
