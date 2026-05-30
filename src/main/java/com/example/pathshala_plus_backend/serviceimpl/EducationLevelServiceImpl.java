package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.dto.response.education.EducationLevelResponse;
import com.example.pathshala_plus_backend.entity.EducationLevel;
import com.example.pathshala_plus_backend.repository.EducationLevelRepository;
import com.example.pathshala_plus_backend.service.EducationLevelService;
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
public class EducationLevelServiceImpl implements EducationLevelService {

    EducationLevelRepository educationLevelRepository;

    @Override
    public List<EducationLevelResponse> getAllEducationLevels() {
        List<EducationLevel> educationLevels = educationLevelRepository.findAll(Sort.by("id"));
        return educationLevels.stream()
                .map(educationLevel -> new EducationLevelResponse(
                        educationLevel.getId(),
                        educationLevel.getName(),
                        educationLevel.getDepartment()
                ))
                .collect(Collectors.toList());
    }
}
