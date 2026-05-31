package com.example.pathshala_plus_backend.controller;

import com.example.pathshala_plus_backend.service.ExamSessionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1")
public class ExamSessionController {

    ExamSessionService examSessionService;

    @GetMapping("/institutes/{instituteId}/units/{unitId}/exam-sessions")
    public ResponseEntity<?> getAllExamSessionsByUnitAndInstitute(@PathVariable Long instituteId, @PathVariable Long unitId) {
        return ResponseEntity.ok(examSessionService.getAllExamSessionByInstituteAndUnit(instituteId, unitId));
    }

    @GetMapping("/institutes/{instituteId}/exam-sessions")
    public ResponseEntity<?> getAllExamSessionsByInstitute(@PathVariable Long instituteId) {
        return ResponseEntity.ok(examSessionService.getAllExamSessionByInstitute(instituteId));
    }
}
