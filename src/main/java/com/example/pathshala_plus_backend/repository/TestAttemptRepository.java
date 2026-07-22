package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.TestAttempt;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestAttemptRepository extends JpaRepository<@NonNull TestAttempt, @NonNull Long> {
    Optional<TestAttempt> findByTestIdAndUserIdAndCompletedFalse(Long testId, Long userId);

    @Query("""
                    SELECT ta FROM TestAttempt ta
                    JOIN FETCH ta.test
                    WHERE ta.user.id = :userId AND ta.completed = true
                    ORDER BY ta.finishedAt DESC
            """)
    List<TestAttempt> findByUserIdAndCompletedTrueOrderByFinishedAtDesc(@Param("userId") Long userId);

    Optional<TestAttempt> findFirstByUserIdAndCompletedFalseOrderByStartedAtDesc(Long userId);

    List<TestAttempt> findByUserIdAndCompletedTrue(Long userId);

    @Query("""
            SELECT ta FROM TestAttempt ta
            JOIN FETCH ta.test
            WHERE ta.user.id = :userId AND ta.completed = true
            ORDER BY ta.finishedAt DESC
            """)
    List<TestAttempt> findByUserIdAndCompletedTrueOrderByFinishedAtDesc(@Param("userId") Long userId, Pageable pageable);
}
