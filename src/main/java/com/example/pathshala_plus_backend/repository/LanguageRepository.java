package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.Language;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<@NonNull Language,@NonNull Long> {
    Optional<Language> findByCode(String code);
}
