package com.example.pathshala_plus_backend.dto.request.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(min = 2, max = 100)
        String name,

        @Pattern(regexp = "^(\\+8801|01)[3-9][0-9]{8}$")
        String phone,

        String preferredLanguageCode,
        Long educationLevelId
) {
    public UpdateProfileRequest {
        phone = phone == null ? null : phone.trim();
    }
}