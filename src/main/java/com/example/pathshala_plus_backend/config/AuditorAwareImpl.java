package com.example.pathshala_plus_backend.config;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<@NonNull Long> {

    @Override
    public @NonNull Optional<Long> getCurrentAuditor() {
        Long userId = 1L;
        return Optional.of(userId);
    }
}
