package com.example.pathshala_plus_backend.controller;

import com.example.pathshala_plus_backend.service.QuestionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/")
public class QuestionController {

    QuestionService questionService;

    @GetMapping("/exam-info/{examInfoId}/questions")
    public ResponseEntity<?> getQuestionsByExamInfo(@PathVariable Long examInfoId) {
        return ResponseEntity.ok(questionService.getQuestionsByExamInfo(examInfoId));
    }

    @GetMapping("questions/subject/{subjectId}")
    public ResponseEntity<?> getQuestionsBySubject(@PathVariable Long subjectId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(questionService.getQuestionsBySubject(subjectId, page, size));
    }
}
