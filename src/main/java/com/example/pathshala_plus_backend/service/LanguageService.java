package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.language.LanguageResponse;

import java.util.List;

public interface LanguageService {
    List<LanguageResponse> getAllLanguages();
}
