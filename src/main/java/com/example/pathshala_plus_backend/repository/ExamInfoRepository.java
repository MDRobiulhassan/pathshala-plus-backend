package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.ExamInfo;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamInfoRepository extends JpaRepository<@NonNull ExamInfo,@NonNull Long> {
    List<ExamInfo> findByExamSessionIdAndUnitIdNullAndShiftIdNull(Long examSessionId);

    List<ExamInfo> findByUnitIdAndExamSessionIdAndShiftIdNull(Long unitId, Long examSessionId);

    List<ExamInfo> findByExamSessionIdAndShiftId(Long examSessionId, Long shiftId);
}
