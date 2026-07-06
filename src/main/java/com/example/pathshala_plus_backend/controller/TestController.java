package com.example.pathshala_plus_backend.controller;

import com.example.pathshala_plus_backend.dto.request.test.SaveAnswersRequest;
import com.example.pathshala_plus_backend.service.TestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/tests")
public class TestController {
    TestService testService;

    @GetMapping("/types")
    public ResponseEntity<?> getAllTestTypes() {
        return ResponseEntity.ok(testService.getAllTestTypes());
    }

    @GetMapping("/types/question-bank")
    public ResponseEntity<?> getQuestionBankTestTypes() {
        return ResponseEntity.ok(testService.getQuestionBankTestTypes());
    }

    @PostMapping("/{testId}/attempts")
    public ResponseEntity<?> createOrResumeTestAttempt(@PathVariable Long testId) {
        return ResponseEntity.ok(testService.createOrResumeTestAttempt(testId));
    }

    @GetMapping("/attempts/{attemptId}")
    public ResponseEntity<?> getTestAttempt(@PathVariable Long attemptId) {
        return ResponseEntity.ok(testService.getTestAttemptWithQuestions(attemptId));
    }

    @PatchMapping("/attempts/{attemptId}/answers")
    public ResponseEntity<?> saveProgress(@PathVariable Long attemptId, @RequestBody SaveAnswersRequest request) {
        testService.saveProgress(attemptId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/attempts/{attemptId}/submit")
    public ResponseEntity<?> submitTestAttempt(@PathVariable Long attemptId) {
        testService.submitTestAttempt(attemptId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/attempts/{attemptId}/result")
    public ResponseEntity<?> getTestAttemptResult(@PathVariable Long attemptId) {
        return ResponseEntity.ok(testService.getTestAttemptResultById(attemptId));
    }
}
