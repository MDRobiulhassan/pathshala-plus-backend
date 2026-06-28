package com.example.pathshala_plus_backend.security;

import com.example.pathshala_plus_backend.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationCookieWriter {

    private final CookieUtil cookieUtil;

    public void addAuthCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        addAccessCookie(response, accessToken);
        addRefreshCookie(response, refreshToken);
    }

    public void addAccessCookie(HttpServletResponse response, String accessToken) {
        ResponseCookie accessCookie = cookieUtil.createAccessCookie(accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    }

    public void addRefreshCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie refreshCookie = cookieUtil.createRefreshCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    public void clearAuthCookies(HttpServletResponse response) {
        ResponseCookie accessCookie = cookieUtil.clearAccessCookie();
        ResponseCookie refreshCookie = cookieUtil.clearRefreshCookie();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}