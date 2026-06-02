package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.dto.response.unit.UnitResponse;
import com.example.pathshala_plus_backend.entity.Unit;
import com.example.pathshala_plus_backend.repository.UnitRepository;
import com.example.pathshala_plus_backend.service.UnitService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class UnitServiceImpl implements UnitService {

    UnitRepository unitRepository;

    @Override
    public List<UnitResponse> getAllUnitByInstitute(Long instituteId) {
        List<Unit> units = unitRepository.findByInstituteIdOrderByNameAsc(instituteId);
        return units.stream()
                .map(unit -> new UnitResponse(
                        unit.getId(),
                        unit.getName(),
                        unit.getInstitute().getId()
                ))
                .toList();
    }
}
