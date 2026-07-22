package com.example.pathshala_plus_backend.controller;

import com.example.pathshala_plus_backend.service.DashboardService;
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
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    DashboardService dashboardService;

    @GetMapping("/student")
    public ResponseEntity<?> getStudentDashboard() {
        return ResponseEntity.ok(dashboardService.getStudentDashboard());
    }
}