package com.example.pathshala_plus_backend.handler;

import com.example.pathshala_plus_backend.Mapper.OauthUserMapper;
import com.example.pathshala_plus_backend.Mapper.RefreshTokenMapper;
import com.example.pathshala_plus_backend.Mapper.UserMapper;
import com.example.pathshala_plus_backend.Mapper.UserRoleMapper;
import com.example.pathshala_plus_backend.entity.*;
import com.example.pathshala_plus_backend.enums.Provider;
import com.example.pathshala_plus_backend.enums.SignupRole;
import com.example.pathshala_plus_backend.repository.RefreshTokenRepository;
import com.example.pathshala_plus_backend.repository.UserOauthAccountRepository;
import com.example.pathshala_plus_backend.repository.UserRepository;
import com.example.pathshala_plus_backend.repository.UserRoleRepository;
import com.example.pathshala_plus_backend.security.JwtTokenProvider;
import com.example.pathshala_plus_backend.service.CookieService;
import com.example.pathshala_plus_backend.service.LanguageService;
import com.example.pathshala_plus_backend.service.RoleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final UserOauthAccountRepository userOauthAccountRepository;
    private final LanguageService languageService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RoleService roleService;
    private final UserRoleRepository userRoleRepository;
    private final CookieService cookieService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${jwt.refresh-token-expiration-days}")
    private int refreshTokenExpirationDays;

    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();

        if (oAuth2User == null) {
            throw new IllegalStateException("OAuth2 user is null");
        }

        String email = (String) Optional.ofNullable(oAuth2User.getAttribute("email"))
                .orElseThrow(() -> new IllegalStateException("Email not provided by OAuth provider"));

        String name = oAuth2User.getAttribute("name");
        String sub = oAuth2User.getAttribute("sub");
        String avatarUrl = oAuth2User.getAttribute("picture");

        if (sub == null) {
            throw new IllegalStateException("OAuth provider id (sub) is missing");
        }

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = UserMapper.fromOauth2UserInfo(
                    name,
                    email,
                    languageService.findLanguageByCode("bn"),
                    avatarUrl
            );
            user = userRepository.save(user);

            Role role = roleService.findRoleByName(SignupRole.STUDENT);
            UserRole userRole = UserRoleMapper.toUserRole(user, role);
            userRoleRepository.save(userRole);
        }

        boolean hasOauthAccount = userOauthAccountRepository.existsByUserIdAndProvider(user.getId(), Provider.GOOGLE);

        if (!hasOauthAccount) {
            UserOauthAccount oauthAccount = OauthUserMapper.toUserOauthAccount(user, Provider.GOOGLE, sub);
            userOauthAccountRepository.save(oauthAccount);
        }

        String rawAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        String rawRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        String hashedRefresh = DigestUtils.sha256Hex(rawRefreshToken);

        RefreshToken refreshToken = RefreshTokenMapper.toRefreshToken(
                user,
                hashedRefresh,
                Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS)
        );

        refreshTokenRepository.save(refreshToken);

        cookieService.addAuthCookies(response, rawAccessToken, rawRefreshToken);

        response.sendRedirect(frontendUrl + "/auth/callback");
    }
}
