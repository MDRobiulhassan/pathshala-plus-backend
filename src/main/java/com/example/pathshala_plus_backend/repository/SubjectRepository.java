package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.EducationLevel;
import com.example.pathshala_plus_backend.entity.Subject;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<@NonNull Subject,@NonNull Long> {
}
