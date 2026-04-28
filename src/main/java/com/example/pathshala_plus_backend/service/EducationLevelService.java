package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.education.EducationLevelResponse;

import java.util.List;

public interface EducationLevelService {
    List<EducationLevelResponse> getAllEducationLevels();
}
