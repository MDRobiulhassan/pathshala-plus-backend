package com.example.pathshala_plus_backend.dto.request.user;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(min = 2, max = 100)
        String name,

        @Size(max = 20)
        String phone,

        String preferredLanguageCode,
        Long educationLevelId
) {
}