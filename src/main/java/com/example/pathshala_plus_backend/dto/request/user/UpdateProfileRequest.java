package com.example.pathshala_plus_backend.dto.request.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @Size(max = 20, message = "Phone must be at most 20 characters")
        String phone,

        String preferredLanguageCode,

        Long educationLevelId,

        String avatarUrl,

        String currentPassword,

        @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$",
                message = "Password must contain uppercase, lowercase, number, and special character"
        )
        String newPassword,

        String confirmNewPassword
) {
}