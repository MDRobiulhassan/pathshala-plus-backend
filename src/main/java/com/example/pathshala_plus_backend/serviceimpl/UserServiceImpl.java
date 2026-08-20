package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.dto.request.user.ChangePasswordRequest;
import com.example.pathshala_plus_backend.dto.request.user.UpdateProfileRequest;
import com.example.pathshala_plus_backend.dto.response.user.AvatarResponse;
import com.example.pathshala_plus_backend.dto.response.user.UserProfileResponse;
import com.example.pathshala_plus_backend.entity.EducationLevel;
import com.example.pathshala_plus_backend.entity.Language;
import com.example.pathshala_plus_backend.entity.User;
import com.example.pathshala_plus_backend.entity.UserOauthAccount;
import com.example.pathshala_plus_backend.enums.Provider;
import com.example.pathshala_plus_backend.enums.RoleName;
import com.example.pathshala_plus_backend.exceptions.InvalidRequestException;
import com.example.pathshala_plus_backend.exceptions.ResourceNotFoundException;
import com.example.pathshala_plus_backend.repository.EducationLevelRepository;
import com.example.pathshala_plus_backend.repository.LanguageRepository;
import com.example.pathshala_plus_backend.repository.UserOauthAccountRepository;
import com.example.pathshala_plus_backend.repository.UserRepository;
import com.example.pathshala_plus_backend.security.AuthUtil;
import com.example.pathshala_plus_backend.security.CustomUserDetails;
import com.example.pathshala_plus_backend.security.JwtTokenProvider;
import com.example.pathshala_plus_backend.service.UserService;
import com.example.pathshala_plus_backend.supabase.SupabaseStorageService;
import com.example.pathshala_plus_backend.util.ImageProcessingUtil;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
    SupabaseStorageService supabaseStorageService;
    JwtTokenProvider jwtTokenProvider;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithRolesAndPermissions(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new CustomUserDetails(user);
    }

    @Override
    public UserProfileResponse getUserProfile() {
        User user = findCurrentUser();
        return buildUserProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
        User user = findCurrentUser();
        applyProfileUpdates(request, user);
        User saved = userRepository.save(user);
        return buildUserProfileResponse(saved);
    }

    @Override
    public AvatarResponse updateAvatar(MultipartFile file) {
        User user = findCurrentUser();
        byte[] resized = resizeAvatar(file);

        String path = "avatars/" + user.getId() + "/" + UUID.randomUUID() + ".jpg";
        String publicUrl = supabaseStorageService.upload(path, resized, "image/jpeg");

        String oldAvatarUrl = user.getAvatarUrl();
        user.setAvatarUrl(publicUrl);
        userRepository.save(user);

        if (oldAvatarUrl != null) {
            supabaseStorageService.deleteByPublicUrl(oldAvatarUrl);
        }

        return new AvatarResponse(publicUrl);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User user = findCurrentUser();
        validateChangePasswordRequest(request, user);
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        jwtTokenProvider.invalidateRefreshTokens(user);
    }


    // -----------------------------
    // Private Helper Methods
    // -----------------------------
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

    private User findCurrentUser() {
        return userRepository.findById(authUtil.getCurrentUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private byte[] resizeAvatar(MultipartFile file) {
        try {
            return ImageProcessingUtil.resizeAvatar(file);
        } catch (IOException e) {
            throw new InvalidRequestException("Could not process image file");
        }
    }

    private void applyProfileUpdates(UpdateProfileRequest request, User user) {
        if (request.name() != null) {
            user.setName(request.name());
        }

        if (request.phone() != null) {
            user.setPhone(request.phone());
        }

        if (request.preferredLanguageCode() != null) {
            Language language = languageRepository.findByCode(request.preferredLanguageCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Unsupported language code"));
            user.setPreferredLanguage(language);
        }

        if (request.educationLevelId() != null) {
            EducationLevel educationLevel = educationLevelRepository.findById(request.educationLevelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Education level not found"));
            user.setEducationLevel(educationLevel);
        }
    }

    private void validateChangePasswordRequest(ChangePasswordRequest request, User user) {
        if (user.getPassword() == null) {
            throw new InvalidRequestException(
                    "This account was created with Google sign-in and has no password set. "
                            + "Use 'forgot password' to set one.");
        }

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new InvalidRequestException("Current password is incorrect");
        }

        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new InvalidRequestException("New password and confirm password do not match");
        }
    }
}