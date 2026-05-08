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
import com.example.pathshala_plus_backend.service.LanguageService;
import com.example.pathshala_plus_backend.service.RoleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${jwt.refresh-token-expiration-days}")
    private int refreshTokenExpirationDays;

    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();

        assert oAuth2User != null;
        String email     = oAuth2User.getAttribute("email");
        String name      = oAuth2User.getAttribute("name");
        String sub       = oAuth2User.getAttribute("sub");
        String avatarUrl = oAuth2User.getAttribute("picture");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            User newUser = UserMapper.fromOauth2UserInfo(name, email,languageService.findLanguageByCode("bn"),avatarUrl);
            user = userRepository.save(newUser);

            UserOauthAccount oauthAccount = OauthUserMapper.toUserOauthAccount(user, Provider.GOOGLE, oAuth2User.getAttribute("sub"));
            userOauthAccountRepository.save(oauthAccount);

            Role role = roleService.findRoleByName(SignupRole.STUDENT);
            UserRole userRole = UserRoleMapper.toUserRole(user, role);
            userRoleRepository.save(userRole);

        } else{
            boolean hasOauthAccount = userOauthAccountRepository.existsByUserIdAndProvider(user.getId(), Provider.GOOGLE);

            if(!hasOauthAccount){
                response.sendRedirect(frontendUrl+"/api/v1/auth/callback?error=email_conflict");
                return;
            }
        }

        String rawAccessToken  = jwtTokenProvider.generateAccessToken(authentication);
        String rawRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        String hashedRefresh   = DigestUtils.sha256Hex(rawRefreshToken);

        RefreshToken refreshToken = RefreshTokenMapper.toRefreshToken(user,hashedRefresh,Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS));
        refreshTokenRepository.save(refreshToken);

        Cookie accessTokenCookie = new Cookie("access_token", rawAccessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(180);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refresh_token", rawRefreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(refreshTokenExpirationDays * 24 * 60 * 60);
        response.addCookie(refreshTokenCookie);

        response.sendRedirect(frontendUrl + "/ap1/v1/auth/callback");
    }
}
