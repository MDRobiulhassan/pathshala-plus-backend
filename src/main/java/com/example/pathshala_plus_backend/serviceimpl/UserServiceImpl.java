package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.dto.response.user.UserProfileResponse;
import com.example.pathshala_plus_backend.entity.User;
import com.example.pathshala_plus_backend.entity.UserOauthAccount;
import com.example.pathshala_plus_backend.enums.Provider;
import com.example.pathshala_plus_backend.enums.RoleName;
import com.example.pathshala_plus_backend.repository.UserOauthAccountRepository;
import com.example.pathshala_plus_backend.repository.UserRepository;
import com.example.pathshala_plus_backend.security.AuthUtil;
import com.example.pathshala_plus_backend.security.CustomUserDetails;
import com.example.pathshala_plus_backend.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.pathshala_plus_backend.dto.request.user.UpdateProfileRequest;
import com.example.pathshala_plus_backend.entity.EducationLevel;
import com.example.pathshala_plus_backend.entity.Language;
import com.example.pathshala_plus_backend.repository.EducationLevelRepository;
import com.example.pathshala_plus_backend.repository.LanguageRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.pathshala_plus_backend.exceptions.BadRequestException;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    UserRepository userRepository;
    LanguageRepository languageRepository;
    EducationLevelRepository educationLevelRepository;
    UserOauthAccountRepository userOauthAccountRepository;
    AuthUtil authUtil;
    PasswordEncoder passwordEncoder;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithRolesAndPermissions(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new CustomUserDetails(user);
    }

    @Override
    public UserProfileResponse getUserProfile() {
        Long userId = authUtil.getCurrentUserId();
        User user = userRepository.findByIdWithDetails(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        return buildUserProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
        Long userId = authUtil.getCurrentUserId();
        User user = userRepository.findByIdWithDetails(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (request.name() != null) {
            user.setName(request.name());
        }
        if (request.phone() != null) {
            user.setPhone(request.phone());
        }
        if (request.avatarUrl() != null) {
            user.setAvatarUrl(request.avatarUrl());
        }
        if (request.preferredLanguageCode() != null) {
            user.setPreferredLanguage(resolveLanguage(request.preferredLanguageCode()));
        }
        if (request.educationLevelId() != null) {
            user.setEducationLevel(resolveEducationLevel(request.educationLevelId()));
        }
        if (request.newPassword() != null) {
            updatePassword(user, request);
        }

        userRepository.save(user);
        return buildUserProfileResponse(user);
    }

    //Helper methods

    private UserProfileResponse buildUserProfileResponse(User user) {
        List<Provider> providers = userOauthAccountRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(UserOauthAccount::getProvider)
                .toList();

        List<RoleName> roles = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getName())
                .toList();

        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAvatarUrl(),
                user.getPreferredLanguage().getName(),
                user.getEducationLevel() != null ? user.getEducationLevel().getName() : null,
                user.getEducationLevel() != null ? user.getEducationLevel().getDepartment() : null,
                user.getIsActive(),
                user.getLastLoginAt(),
                providers,
                roles
        );
    }

    private Language resolveLanguage(String code) {
        return languageRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Language not found"));
    }

    private EducationLevel resolveEducationLevel(Long educationLevelId) {
        return educationLevelRepository.findById(educationLevelId)
                .orElseThrow(() -> new IllegalArgumentException("Education level not found"));
    }

    private void updatePassword(User user, UpdateProfileRequest request) {
        if (request.currentPassword() == null) {
            throw new BadRequestException("Current password is required to set a new password");
        }
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
    }
}