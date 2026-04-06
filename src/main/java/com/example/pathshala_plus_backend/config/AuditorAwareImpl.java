package com.example.pathshala_plus_backend.config;

import com.example.pathshala_plus_backend.security.CustomUserDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component("auditorProvider")
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<@NonNull Long> {

    @Override
    public @NonNull Optional<Long> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() ||
                Objects.equals(auth.getPrincipal(), "anonymousUser")) {
            return Optional.empty();
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        assert userDetails != null;
        return Optional.of(userDetails.getId());
    }
}
