package com.example.pathshala_plus_backend.controller;

import com.example.pathshala_plus_backend.service.UnitService;
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
@RequestMapping("/api/v1/institutes")
public class UnitController {

    UnitService unitService;

    @GetMapping("/{instituteId}/units")
    public ResponseEntity<?> getAllUnitByInstitute(@PathVariable Long instituteId) {
        return ResponseEntity.ok(unitService.getAllUnitByInstitute(instituteId));
    }
}
