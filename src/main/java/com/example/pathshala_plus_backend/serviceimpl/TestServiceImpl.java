package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.Mapper.TestAnswerMapper;
import com.example.pathshala_plus_backend.Mapper.TestAttemptMapper;
import com.example.pathshala_plus_backend.dto.request.test.AnswerRequest;
import com.example.pathshala_plus_backend.dto.request.test.SaveAnswersRequest;
import com.example.pathshala_plus_backend.dto.response.test.TestAttemptResponse;
import com.example.pathshala_plus_backend.dto.response.test.TestTypeResponse;
import com.example.pathshala_plus_backend.dto.response.test.testhistory.TestAttemptHistory;
import com.example.pathshala_plus_backend.dto.response.test.testhistory.TestAttemptHistoryItem;
import com.example.pathshala_plus_backend.dto.response.test.testquestion.*;
import com.example.pathshala_plus_backend.dto.response.test.testresult.*;
import com.example.pathshala_plus_backend.entity.*;
import com.example.pathshala_plus_backend.enums.TestTypeName;
import com.example.pathshala_plus_backend.exceptions.BadRequestException;
import com.example.pathshala_plus_backend.exceptions.ResourceNotFoundException;
import com.example.pathshala_plus_backend.model.TestScoreSummary;
import com.example.pathshala_plus_backend.repository.*;
import com.example.pathshala_plus_backend.security.AuthUtil;
import com.example.pathshala_plus_backend.service.TestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class TestServiceImpl implements TestService {

    TestTypeRepository testTypeRepository;
    TestRepository testRepository;
    TestAttemptRepository testAttemptRepository;
    TestQuestionRepository testQuestionRepository;
    UserRepository userRepository;
    QuestionRepository questionRepository;
    TestAnswerRepository testAnswerRepository;
    QuestionOptionRepository questionOptionRepository;
    AuthUtil authUtil;

    @Override
    public List<TestTypeResponse> getAllTestTypes() {
        List<TestType> testTypes = testTypeRepository.findAll();
        return testTypes.stream()
                .map(testType -> new TestTypeResponse(
                        testType.getId(),
                        testType.getTestTypeName()
                )).toList();
    }

    @Override
    public List<TestTypeResponse> getQuestionBankTestTypes() {

        List<TestType> testTypes = testTypeRepository.findByTestTypeNameIn(
                List.of(TestTypeName.PRACTICE, TestTypeName.TIMED)
        );

        return testTypes.stream()
                .map(testType -> new TestTypeResponse(
                        testType.getId(),
                        testType.getTestTypeName()
                ))
                .toList();
    }

    @Override
    public TestAttemptResponse createOrResumeTestAttempt(Long testId) {

        Long userId = authUtil.getCurrentUserId();

        Test test = testRepository.findById(testId).orElseThrow(() -> new ResourceNotFoundException("Test not found"));
        Optional<TestAttempt> existingAttempt = testAttemptRepository.findByTestIdAndUserIdAndCompletedFalse(testId, userId);

        if (existingAttempt.isPresent()) {
            return toAttemptResponse(existingAttempt.get(), true);
        }

        User user = userRepository.getReferenceById(userId);

        TestAttempt attempt = TestAttemptMapper.fromTestAndUser(test, user);
        attempt = testAttemptRepository.save(attempt);

        return toAttemptResponse(attempt, false);
    }

    @Override
    public TestQuestionsResponse getTestAttemptWithQuestions(Long attemptId) {
        TestAttempt attempt = testAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Test Attempt Not Found"));

        List<TestQuestion> testQuestions = testQuestionRepository
                .findByTestIdWithQuestions(attempt.getTest().getId());

        if (testQuestions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for this test");
        }

        List<Question> questions = testQuestions.stream()
                .map(TestQuestion::getQuestion)
                .toList();

        return new TestQuestionsResponse(
                toTestInfoResponse(attempt),
                questions.get(0).getLanguage().getCode(),
                toSubjectResponses(questions)
        );
    }

    @Override
    public void saveProgress(Long attemptId, SaveAnswersRequest request) {
        Long userId = authUtil.getCurrentUserId();

        TestAttempt attempt = testAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Test Attempt Not Found"));

        if (!attempt.getUser().getId().equals(userId)) {
            throw new BadRequestException("User not authorized to save progress for this attempt");
        }
        if (attempt.getCompleted()) {
            throw new BadRequestException("Cannot save progress for a completed attempt");
        }

        Long testId = attempt.getTest().getId();

        Set<Long> validQuestionIds = testQuestionRepository.findQuestionIdsByTestId(testId);

        List<Long> incomingQuestionIds = request.answers().stream()
                .map(AnswerRequest::questionId)
                .toList();

        for (Long questionId : incomingQuestionIds) {
            if (!validQuestionIds.contains(questionId)) {
                throw new BadRequestException("Question does not belong to this test");
            }
        }

        List<Long> incomingOptionIds = request.answers().stream()
                .map(AnswerRequest::selectedOptionId)
                .filter(Objects::nonNull)
                .toList();

        Map<Long, QuestionOption> optionMap = incomingOptionIds.isEmpty()
                ? Map.of()
                : questionOptionRepository.findAllById(incomingOptionIds)
                .stream()
                .collect(Collectors.toMap(QuestionOption::getId, option -> option));

        Map<Long, TestAnswer> existingAnswers = testAnswerRepository
                .findByTestAttemptId(attemptId)
                .stream()
                .collect(Collectors.toMap(
                        answer -> answer.getQuestion().getId(), ans -> ans));

        List<TestAnswer> toSave = new ArrayList<>();

        for (AnswerRequest answerRequest : request.answers()) {
            Long questionId = answerRequest.questionId();

            TestAnswer answer = existingAnswers.getOrDefault(
                    questionId,
                    TestAnswerMapper.toTestAnswer(attempt, questionRepository.getReferenceById(questionId)));

            if (answerRequest.selectedOptionId() == null) {
                answer.setSelectedOption(null);
            } else {
                QuestionOption option = optionMap.get(answerRequest.selectedOptionId());
                if (option == null) {
                    throw new ResourceNotFoundException("Option not found");
                }
                if (!option.getQuestion().getId().equals(questionId)) {
                    throw new BadRequestException("Option does not belong to question ");
                }
                answer.setSelectedOption(option);
            }

            answer.setAnsweredAt(Instant.now());
            toSave.add(answer);
        }

        testAnswerRepository.saveAll(toSave);
    }

    @Override
    public void submitTestAttempt(Long attemptId) {
        Long userId = authUtil.getCurrentUserId();

        TestAttempt attempt = testAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Test attempt not found"));

        if (!attempt.getUser().getId().equals(userId)) {
            throw new BadRequestException("User not authorized to submit this attempt");
        }
        if (attempt.getCompleted()) {
            throw new BadRequestException("Test has already been submitted");
        }

        List<TestAnswer> answers = testAnswerRepository.findByTestAttemptId(attemptId);

        Long testId = attempt.getTest().getId();
        Map<Long, Long> correctOptionByQuestionId = questionOptionRepository
                .findCorrectOptionsByTestId(testId)
                .stream()
                .collect(Collectors.toMap(
                        o -> o.getQuestion().getId(),
                        QuestionOption::getId
                ));

        TestScoreSummary summary = calculateScore(attempt.getTest(), answers, correctOptionByQuestionId);
        TestAttemptMapper.completeAttempt(attempt, summary);
        testAttemptRepository.save(attempt);
        testAnswerRepository.saveAll(answers);
    }

    @Override
    public TestResultResponse getTestAttemptResultById(Long attemptId) {

        Long userId = authUtil.getCurrentUserId();

        TestAttempt attempt = testAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Test attempt not found"));

        if (!attempt.getUser().getId().equals(userId)) {
            throw new BadRequestException("User not authorized to view this result");
        }

        if (!attempt.getCompleted()) {
            throw new BadRequestException("Test has not been submitted yet");
        }

        Long testId = attempt.getTest().getId();

        List<Question> questions = testQuestionRepository.findByTestIdWithQuestions(testId)
                .stream()
                .map(TestQuestion::getQuestion)
                .toList();

        List<Long> questionIds = questions.stream()
                .map(Question::getId)
                .toList();

        Map<Long, List<QuestionOption>> optionsByQuestionId = questionOptionRepository
                .findByQuestionIds(questionIds)
                .stream()
                .collect(Collectors.groupingBy(option -> option.getQuestion().getId()));

        Map<Long, TestAnswer> answerByQuestionId = testAnswerRepository
                .findByTestAttemptId(attemptId)
                .stream()
                .collect(Collectors.toMap(
                        answer -> answer.getQuestion().getId(),
                        answer -> answer
                ));

        TestResultSummaryResponse summary = toTestResultSummaryResponse(
                attempt,
                attempt.getTest(),
                attempt.getPassed(),
                attempt.getCorrectAnswers(),
                attempt.getIncorrectAnswers(),
                attempt.getUnansweredQuestions()
        );

        List<SubjectResultResponse> subjects = toSubjectResultResponses(
                questions,
                optionsByQuestionId,
                answerByQuestionId
        );

        return new TestResultResponse(summary, subjects);
    }

    @Override
    public TestAttemptHistory getUserTestAttempts() {
        Long userId = authUtil.getCurrentUserId();
        List<TestAttemptHistoryItem> attempts = testAttemptRepository.findByUserIdAndCompletedTrueOrderByFinishedAtDesc(userId)
                .stream()
                .map(attempt -> new TestAttemptHistoryItem(
                        attempt.getId(),
                        attempt.getTest().getId(),
                        attempt.getTest().getName(),
                        attempt.getScore(),
                        attempt.getPassed(),
                        attempt.getFinishedAt(),
                        attempt.getFinishedAt() != null
                                ? (int) Duration.between(
                                attempt.getStartedAt(),
                                attempt.getFinishedAt()
                        ).getSeconds() : 0
                )).toList();
        return new TestAttemptHistory(attempts);
    }

    /* ----------------------------------------
     * Private Helper Methods
     * ---------------------------------------- */

    private TestAttemptResponse toAttemptResponse(TestAttempt attempt, Boolean resumed) {
        return new TestAttemptResponse(
                attempt.getId(),
                attempt.getTest().getId(),
                attempt.getStartedAt(),
                attempt.getFinishedAt(),
                attempt.getScore(),
                attempt.getCompleted(),
                resumed
        );
    }

    private TestScoreSummary calculateScore(Test test, List<TestAnswer> answers, Map<Long, Long> correctOptionByQuestionId) {

        int correct = 0;
        int incorrect = 0;
        int unanswered = 0;
        float score = 0f;

        float negativeMarks = test.getNegativeMarksPerQuestion() == null
                ? 0f
                : Math.abs(test.getNegativeMarksPerQuestion());

        for (TestAnswer answer : answers) {
            if (answer.getSelectedOption() == null) {
                unanswered++;
                answer.setIsCorrect(false);
                answer.setMarksAwarded(0f);
                continue;
            }

            Long questionId = answer.getQuestion().getId();
            Long correctOptionId = correctOptionByQuestionId.get(questionId);

            if (correctOptionId == null) {
                throw new ResourceNotFoundException("Correct option not found for question ");
            }

            boolean isCorrect = answer.getSelectedOption().getId().equals(correctOptionId);
            answer.setIsCorrect(isCorrect);

            if (isCorrect) {
                correct++;
                answer.setMarksAwarded(test.getMarksPerQuestion());
                score += test.getMarksPerQuestion();
            } else {
                incorrect++;
                answer.setMarksAwarded(-negativeMarks);
                score -= negativeMarks;
            }
        }

        unanswered += test.getTotalQuestions() - answers.size();

        return new TestScoreSummary(score, correct, incorrect, unanswered);
    }

    /* ----------------------------------------
     * Test Question Mappers
     * ---------------------------------------- */

    private TestInfoResponse toTestInfoResponse(TestAttempt attempt) {
        Test test = attempt.getTest();
        return new TestInfoResponse(
                attempt.getId(),
                test.getExamInfo().getId(),
                test.getName(),
                test.getTestType().getTestTypeName(),
                test.getExamInfo().getExamType(),
                test.getTotalQuestions(),
                test.getExamInfo().getTotalMarks(),
                test.getDurationMinutes(),
                test.getMarksPerQuestion(),
                test.getNegativeMarksPerQuestion(),
                test.getPassMarks()
        );
    }

    private List<SubjectQuestionsResponse> toSubjectResponses(List<Question> questions) {
        return groupBySubject(questions).entrySet().stream()
                .map(e -> new SubjectQuestionsResponse(
                        e.getKey().getId(),
                        e.getKey().getName(),
                        toChapterResponses(e.getValue())
                ))
                .toList();
    }

    private List<ChapterQuestionsResponse> toChapterResponses(Map<Chapter, List<Question>> byChapter) {
        return byChapter.entrySet().stream()
                .map(e -> {
                    Chapter chapter = e.getKey();
                    boolean noChapter = chapter == Chapter.NONE;
                    return new ChapterQuestionsResponse(
                            noChapter ? null : chapter.getId(),
                            noChapter ? null : chapter.getName(),
                            toPassageResponses(e.getValue())
                    );
                })
                .toList();
    }

    private Map<Subject, Map<Chapter, List<Question>>> groupBySubject(List<Question> questions) {
        return questions.stream()
                .collect(Collectors.groupingBy(
                        Question::getSubject,
                        LinkedHashMap::new,
                        Collectors.groupingBy(
                                q -> q.getChapter() != null ? q.getChapter() : Chapter.NONE,
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                ));
    }

    private List<PassageQuestionsResponse> toPassageResponses(List<Question> questions) {
        return questions.stream()
                .collect(Collectors.groupingBy(
                        q -> q.getPassage() != null ? q.getPassage().getId() : -1L,
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values().stream()
                .map(group -> {
                    Passage passage = group.get(0).getPassage();
                    return new PassageQuestionsResponse(
                            passage != null ? passage.getId() : null,
                            passage != null ? passage.getPassageText() : null,
                            passage != null ? passage.getImageUrl() : null,
                            group.stream().map(this::toQuestionResponse).toList()
                    );
                })
                .toList();
    }

    private QuestionResponse toQuestionResponse(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getQuestionText(),
                question.getImageUrl(),
                question.getDifficulty(),
                toQuestionOptionResponses(question)
        );
    }

    private List<QuestionOptionResponse> toQuestionOptionResponses(Question question) {
        return question.getOptions().stream()
                .map(option -> new QuestionOptionResponse(
                        option.getId(),
                        option.getOptionLabel(),
                        option.getOptionText(),
                        option.getImageUrl()
                ))
                .toList();
    }

    /* ----------------------------------------
     * Test Result Mappers
     * ---------------------------------------- */

    private TestResultSummaryResponse toTestResultSummaryResponse(
            TestAttempt attempt,
            Test test,
            boolean passed,
            int correct,
            int incorrect,
            int unanswered) {

        return new TestResultSummaryResponse(
                attempt.getId(),
                test.getId(),
                test.getName(),
                attempt.getScore(),
                test.getExamInfo().getTotalMarks(),
                test.getPassMarks(),
                passed,
                test.getTotalQuestions(),
                correct,
                incorrect,
                unanswered,
                attempt.getStartedAt(),
                attempt.getFinishedAt()
        );
    }

    private List<SubjectResultResponse> toSubjectResultResponses(
            List<Question> questions,
            Map<Long, List<QuestionOption>> optionsByQuestionId,
            Map<Long, TestAnswer> answerByQuestionId) {

        return groupBySubject(questions).entrySet().stream()
                .map(e -> new SubjectResultResponse(
                        e.getKey().getId(),
                        e.getKey().getName(),
                        toChapterResultResponses(
                                e.getValue(),
                                optionsByQuestionId,
                                answerByQuestionId
                        )
                ))
                .toList();
    }

    private List<ChapterResultResponse> toChapterResultResponses(
            Map<Chapter, List<Question>> byChapter,
            Map<Long, List<QuestionOption>> optionsByQuestionId,
            Map<Long, TestAnswer> answerByQuestionId) {

        return byChapter.entrySet().stream()
                .map(e -> {
                    Chapter chapter = e.getKey();
                    boolean noChapter = chapter == Chapter.NONE;

                    return new ChapterResultResponse(
                            noChapter ? null : chapter.getId(),
                            noChapter ? null : chapter.getName(),
                            toPassageResultResponses(
                                    e.getValue(),
                                    optionsByQuestionId,
                                    answerByQuestionId
                            )
                    );
                })
                .toList();
    }

    private List<PassageResultResponse> toPassageResultResponses(
            List<Question> questions,
            Map<Long, List<QuestionOption>> optionsByQuestionId,
            Map<Long, TestAnswer> answerByQuestionId) {

        return questions.stream()
                .collect(Collectors.groupingBy(
                        q -> q.getPassage() != null ? q.getPassage().getId() : -1L,
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values().stream()
                .map(group -> {

                    Passage passage = group.get(0).getPassage();

                    return new PassageResultResponse(
                            passage != null ? passage.getId() : null,
                            passage != null ? passage.getPassageText() : null,
                            passage != null ? passage.getImageUrl() : null,
                            group.stream()
                                    .map(question -> toQuestionResultResponse(
                                            question,
                                            optionsByQuestionId,
                                            answerByQuestionId
                                    ))
                                    .toList()
                    );
                })
                .toList();
    }

    private QuestionResultResponse toQuestionResultResponse(
            Question question,
            Map<Long, List<QuestionOption>> optionsByQuestionId,
            Map<Long, TestAnswer> answerByQuestionId) {

        TestAnswer answer = answerByQuestionId.get(question.getId());

        return new QuestionResultResponse(
                question.getId(),
                question.getQuestionText(),
                question.getImageUrl(),
                question.getDifficulty(),
                question.getExplanation(),
                answer != null ? answer.getIsCorrect() : null,
                answer != null ? answer.getMarksAwarded() : 0f,
                toQuestionOptionResultResponses(
                        optionsByQuestionId.getOrDefault(question.getId(), List.of()),
                        answer
                )
        );
    }

    private List<QuestionOptionResultResponse> toQuestionOptionResultResponses(
            List<QuestionOption> options,
            TestAnswer answer) {

        Long selectedOptionId =
                answer != null && answer.getSelectedOption() != null
                        ? answer.getSelectedOption().getId()
                        : null;

        return options.stream()
                .map(option -> new QuestionOptionResultResponse(
                        option.getId(),
                        option.getOptionLabel(),
                        option.getOptionText(),
                        option.getImageUrl(),
                        option.getId().equals(selectedOptionId),
                        option.getIsCorrect()
                ))
                .toList();
    }

}
