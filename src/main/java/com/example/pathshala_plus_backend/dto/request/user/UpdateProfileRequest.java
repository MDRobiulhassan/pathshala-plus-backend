package com.example.pathshala_plus_backend.dto.request.user;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @Size(max = 20, message = "Phone must be at most 20 characters")
        String phone,

        String preferredLanguageCode,

        Long educationLevelId
) {
}