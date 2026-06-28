package com.example.pathshala_plus_backend.controller;

import com.example.pathshala_plus_backend.service.TestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
