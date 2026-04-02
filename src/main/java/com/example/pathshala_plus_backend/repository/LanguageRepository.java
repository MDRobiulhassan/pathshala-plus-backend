package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.EducationLevel;
import com.example.pathshala_plus_backend.entity.Language;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<@NonNull Language,@NonNull Long> {
}
