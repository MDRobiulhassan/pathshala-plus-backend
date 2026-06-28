package com.example.pathshala_plus_backend.dto.response.auth;

import com.example.pathshala_plus_backend.enums.RoleName;

public record LoginResponse(
        Long id,
        RoleName role
) {
}
