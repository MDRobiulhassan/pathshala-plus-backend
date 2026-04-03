package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.EducationLevel;
import com.example.pathshala_plus_backend.entity.Role;
import com.example.pathshala_plus_backend.enums.RoleName;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<@NonNull Role,@NonNull Long> {
    Optional<Role> findByName(RoleName roleName);
}
