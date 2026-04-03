package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.UserRole;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<@NonNull UserRole,@NonNull Long> {
}
