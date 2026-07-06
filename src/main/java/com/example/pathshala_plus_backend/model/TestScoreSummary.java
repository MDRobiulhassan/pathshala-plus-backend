package com.example.pathshala_plus_backend.model;

public record TestScoreSummary(
        Float score,
        Integer correctAnswers,
        Integer incorrectAnswers,
        Integer unansweredQuestions
) {
}