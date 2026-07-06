package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.dto.response.education.EducationLevelResponse;
import com.example.pathshala_plus_backend.dto.response.education.EducationLevelVariantResponse;
import com.example.pathshala_plus_backend.entity.EducationLevel;
import com.example.pathshala_plus_backend.enums.Department;
import com.example.pathshala_plus_backend.repository.EducationLevelRepository;
import com.example.pathshala_plus_backend.service.EducationLevelService;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class EducationLevelServiceImpl implements EducationLevelService {

    EducationLevelRepository educationLevelRepository;

    @Override
    public List<EducationLevelResponse> getAllEducationLevels() {

        List<EducationLevel> educationLevels =
                educationLevelRepository.findAll();

        Map<String, List<EducationLevel>> groupedEducationLevels =
                educationLevels.stream()
                        .collect(Collectors.groupingBy(EducationLevel::getName));

        return groupedEducationLevels.entrySet()
                .stream()
                .map(entry -> {

                    List<EducationLevelVariantResponse> variants =
                            entry.getValue().stream()
                                    .map(level -> new EducationLevelVariantResponse(
                                            level.getId(),
                                            level.getDepartment()
                                    ))
                                    .toList();

                    return new EducationLevelResponse(
                            entry.getKey(),
                            variants
                    );
                })
                .toList();
    }
}