package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.response.education.EducationLevelVariantResponse;
import com.example.pathshala_plus_backend.entity.EducationLevel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EducationLevelMapper {
    EducationLevelVariantResponse toVariantResponse(EducationLevel educationLevel);
}