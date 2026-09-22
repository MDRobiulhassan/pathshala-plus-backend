package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.response.language.LanguageResponse;
import com.example.pathshala_plus_backend.entity.Language;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LanguageMapper {
    LanguageResponse toLanguageResponse(Language language);
}