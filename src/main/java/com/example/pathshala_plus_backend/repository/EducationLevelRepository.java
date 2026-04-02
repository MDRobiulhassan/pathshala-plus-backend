package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.EducationLevel;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationLevelRepository extends JpaRepository<@NonNull EducationLevel, @NonNull Long> {
}
