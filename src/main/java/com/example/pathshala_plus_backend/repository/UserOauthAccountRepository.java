package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.EducationLevel;
import com.example.pathshala_plus_backend.entity.UserOauthAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOauthAccountRepository extends JpaRepository<UserOauthAccount,Long> {
}
