package com.example.pathshala_plus_backend.dto.request.user;

import jakarta.validation.constraints.NotNull;

public record UpdateAcademicsRequest(
        @NotNull(message = "Education level is required")
        Long educationLevelId
) {
}