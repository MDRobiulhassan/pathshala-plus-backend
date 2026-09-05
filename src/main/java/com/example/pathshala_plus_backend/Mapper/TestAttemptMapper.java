package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.Test;
import com.example.pathshala_plus_backend.entity.TestAttempt;
import com.example.pathshala_plus_backend.entity.User;
import com.example.pathshala_plus_backend.model.TestScoreSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TestAttemptMapper {

    @Mapping(target = "test", source = "test")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "startedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "completed", constant = "false")
    @Mapping(target = "score", constant = "0F")
    TestAttempt fromTestAndUser(Test test, User user);

    @Mapping(target = "score", source = "summary.score")
    @Mapping(target = "correctAnswers", source = "summary.correctAnswers")
    @Mapping(target = "incorrectAnswers", source = "summary.incorrectAnswers")
    @Mapping(target = "unansweredQuestions", source = "summary.unansweredQuestions")
    @Mapping(target = "completed", constant = "true")
    @Mapping(target = "passed", expression = "java(summary.score() >= attempt.getTest().getPassMarks())")
    @Mapping(target = "finishedAt", expression = "java(java.time.Instant.now())")
    void completeAttempt(@MappingTarget TestAttempt attempt, TestScoreSummary summary);
}