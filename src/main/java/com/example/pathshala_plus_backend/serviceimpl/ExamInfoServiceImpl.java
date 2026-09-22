package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.Mapper.ExamInfoMapper;
import com.example.pathshala_plus_backend.dto.response.examinfo.ExamInfoResponse;
import com.example.pathshala_plus_backend.entity.ExamInfo;
import com.example.pathshala_plus_backend.repository.ExamInfoRepository;
import com.example.pathshala_plus_backend.service.ExamInfoService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class ExamInfoServiceImpl implements ExamInfoService {

    ExamInfoRepository examInfoRepository;
    ExamInfoMapper examInfoMapper;

    @Override
    public List<ExamInfoResponse> getExamInfoByExamSession(Long examSessionId) {
        List<ExamInfo> examInfos = examInfoRepository.findByExamSessionIdAndUnitIdNullAndShiftIdNull(examSessionId);
        return examInfos.stream()
                .map(examInfoMapper::toExamInfoResponse)
                .toList();
    }

    @Override
    public List<ExamInfoResponse> getExamInfoByUnitAndExamSession(Long unitId, Long examSessionId) {
        List<ExamInfo> examInfos = examInfoRepository.findByUnitIdAndExamSessionIdAndShiftIdNull(unitId, examSessionId);
        return examInfos.stream()
                .map(examInfoMapper::toExamInfoResponse)
                .toList();
    }

    @Override
    public List<ExamInfoResponse> getExamInfoByExamSessionAndShift(Long examSessionId, Long shiftId) {
        List<ExamInfo> examInfos = examInfoRepository.findByExamSessionIdAndShiftId(examSessionId, shiftId);
        return examInfos.stream()
                .map(examInfoMapper::toExamInfoResponse)
                .toList();
    }
}