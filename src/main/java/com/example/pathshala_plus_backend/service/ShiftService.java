package com.example.pathshala_plus_backend.service;

import com.example.pathshala_plus_backend.dto.response.shift.ShiftResponse;

import java.util.List;

public interface ShiftService {
    List<ShiftResponse> getAllShiftByUnitAndExamSession(Long unitId, Long examSessionId);
}
