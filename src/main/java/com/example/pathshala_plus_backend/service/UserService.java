package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.user.UserProfileResponse;
import com.example.pathshala_plus_backend.dto.request.user.UpdateProfileRequest;
import com.example.pathshala_plus_backend.dto.request.user.UpdatePersonalDetailsRequest;
import com.example.pathshala_plus_backend.dto.request.user.UpdateAcademicsRequest;
import com.example.pathshala_plus_backend.dto.request.user.UpdatePreferencesRequest;
import com.example.pathshala_plus_backend.dto.request.user.ChangePasswordRequest;

public interface UserService {
    UserProfileResponse getUserProfile();
    UserProfileResponse updateProfile(UpdateProfileRequest request);
    UserProfileResponse updatePersonalDetails(UpdatePersonalDetailsRequest request);
    UserProfileResponse updateAcademics(UpdateAcademicsRequest request);
    UserProfileResponse updatePreferences(UpdatePreferencesRequest request);
    void changePassword(ChangePasswordRequest request);

}
