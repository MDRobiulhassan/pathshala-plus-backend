package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.Test;
import com.example.pathshala_plus_backend.entity.TestAttempt;
import com.example.pathshala_plus_backend.entity.User;
import com.example.pathshala_plus_backend.model.TestScoreSummary;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface TestAttemptMapper {

    @Mapping(target = "test", source = "test")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "startedAt", ignore = true)
    @Mapping(target = "completed", constant = "false")
    @Mapping(target = "score", constant = "0F")
    TestAttempt fromTestAndUser(Test test, User user);

    @AfterMapping
    default void setStartedAt(@MappingTarget TestAttempt attempt) {
        attempt.setStartedAt(Instant.now());
    }

    @Mapping(target = "score", source = "summary.score")
    @Mapping(target = "correctAnswers", source = "summary.correctAnswers")
    @Mapping(target = "incorrectAnswers", source = "summary.incorrectAnswers")
    @Mapping(target = "unansweredQuestions", source = "summary.unansweredQuestions")
    @Mapping(target = "completed", constant = "true")
    @Mapping(target = "passed", ignore = true)
    @Mapping(target = "finishedAt", ignore = true)
    void completeAttempt(@MappingTarget TestAttempt attempt, TestScoreSummary summary);

    @AfterMapping
    default void finalizeAttempt(@MappingTarget TestAttempt attempt, TestScoreSummary summary) {
        attempt.setPassed(summary.score() >= attempt.getTest().getPassMarks());
        attempt.setFinishedAt(Instant.now());
    }
}