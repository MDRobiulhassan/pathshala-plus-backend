package com.example.pathshala_plus_backend.dto.response.auth;

import com.example.pathshala_plus_backend.enums.RoleName;

public record SignupResponse(
        Long userId,
        RoleName role
) {
}
