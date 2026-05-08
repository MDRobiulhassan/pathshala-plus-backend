package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.UserOauthAccount;
import com.example.pathshala_plus_backend.enums.Provider;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserOauthAccountRepository extends JpaRepository<@NonNull UserOauthAccount,@NonNull Long> {
    boolean existsByUserIdAndProvider(Long userId, Provider provider);
    List<UserOauthAccount> findAllByUserId(Long userId);
}
