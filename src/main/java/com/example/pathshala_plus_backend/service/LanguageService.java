package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.language.LanguageResponse;
import com.example.pathshala_plus_backend.entity.Language;

import java.util.List;

public interface LanguageService {
    List<LanguageResponse> getAllLanguages();
    Language findLanguageByCode(String code);
}
