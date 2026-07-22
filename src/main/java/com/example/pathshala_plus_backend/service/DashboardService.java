package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.dashboard.student.StudentDashboardResponse;

public interface DashboardService {
    StudentDashboardResponse getStudentDashboard();
}
