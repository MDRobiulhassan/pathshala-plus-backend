package com.example.pathshala_plus_backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    @Value("${jwt.refresh-token-expiration-days}")
    private int refreshTokenExpirationDays;

    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    public ResponseCookie createAccessCookie(String token) {
        return ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(false) // true in production
                .path("/")
                .maxAge(accessTokenExpirationMinutes * 60L)
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie createRefreshCookie(String token) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(false) // true in production
                .path("/") // /api/v1/auth/refresh-token on production
                .maxAge(refreshTokenExpirationDays * 24L * 60L * 60L)
                .sameSite("Lax")
                .build();
    }
}