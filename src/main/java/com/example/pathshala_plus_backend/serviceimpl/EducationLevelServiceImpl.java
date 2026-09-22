package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.Mapper.EducationLevelMapper;
import com.example.pathshala_plus_backend.dto.response.education.EducationLevelResponse;
import com.example.pathshala_plus_backend.entity.EducationLevel;
import com.example.pathshala_plus_backend.repository.EducationLevelRepository;
import com.example.pathshala_plus_backend.service.EducationLevelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class EducationLevelServiceImpl implements EducationLevelService {

    EducationLevelRepository educationLevelRepository;
    EducationLevelMapper educationLevelMapper;

    @Override
    public List<EducationLevelResponse> getAllEducationLevels() {
        return educationLevelRepository.findAll().stream()
                .collect(Collectors.groupingBy(EducationLevel::getName))
                .entrySet()
                .stream()
                .map(entry -> new EducationLevelResponse(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(educationLevelMapper::toVariantResponse)
                                .toList()
                ))
                .toList();
    }
}