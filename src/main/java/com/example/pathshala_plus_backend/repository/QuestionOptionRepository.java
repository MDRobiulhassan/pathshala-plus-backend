package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.EducationLevel;
import com.example.pathshala_plus_backend.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption,Long> {
}
