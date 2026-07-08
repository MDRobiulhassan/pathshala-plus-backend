package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.user.UserProfileResponse;
import com.example.pathshala_plus_backend.dto.request.user.UpdateProfileRequest;

public interface UserService {
    UserProfileResponse getUserProfile();
    UserProfileResponse updateProfile(UpdateProfileRequest request);
}
