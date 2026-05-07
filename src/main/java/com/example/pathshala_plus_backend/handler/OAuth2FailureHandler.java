package com.example.pathshala_plus_backend.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationFailure(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String reason = "auth_failed";
        if (exception.getMessage() != null && exception.getMessage().contains("email_conflict")) {
            reason = "email_conflict";
        }
        response.sendRedirect(frontendUrl + "/api/v1/auth/callback?error=" + reason);
    }
}