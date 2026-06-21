package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.Question;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<@NonNull Question, @NonNull Long> {

    @Query("""
            SELECT q FROM Question q
            LEFT JOIN FETCH q.options
            LEFT JOIN FETCH q.passage
            JOIN FETCH q.examInfo
            JOIN FETCH q.subject
            LEFT JOIN FETCH q.chapter
            WHERE q.examInfo.id = :examInfoId
            ORDER BY q.subject.id
            """)
    List<Question> findByExamInfoWithDetails(@Param("examInfoId") Long examInfoId);

    @Query("""
            SELECT q.id FROM Question q
            WHERE q.subject.id = :subjectId
            AND q.isDeleted = false
            """)
    Page<@NonNull Long> findIdsBySubjectId(
            @Param("subjectId") Long subjectId,
            Pageable pageable
    );

    @Query("""
            SELECT q FROM Question q
            JOIN FETCH q.examInfo ei
            JOIN FETCH ei.examSession es
            JOIN FETCH es.institute i
            LEFT JOIN FETCH ei.unit u
            LEFT JOIN FETCH q.passage p
            JOIN FETCH q.options
            WHERE q.id IN :ids
            AND q.isDeleted = false
            """)
    List<Question> findByIdsWithDetails(@Param("ids") List<Long> ids);
}
