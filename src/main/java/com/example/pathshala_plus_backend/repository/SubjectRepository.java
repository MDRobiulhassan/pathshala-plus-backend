package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.Subject;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<@NonNull Subject, @NonNull Long> {

    @Query("SELECT s FROM Subject s JOIN FETCH s.educationLevel WHERE s.educationLevel.id = :educationLevelId")
    List<Subject> findByEducationLevelId(@Param("educationLevelId") Long educationLevelId);
}
