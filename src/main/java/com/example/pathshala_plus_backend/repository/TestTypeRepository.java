package com.example.pathshala_plus_backend.repository;

import com.example.pathshala_plus_backend.entity.TestType;
import com.example.pathshala_plus_backend.enums.TestTypeName;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestTypeRepository extends JpaRepository<@NonNull TestType,@NonNull Long> {
    List<TestType> findByTestTypeNameIn(List<TestTypeName> testTypeNames);
}
