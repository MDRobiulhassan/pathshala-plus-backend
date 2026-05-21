package com.example.pathshala_plus_backend.dto.response.education;

import com.example.pathshala_plus_backend.enums.Department;

public record EducationLevelResponse(
        Long id,
        String name,
        Department department
) {
}
