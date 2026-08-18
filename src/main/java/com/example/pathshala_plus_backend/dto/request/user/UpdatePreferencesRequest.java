package com.example.pathshala_plus_backend.dto.request.user;

import jakarta.validation.constraints.NotBlank;

public record UpdatePreferencesRequest(
        @NotBlank(message = "Preferred language is required")
        String preferredLanguageCode
) {
}