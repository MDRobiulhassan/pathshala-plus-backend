package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.request.auth.LoginRequest;
import com.example.pathshala_plus_backend.dto.request.auth.SignupRequest;
import com.example.pathshala_plus_backend.dto.response.auth.LoginResponse;
import com.example.pathshala_plus_backend.dto.response.auth.SignupResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    SignupResponse signup(SignupRequest signupRequest, HttpServletResponse response);
    LoginResponse login(LoginRequest loginRequest,HttpServletResponse response);
    void refreshAccessToken(HttpServletRequest request, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
}
