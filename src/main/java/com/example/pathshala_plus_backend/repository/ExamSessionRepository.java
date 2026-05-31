package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.ExamSession;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamSessionRepository extends JpaRepository<@NonNull ExamSession, @NonNull Long> {
    List<ExamSession> findByInstituteIdAndUnitId(Long instituteId, Long unitId);

    List<ExamSession> findByInstituteIdAndUnitIsNull(Long instituteId);
}
