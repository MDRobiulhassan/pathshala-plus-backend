package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.Unit;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitRepository extends JpaRepository<@NonNull Unit,@NonNull Long> {
    List<Unit> findByInstituteId(Long instituteId);
}
