package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
