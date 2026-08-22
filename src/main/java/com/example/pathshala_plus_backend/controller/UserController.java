package com.example.pathshala_plus_backend.controller;

import com.example.pathshala_plus_backend.dto.request.user.ChangePasswordRequest;
import com.example.pathshala_plus_backend.dto.request.user.UpdateProfileRequest;
import com.example.pathshala_plus_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/users")
public class UserController {

    UserService userService;

    @PatchMapping("/me")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok().body(userService.updateProfile(request));
    }

    @PostMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateAvatar(file));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok().build();
    }
}