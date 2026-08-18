package com.example.pathshala_plus_backend.controller;

import com.example.pathshala_plus_backend.dto.request.user.UpdateProfileRequest;
import com.example.pathshala_plus_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.pathshala_plus_backend.dto.request.user.UpdatePersonalDetailsRequest;
import com.example.pathshala_plus_backend.dto.request.user.UpdateAcademicsRequest;
import com.example.pathshala_plus_backend.dto.request.user.UpdatePreferencesRequest;
import com.example.pathshala_plus_backend.dto.request.user.ChangePasswordRequest;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/users")
public class UserController {

    UserService userService;

    @PatchMapping("/me")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }

    @PatchMapping("/me/personal-details")
    public ResponseEntity<?> updatePersonalDetails(@Valid @RequestBody UpdatePersonalDetailsRequest request) {
        return ResponseEntity.ok(userService.updatePersonalDetails(request));
    }

    @PatchMapping("/me/academics")
    public ResponseEntity<?> updateAcademics(@Valid @RequestBody UpdateAcademicsRequest request) {
        return ResponseEntity.ok(userService.updateAcademics(request));
    }

    @PatchMapping("/me/preferences")
    public ResponseEntity<?> updatePreferences(@Valid @RequestBody UpdatePreferencesRequest request) {
        return ResponseEntity.ok(userService.updatePreferences(request));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok().build();
    }
}