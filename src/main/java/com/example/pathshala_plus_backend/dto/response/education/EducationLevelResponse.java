package com.example.pathshala_plus_backend.dto.response.education;

import com.example.pathshala_plus_backend.enums.Department;

import java.util.List;

public record EducationLevelResponse(
        String name,
        List<EducationLevelVariantResponse> variants
) {
}
