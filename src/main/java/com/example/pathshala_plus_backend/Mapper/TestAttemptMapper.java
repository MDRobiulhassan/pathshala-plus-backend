package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.Test;
import com.example.pathshala_plus_backend.entity.TestAttempt;
import com.example.pathshala_plus_backend.entity.User;
import com.example.pathshala_plus_backend.model.TestScoreSummary;
import lombok.experimental.UtilityClass;

import java.time.Instant;

@UtilityClass
public class TestAttemptMapper {

    public static TestAttempt fromTestAndUser(Test test, User user) {
        TestAttempt attempt = new TestAttempt();
        attempt.setTest(test);
        attempt.setUser(user);
        attempt.setStartedAt(Instant.now());
        attempt.setCompleted(false);
        attempt.setScore(0F);
        return attempt;
    }

    public static void completeAttempt(TestAttempt attempt, TestScoreSummary summary) {
        attempt.setScore(summary.score());
        attempt.setCorrectAnswers(summary.correctAnswers());
        attempt.setIncorrectAnswers(summary.incorrectAnswers());
        attempt.setUnansweredQuestions(summary.unansweredQuestions());
        attempt.setPassed(summary.score() >= attempt.getTest().getPassMarks());
        attempt.setCompleted(true);
        attempt.setFinishedAt(Instant.now());
    }
}