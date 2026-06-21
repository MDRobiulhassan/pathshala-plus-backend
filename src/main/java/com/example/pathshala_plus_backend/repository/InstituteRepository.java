package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.Institute;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstituteRepository extends JpaRepository<@NonNull Institute,@NonNull Long> {
    List<Institute> findByEducationLevel_Id(Long educationLevelId);
}
