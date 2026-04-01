package com.example.pathshala_plus_backend.dto.response.auth;

import com.example.pathshala_plus_backend.enums.RoleName;

public record SignupResponse(
        Long userId,
        String refreshToken,
        String accessToken,
        RoleName role
) {
}
