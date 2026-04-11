package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.request.auth.LoginRequest;
import com.example.pathshala_plus_backend.dto.request.auth.SignupRequest;
import com.example.pathshala_plus_backend.dto.response.auth.LoginResponse;
import com.example.pathshala_plus_backend.dto.response.auth.RefreshTokenResponse;
import com.example.pathshala_plus_backend.dto.response.auth.SignupResponse;

public interface AuthService {
    SignupResponse signup(SignupRequest signupRequest);
    LoginResponse login(LoginRequest loginRequest);
    RefreshTokenResponse refreshAccessToken(String refreshToken);

}
