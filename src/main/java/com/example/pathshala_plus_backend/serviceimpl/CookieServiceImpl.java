package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.service.CookieService;
import com.example.pathshala_plus_backend.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieServiceImpl implements CookieService {

    private final CookieUtil cookieUtil;

    @Override
    public void addAuthCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        addAccessCookie(response, accessToken);
        addRefreshCookie(response, refreshToken);
    }

    @Override
    public void addAccessCookie(HttpServletResponse response, String accessToken) {
        ResponseCookie accessCookie = cookieUtil.createAccessCookie(accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    }

    @Override
    public void addRefreshCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie refreshCookie = cookieUtil.createRefreshCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}