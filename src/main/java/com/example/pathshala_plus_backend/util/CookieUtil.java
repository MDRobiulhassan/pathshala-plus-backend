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

    @Value("${app.cookie-secure}")
    private boolean cookieSecure;

    @Value("${app.cookie-same-site}")
    private String cookieSameSite;

    public ResponseCookie createAccessCookie(String token) {
        return ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(accessTokenExpirationMinutes * 60L)
                .sameSite(cookieSameSite)
                .build();
    }

    public ResponseCookie createRefreshCookie(String token) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(refreshTokenExpirationDays * 24L * 60L * 60L)
                .sameSite(cookieSameSite)
                .build();
    }

    public ResponseCookie clearAccessCookie() {
        return ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .sameSite(cookieSameSite)
                .maxAge(0)
                .build();
    }

    public ResponseCookie clearRefreshCookie() {
        return ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .sameSite(cookieSameSite)
                .maxAge(0)
                .build();
    }
}