package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.request.test.SaveAnswersRequest;
import com.example.pathshala_plus_backend.dto.response.test.TestAttemptResponse;
import com.example.pathshala_plus_backend.dto.response.test.TestTypeResponse;
import com.example.pathshala_plus_backend.dto.response.test.testhistory.TestAttemptHistory;
import com.example.pathshala_plus_backend.dto.response.test.testquestion.TestQuestionsResponse;
import com.example.pathshala_plus_backend.dto.response.test.testresult.TestResultResponse;

import java.util.List;

public interface TestService {
    List<TestTypeResponse> getAllTestTypes();

    List<TestTypeResponse> getQuestionBankTestTypes();

    TestAttemptResponse createOrResumeTestAttempt(Long testId);

    TestQuestionsResponse getTestAttemptWithQuestions(Long attemptId);

    void saveProgress(Long attemptId, SaveAnswersRequest request);

    void submitTestAttempt(Long attemptId);

    TestResultResponse getTestAttemptResultById(Long attemptId);

    TestAttemptHistory getUserTestAttempts();
}
