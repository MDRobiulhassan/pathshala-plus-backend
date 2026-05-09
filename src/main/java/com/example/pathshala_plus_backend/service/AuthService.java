package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.request.auth.LoginRequest;
import com.example.pathshala_plus_backend.dto.request.auth.RefreshRequest;
import com.example.pathshala_plus_backend.dto.request.auth.SignupRequest;
import com.example.pathshala_plus_backend.dto.response.auth.LoginResponse;
import com.example.pathshala_plus_backend.dto.response.auth.RefreshResponse;
import com.example.pathshala_plus_backend.dto.response.auth.SignupResponse;

public interface AuthService {
    SignupResponse signup(SignupRequest signupRequest);
    LoginResponse login(LoginRequest loginRequest);
    RefreshResponse refreshAccessToken(RefreshRequest refreshRequest);
    void logout(RefreshRequest refreshRequest);
}
