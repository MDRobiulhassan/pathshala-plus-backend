package com.example.pathshala_plus_backend.dto.response.user;

import com.example.pathshala_plus_backend.enums.Provider;
import com.example.pathshala_plus_backend.enums.RoleName;

import java.time.Instant;
import java.util.List;

public record UserProfileResponse(
        Long id,
        String name,
        String email,
        String phone,
        String avatarUrl,
        String preferredLanguage,
        String educationLevel,
        Boolean isActive,
        Instant lastLoginAt,
        List<Provider> providers,
        List<RoleName> roles
) {
}
