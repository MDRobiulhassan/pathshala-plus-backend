package com.example.pathshala_plus_backend.security;

import com.example.pathshala_plus_backend.exceptions.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)){
            throw new UnauthorizedException("No authenticated user found");
        }

        return userDetails.getId();
    }
}
