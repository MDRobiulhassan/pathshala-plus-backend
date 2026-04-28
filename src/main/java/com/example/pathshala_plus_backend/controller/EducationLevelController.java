package com.example.pathshala_plus_backend.controller;

import com.example.pathshala_plus_backend.service.EducationLevelService;
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
@RequestMapping("/api/v1/education-levels")
public class EducationLevelController {

    EducationLevelService educationLevelService;

    @GetMapping
    public ResponseEntity<?> getEducationLevels() {
        return ResponseEntity.ok(educationLevelService.getAllEducationLevels());
    }
}
