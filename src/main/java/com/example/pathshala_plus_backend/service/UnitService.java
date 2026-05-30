package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.unit.UnitResponse;

import java.util.List;

public interface UnitService {
    List<UnitResponse> getAllUnitByInstitute(Long instituteId);
}
