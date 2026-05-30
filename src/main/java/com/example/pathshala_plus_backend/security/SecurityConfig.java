package com.example.pathshala_plus_backend.security;

import com.example.pathshala_plus_backend.exceptions.CustomAccessDeniedHandler;
import com.example.pathshala_plus_backend.exceptions.CustomAuthenticationEntryPoint;
import com.example.pathshala_plus_backend.handler.OAuth2FailureHandler;
import com.example.pathshala_plus_backend.handler.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler OAuth2SuccessHandler;
    private final OAuth2FailureHandler OAuth2FailureHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api/v1/languages/**",
                                "/api/v1/education/levels/**",
                                "/api/v1/education-levels",
                                "/api/v1/languages",
                                "/oauth2/**",
                                "/api/v1/auth/oauth2/**"
                        ).permitAll()
                        .requestMatchers(
                                "/api/v1/auth/me",
                                "/api/v1/auth/logout"
                        ).authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(
                        oauth2config -> oauth2config
                                .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/oauth2"))
                                .redirectionEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/oauth2/callback/*"))
                                .userInfoEndpoint(userInfo -> userInfo.userService(new DefaultOAuth2UserService()))
                                .failureHandler(OAuth2FailureHandler)
                                .successHandler(OAuth2SuccessHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
