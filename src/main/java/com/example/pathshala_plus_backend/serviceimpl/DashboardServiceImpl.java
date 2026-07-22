package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.dto.response.dashboard.student.ContinueLearningResponse;
import com.example.pathshala_plus_backend.dto.response.dashboard.student.LearningProgressResponse;
import com.example.pathshala_plus_backend.dto.response.dashboard.student.RecentTestResponse;
import com.example.pathshala_plus_backend.dto.response.dashboard.student.StudentDashboardResponse;
import com.example.pathshala_plus_backend.entity.TestAttempt;
import com.example.pathshala_plus_backend.repository.TestAnswerRepository;
import com.example.pathshala_plus_backend.repository.TestAttemptRepository;
import com.example.pathshala_plus_backend.security.AuthUtil;
import com.example.pathshala_plus_backend.service.DashboardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {
    TestAttemptRepository testAttemptRepository;
    TestAnswerRepository testAnswerRepository;
    AuthUtil authUtil;

    private static final int RECENT_TESTS_LIMIT = 5;
    private static final ZoneId STREAK_ZONE = ZoneId.systemDefault();

    @Override
    public StudentDashboardResponse getStudentDashboard() {
        Long userId = authUtil.getCurrentUserId();

        return new StudentDashboardResponse(
                toContinueLearning(userId),
                toLearningProgress(userId),
                toRecentTests(userId)
        );
    }


    /* ----------------------------------------
     * Private Helper Methods
     * ---------------------------------------- */

    private ContinueLearningResponse toContinueLearning(Long userId) {
        Optional<TestAttempt> attemptOpt = testAttemptRepository
                .findFirstByUserIdAndCompletedFalseOrderByStartedAtDesc(userId);

        if (attemptOpt.isEmpty()) {
            return null;
        }

        TestAttempt attempt = attemptOpt.get();

        if (isExpired(attempt)) {
            return null;
        }

        int answeredQuestions = testAnswerRepository.countByTestAttemptId(attempt.getId());

        Integer remainingTimeInMinutes = null;
        Integer durationMinutes = attempt.getTest().getDurationMinutes();
        if (durationMinutes != null) {
            Instant expiresAt = attempt.getStartedAt().plusSeconds(durationMinutes * 60L);
            long remainingSeconds = expiresAt.getEpochSecond() - Instant.now().getEpochSecond();
            remainingTimeInMinutes = (int) Math.max(0, remainingSeconds / 60);
        }

        return new ContinueLearningResponse(
                attempt.getId(),
                attempt.getTest().getId(),
                attempt.getTest().getName(),
                answeredQuestions,
                attempt.getTest().getTotalQuestions(),
                remainingTimeInMinutes
        );
    }

    private LearningProgressResponse toLearningProgress(Long userId) {
        List<TestAttempt> completedAttempts = testAttemptRepository
                .findByUserIdAndCompletedTrue(userId);

        if (completedAttempts.isEmpty()) {
            return new LearningProgressResponse(0, 0f, 0f, 0, 0f, 0);
        }

        int totalTestsTaken = completedAttempts.size();

        float averageScore = round((float) completedAttempts.stream()
                .mapToDouble(TestAttempt::getScore)
                .average()
                .orElse(0));

        float highestScore = round((float) completedAttempts.stream()
                .mapToDouble(TestAttempt::getScore)
                .max()
                .orElse(0));

        int totalCorrect = completedAttempts.stream()
                .mapToInt(TestAttempt::getCorrectAnswers)
                .sum();
        int totalIncorrect = completedAttempts.stream()
                .mapToInt(TestAttempt::getIncorrectAnswers)
                .sum();
        int questionsSolved = totalCorrect + totalIncorrect;

        float accuracyPercentage = questionsSolved == 0
                ? 0f
                : round((totalCorrect * 100f) / questionsSolved);

        int studyStreak = calculateStudyStreak(completedAttempts);

        return new LearningProgressResponse(
                totalTestsTaken,
                averageScore,
                highestScore,
                questionsSolved,
                accuracyPercentage,
                studyStreak
        );
    }

    private List<RecentTestResponse> toRecentTests(Long userId) {
        Pageable pageable = PageRequest.of(0, RECENT_TESTS_LIMIT);

        return testAttemptRepository
                .findByUserIdAndCompletedTrueOrderByFinishedAtDesc(userId, pageable)
                .stream()
                .map(attempt -> new RecentTestResponse(
                        attempt.getId(),
                        attempt.getTest().getId(),
                        attempt.getTest().getName(),
                        attempt.getScore(),
                        attempt.getPassed(),
                        attempt.getFinishedAt()
                ))
                .toList();
    }

    private int calculateStudyStreak(List<TestAttempt> completedAttempts) {
        TreeSet<LocalDate> activeDates = new TreeSet<>();
        for (TestAttempt attempt : completedAttempts) {
            if (attempt.getFinishedAt() != null) {
                activeDates.add(attempt.getFinishedAt().atZone(STREAK_ZONE).toLocalDate());
            }
        }

        if (activeDates.isEmpty()) {
            return 0;
        }

        LocalDate today = LocalDate.now(STREAK_ZONE);
        LocalDate cursor = activeDates.contains(today) ? today : today.minusDays(1);

        if (!activeDates.contains(cursor)) {
            return 0;
        }

        int streak = 0;
        while (activeDates.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }

    private boolean isExpired(TestAttempt attempt) {
        Integer durationMinutes = attempt.getTest().getDurationMinutes();
        if (durationMinutes == null) {
            return false;
        }
        Instant expiresAt = attempt.getStartedAt().plusSeconds(durationMinutes * 60L);
        return Instant.now().isAfter(expiresAt);
    }

    private float round(float value) {
        return Math.round(value * 100f) / 100f;
    }
}
