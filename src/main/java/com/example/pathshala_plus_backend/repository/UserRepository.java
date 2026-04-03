package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<@NonNull User, @NonNull Long> {

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.userRoles ur " +
            "LEFT JOIN FETCH ur.role r " +
            "LEFT JOIN FETCH r.permissions " +
            "WHERE u.email = :email AND u.isDeleted = false")
    Optional<User> findByEmailWithRolesAndPermissions(@Param("email") String email);

    Optional<User> findByEmail(String email);
}
