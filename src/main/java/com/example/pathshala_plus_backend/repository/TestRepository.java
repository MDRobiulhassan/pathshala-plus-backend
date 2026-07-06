package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.Test;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<@NonNull Test,@NonNull Long> {
}
