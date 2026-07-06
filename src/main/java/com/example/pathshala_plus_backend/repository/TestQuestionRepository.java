package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.TestQuestion;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface TestQuestionRepository extends JpaRepository<@NonNull TestQuestion, @NonNull Long> {
    @Query("""
            SELECT tq FROM TestQuestion tq
            JOIN FETCH tq.question q
            JOIN FETCH q.subject
            JOIN FETCH q.language
            LEFT JOIN FETCH q.chapter
            LEFT JOIN FETCH q.passage
            LEFT JOIN FETCH q.options
            WHERE tq.test.id = :testId
            ORDER BY tq.sequenceOrder
            """)
    List<TestQuestion> findByTestIdWithQuestions(@Param("testId") Long testId);

    @Query("SELECT tq.question.id FROM TestQuestion tq WHERE tq.test.id = :testId")
    Set<Long> findQuestionIdsByTestId(@Param("testId") Long testId);
}
