package com.example.pathshala_plus_backend.controller;

import com.example.pathshala_plus_backend.service.ShiftService;
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
public class ShiftController {

    ShiftService shiftService;

    @GetMapping("/units/{unitId}/exam-sessions/{examSessionId}/shifts")
    public ResponseEntity<?> getAllShiftsByUnitAndExamSession(@PathVariable Long unitId, @PathVariable Long examSessionId) {
        return ResponseEntity.ok(shiftService.getAllShiftByUnitAndExamSession(unitId, examSessionId));
    }
}
