package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.TestAnswer;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TestAnswerRepository extends JpaRepository<@NonNull TestAnswer, @NonNull Long> {
    @Query("""
            SELECT a FROM TestAnswer a
            LEFT JOIN FETCH a.selectedOption
            JOIN FETCH a.question
            WHERE a.testAttempt.id = :attemptId
            """)
    List<TestAnswer> findByTestAttemptId(@Param("attemptId") Long attemptId);

    int countByTestAttemptId(Long attemptId);
}
