package com.example.pathshala_plus_backend.dto.response.dashboard.student;

public record LearningProgressResponse(
        Integer totalTestsTaken,
        Float averageScore,
        Float highestScore,
        Integer questionsSolved,
        Float accuracyPercentage,
        Integer studyStreak
) {
}