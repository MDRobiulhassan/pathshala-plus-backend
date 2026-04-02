package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.Passage;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassageRepository extends JpaRepository<@NonNull Passage,@NonNull Long> {
}
