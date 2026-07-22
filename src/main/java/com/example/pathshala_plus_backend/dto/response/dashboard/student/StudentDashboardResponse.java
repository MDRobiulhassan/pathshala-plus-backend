package com.example.pathshala_plus_backend.dto.response.dashboard.student;

import java.util.List;

public record StudentDashboardResponse(
        ContinueLearningResponse continueLearning,
        LearningProgressResponse learningProgress,
        List<RecentTestResponse> recentTests
) {
}