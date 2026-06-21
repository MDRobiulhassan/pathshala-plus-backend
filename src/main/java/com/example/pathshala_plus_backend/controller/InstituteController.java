package com.example.pathshala_plus_backend.controller;

import com.example.pathshala_plus_backend.service.InstituteService;
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
public class InstituteController {

    InstituteService instituteService;

    @GetMapping("/institutes")
    public ResponseEntity<?> getAllInstitutes() {
        return ResponseEntity.ok(instituteService.getAllInstitutes());
    }

    @GetMapping("/{educationLevelId}/institutes")
    public ResponseEntity<?> getInstitutesByEducationLevel(@PathVariable Long educationLevelId) {
        return ResponseEntity.ok(instituteService.getInstitutesByEducationLevel(educationLevelId));
    }
}
