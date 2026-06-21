package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.dto.response.institute.InstituteResponse;
import com.example.pathshala_plus_backend.entity.Institute;
import com.example.pathshala_plus_backend.repository.InstituteRepository;
import com.example.pathshala_plus_backend.repository.UnitRepository;
import com.example.pathshala_plus_backend.service.InstituteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class InstituteServiceImpl implements InstituteService {

    InstituteRepository instituteRepository;
    UnitRepository unitRepository;

    @Override
    public List<InstituteResponse> getAllInstitutes() {
        List<Institute> institutes = instituteRepository.findAll();
        Set<Long> instituteIdsWithUnits = new HashSet<>(unitRepository.findDistinctInstituteIds());
        return institutes.stream()
                .map(institute -> new InstituteResponse(
                        institute.getId(),
                        institute.getName(),
                        institute.getShortName(),
                        institute.getLogoUrl(),
                        instituteIdsWithUnits.contains(institute.getId())
                ))
                .toList();
    }

    @Override
    public List<InstituteResponse> getInstitutesByEducationLevel(Long educationLevelId) {
        List<Institute> institutes = instituteRepository.findByEducationLevel_Id(educationLevelId);
        Set<Long> instituteIdsWithUnits = new HashSet<>(unitRepository.findDistinctInstituteIds());
        return institutes.stream()
                .map(institute -> new InstituteResponse(
                        institute.getId(),
                        institute.getName(),
                        institute.getShortName(),
                        institute.getLogoUrl(),
                        instituteIdsWithUnits.contains(institute.getId())
                ))
                .toList();
    }
}
