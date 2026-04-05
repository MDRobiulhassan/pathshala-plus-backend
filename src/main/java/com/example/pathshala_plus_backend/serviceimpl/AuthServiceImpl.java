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
import com.example.pathshala_plus_backend.repository.*;
import com.example.pathshala_plus_backend.security.CustomUserDetails;
import com.example.pathshala_plus_backend.security.JwtTokenProvider;
import com.example.pathshala_plus_backend.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

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

    @Value("${jwt.refresh-token-expiration-days}")
    private Integer refreshTokenExpirationDays;

    @Override
    public SignupResponse signup(SignupRequest signupRequest) {
        userRepository.findByEmail(signupRequest.email()).ifPresent(user -> {
            throw new DuplicateResourceException("User with email " + signupRequest.email() + " already exists");
        });

        if (!signupRequest.password().equals(signupRequest.confirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        Language language = languageRepository.findByCode(signupRequest.preferredLanguageCode())
                .orElseThrow(() -> {
                    log.error("Language not found for code: '{}'", signupRequest.preferredLanguageCode());
                    return new ResourceNotFoundException("Invalid language: " + signupRequest.preferredLanguageCode());
                });

        if (signupRequest.educationLevelId() != null) {
            educationLevelRepository.findById(signupRequest.educationLevelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Education level not found"));
        }

        EducationLevel educationLevel = null;
        if (signupRequest.educationLevelId() != null) {
            educationLevel = educationLevelRepository.findById(signupRequest.educationLevelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Education level not found"));
        }

        RoleName roleName = RoleName.valueOf(signupRequest.role().name());
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        User user = UserMapper.fromSignupRequest(signupRequest, language, educationLevel, passwordEncoder);
        User savedUser = userRepository.save(user);

        UserRole userRole = UserRoleMapper.userRole(savedUser, role);
        userRoleRepository.save(userRole);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signupRequest.email(),
                        signupRequest.password()
                )
        );

        String rawAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        String rawRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

//        String hashedRefreshToken = passwordEncoder.encode(rawRefreshToken);
        RefreshToken refreshTokenEntity = RefreshTokenMapper.toRefreshToken(savedUser, rawRefreshToken, Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS));
        refreshTokenRepository.save(refreshTokenEntity);

        log.info("User registered successfully: {}", savedUser.getEmail());
        return new SignupResponse(savedUser.getId(), rawRefreshToken, rawAccessToken, roleName);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequest.email()));

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
        RoleName role = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).filter(Objects::nonNull)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> RoleName.valueOf(a.substring(5)))
                .findFirst()
                .orElseThrow();

        return new LoginResponse(userDetails.getId(), rawRefreshToken, rawAccessToken, role);
    }
}
