package com.example.pathshala_plus_backend.controller;

import com.example.pathshala_plus_backend.service.ExamInfoService;
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
public class ExamInfoController {

    ExamInfoService examInfoService;

    @GetMapping("/exam-session/{examSessionId}/exam-info")
    public ResponseEntity<?> getExamInfoByExamSession(@PathVariable Long examSessionId) {
        return ResponseEntity.ok(examInfoService.getExamInfoByExamSession(examSessionId));
    }

    @GetMapping("/unit/{unitId}/exam-session/{examSessionId}/exam-info")
    public ResponseEntity<?> getExamInfoByUnitAndExamSession(@PathVariable Long unitId, @PathVariable Long examSessionId) {
        return ResponseEntity.ok(examInfoService.getExamInfoByUnitAndExamSession(unitId, examSessionId));
    }

    @GetMapping("/exam-session/{examSessionId}/shift/{shiftId}/exam-info")
    public ResponseEntity<?> getExamInfoByExamSessionAndShift(@PathVariable Long examSessionId, @PathVariable Long shiftId) {
        return ResponseEntity.ok(examInfoService.getExamInfoByExamSessionAndShift(examSessionId, shiftId));
    }
}
