package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.request.user.ChangePasswordRequest;
import com.example.pathshala_plus_backend.dto.response.user.AvatarResponse;
import com.example.pathshala_plus_backend.dto.response.user.UserProfileResponse;
import com.example.pathshala_plus_backend.dto.request.user.UpdateProfileRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserProfileResponse getUserProfile();
    UserProfileResponse updateProfile(UpdateProfileRequest request);
    AvatarResponse updateAvatar(MultipartFile file);
    void changePassword(ChangePasswordRequest request);
}
