package com.example.pathshala_plus_backend.security;

import com.example.pathshala_plus_backend.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@EqualsAndHashCode(of = "id")
public class CustomUserDetails implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        Objects.requireNonNull(user);
        this.id = user.getId();
        this.username = Objects.requireNonNull(user.getEmail());
        this.password = user.getPassword();
        this.enabled = Boolean.TRUE.equals(user.getIsActive());

        Set<GrantedAuthority> auths = new HashSet<>();

        user.getUserRoles().forEach(userRole -> {
            // Role
            auths.add(new SimpleGrantedAuthority(
                    "ROLE_" + userRole.getRole().getName().name()
            ));
            // Permissions via role
            userRole.getRole().getPermissions().forEach(permission ->
                    auths.add(new SimpleGrantedAuthority(
                            permission.getName()
                    ))
            );
        });

        this.authorities = Collections.unmodifiableSet(auths);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}