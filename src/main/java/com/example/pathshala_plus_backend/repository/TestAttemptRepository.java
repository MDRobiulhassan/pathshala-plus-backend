package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.TestAttempt;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestAttemptRepository extends JpaRepository<@NonNull TestAttempt, @NonNull Long> {
    Optional<TestAttempt> findByTestIdAndUserIdAndCompletedFalse(Long testId, Long userId);
}
