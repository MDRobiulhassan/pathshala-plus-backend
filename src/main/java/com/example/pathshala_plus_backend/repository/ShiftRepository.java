package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.Shift;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShiftRepository extends JpaRepository<@NonNull Shift, @NonNull Long> {
    List<Shift> findByUnitIdAndExamSessionIdOrderByShiftNumberAsc(Long unitId, Long examSessionId);

    @Query("SELECT DISTINCT s.examSession.id FROM Shift s")
    List<Long> findDistinctExamSessionsIds();
}
