package com.example.pathshala_plus_backend.service;

import jakarta.servlet.http.HttpServletResponse;

public interface CookieService {

    void addAuthCookies(HttpServletResponse response, String accessToken, String refreshToken);

    void addAccessCookie(HttpServletResponse response, String accessToken);

    void addRefreshCookie(HttpServletResponse response, String refreshToken);
}