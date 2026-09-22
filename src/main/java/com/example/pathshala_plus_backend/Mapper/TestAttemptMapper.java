package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.response.dashboard.student.ContinueLearningResponse;
import com.example.pathshala_plus_backend.dto.response.dashboard.student.RecentTestResponse;
import com.example.pathshala_plus_backend.dto.response.test.TestAttemptResponse;
import com.example.pathshala_plus_backend.dto.response.test.testhistory.TestAttemptHistoryItem;
import com.example.pathshala_plus_backend.dto.response.test.testquestion.TestInfoResponse;
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

    @Mapping(target = "attemptId", source = "attempt.id")
    @Mapping(target = "testId", source = "attempt.test.id")
    @Mapping(target = "resumed", source = "resumed")
    TestAttemptResponse toAttemptResponse(TestAttempt attempt, Boolean resumed);

    @Mapping(target = "attemptId", source = "id")
    @Mapping(target = "examInfoId", source = "test.examInfo.id")
    @Mapping(target = "testName", source = "test.name")
    @Mapping(target = "testTypeName", source = "test.testType.testTypeName")
    @Mapping(target = "examType", source = "test.examInfo.examType")
    @Mapping(target = "totalQuestions", source = "test.totalQuestions")
    @Mapping(target = "totalMarks", source = "test.examInfo.totalMarks")
    @Mapping(target = "durationInMinutes", source = "test.durationMinutes")
    @Mapping(target = "marksPerQuestion", source = "test.marksPerQuestion")
    @Mapping(target = "negativeMarksPerQuestion", source = "test.negativeMarksPerQuestion")
    @Mapping(target = "passingMarks", source = "test.passMarks")
    TestInfoResponse toTestInfoResponse(TestAttempt attempt);

    @Mapping(target = "attemptId", source = "id")
    @Mapping(target = "testId", source = "test.id")
    @Mapping(target = "testName", source = "test.name")
    @Mapping(target = "timeTakenInSeconds", expression = "java(attempt.getFinishedAt() != null " +
            "? (int) java.time.Duration.between(attempt.getStartedAt(), attempt.getFinishedAt()).getSeconds() : 0)")
    TestAttemptHistoryItem toHistoryItem(TestAttempt attempt);

    @Mapping(target = "attemptId", source = "id")
    @Mapping(target = "testId", source = "test.id")
    @Mapping(target = "testName", source = "test.name")
    RecentTestResponse toRecentTestResponse(TestAttempt attempt);

    @Mapping(target = "attemptId", source = "attempt.id")
    @Mapping(target = "testId", source = "attempt.test.id")
    @Mapping(target = "testName", source = "attempt.test.name")
    @Mapping(target = "totalQuestions", source = "attempt.test.totalQuestions")
    @Mapping(target = "answeredQuestions", source = "answeredQuestions")
    @Mapping(target = "remainingTimeInMinutes", source = "remainingTimeInMinutes")
    ContinueLearningResponse toContinueLearningResponse(TestAttempt attempt, Integer answeredQuestions, Integer remainingTimeInMinutes);
}