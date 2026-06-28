package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.Mapper.RefreshTokenMapper;
import com.example.pathshala_plus_backend.Mapper.UserMapper;
import com.example.pathshala_plus_backend.Mapper.UserRoleMapper;
import com.example.pathshala_plus_backend.dto.request.auth.LoginRequest;
import com.example.pathshala_plus_backend.dto.request.auth.SignupRequest;
import com.example.pathshala_plus_backend.dto.response.auth.LoginResponse;
import com.example.pathshala_plus_backend.dto.response.auth.SignupResponse;
import com.example.pathshala_plus_backend.entity.*;
import com.example.pathshala_plus_backend.enums.RoleName;
import com.example.pathshala_plus_backend.exceptions.BadRequestException;
import com.example.pathshala_plus_backend.exceptions.DuplicateResourceException;
import com.example.pathshala_plus_backend.exceptions.ResourceNotFoundException;
import com.example.pathshala_plus_backend.exceptions.UnauthorizedException;
import com.example.pathshala_plus_backend.repository.*;
import com.example.pathshala_plus_backend.security.AuthenticationCookieWriter;
import com.example.pathshala_plus_backend.security.CustomUserDetails;
import com.example.pathshala_plus_backend.security.JwtTokenProvider;
import com.example.pathshala_plus_backend.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final LanguageRepository languageRepository;
    private final EducationLevelRepository educationLevelRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final AuthenticationCookieWriter authenticationCookieWriter;

    @Value("${jwt.refresh-token-expiration-days}")
    private Integer refreshTokenExpirationDays;

    @Override
    public SignupResponse signup(SignupRequest signupRequest, HttpServletResponse response) {
        userRepository.findByEmail(signupRequest.email()).ifPresent(user -> {
            throw new DuplicateResourceException("User with email " + signupRequest.email() + " already exists");
        });

        if (!signupRequest.password().equals(signupRequest.confirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        if (!signupRequest.name().matches("^[A-Za-z\\s]{3,100}$")) {
            throw new BadRequestException("Name must contain only letters and spaces, 3–100 characters");
        }

        if (!signupRequest.password().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,50}$")) {
            throw new BadRequestException("Password must contain uppercase, lowercase, number, and special character");
        }

        if (signupRequest.role() == null || signupRequest.role().name().trim().isEmpty()) {
            throw new BadRequestException("Invalid role value. Allowed values: STUDENT, TEACHER, CONTENT_ADMIN, SUPER_ADMIN");
        }


        Language language = languageRepository.findByCode(signupRequest.preferredLanguageCode())
                .orElseThrow(() -> {
                    log.error("Language not found for code: '{}'", signupRequest.preferredLanguageCode());
                    return new ResourceNotFoundException("Invalid language: " + signupRequest.preferredLanguageCode());
                });

        EducationLevel educationLevel = educationLevelRepository.findById(signupRequest.educationLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Education level not found"));


        RoleName roleName = RoleName.valueOf(signupRequest.role().name());
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        User user = UserMapper.fromSignupRequest(signupRequest, language, educationLevel, passwordEncoder);
        User savedUser = userRepository.save(user);

        UserRole userRole = UserRoleMapper.toUserRole(savedUser, role);
        userRoleRepository.save(userRole);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signupRequest.email(),
                        signupRequest.password()
                )
        );

        String rawAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        String rawRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        RefreshToken refreshTokenEntity = RefreshTokenMapper.toRefreshToken(savedUser, rawRefreshToken, Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS));
        refreshTokenRepository.save(refreshTokenEntity);

        log.info("User registered successfully: {}", savedUser.getEmail());
        authenticationCookieWriter.addAuthCookies(response, rawAccessToken, rawRefreshToken);
        return new SignupResponse(savedUser.getId(), roleName);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequest.email()));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String rawAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        String rawRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        RefreshToken refreshTokenEntity = RefreshTokenMapper.toRefreshToken(user, rawRefreshToken, Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS));
        refreshTokenRepository.save(refreshTokenEntity);

        assert userDetails != null;
        RoleName role = user.getUserRoles().get(0).getRole().getName();

        authenticationCookieWriter.addAuthCookies(response, rawAccessToken, rawRefreshToken);
        return new LoginResponse(userDetails.getId(), role);
    }

    @Override
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshToken(request);
        if (refreshToken == null) {
            throw new UnauthorizedException("Refresh token missing");
        }

        String username = jwtTokenProvider.extractUsername(refreshToken);
        if (username == null) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!jwtTokenProvider.verifyRefreshToken(refreshToken, user)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        authenticationCookieWriter.addAccessCookie(response, jwtTokenProvider.generateAccessToken(authentication));
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshToken(request);

        if (refreshToken == null) {
            throw new UnauthorizedException("Refresh token missing");
        }

        String username = jwtTokenProvider.extractUsername(refreshToken);

        if (username == null) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        jwtTokenProvider.invalidateRefreshTokens(user);

        authenticationCookieWriter.clearAuthCookies(response);
    }

    private String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("refresh_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
