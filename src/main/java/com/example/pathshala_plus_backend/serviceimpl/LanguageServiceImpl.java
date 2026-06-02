package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.dto.response.language.LanguageResponse;
import com.example.pathshala_plus_backend.entity.Language;
import com.example.pathshala_plus_backend.exceptions.ResourceNotFoundException;
import com.example.pathshala_plus_backend.repository.LanguageRepository;
import com.example.pathshala_plus_backend.service.LanguageService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class LanguageServiceImpl implements LanguageService {

    LanguageRepository languageRepository;

    @Override
    public List<LanguageResponse> getAllLanguages() {
        List<Language> languages = languageRepository.findAll(Sort.by("name"));
        return languages.stream()
                .map(language -> new LanguageResponse(
                        language.getId(),
                        language.getName(),
                        language.getCode()
                ))
                .toList();
    }

    @Override
    public Language findLanguageByCode(String code) {
        return languageRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code: " + code));
    }
}
