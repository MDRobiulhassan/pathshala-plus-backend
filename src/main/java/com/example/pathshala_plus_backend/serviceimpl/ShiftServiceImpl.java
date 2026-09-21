package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.Mapper.ShiftMapper;
import com.example.pathshala_plus_backend.dto.response.shift.ShiftResponse;
import com.example.pathshala_plus_backend.entity.Shift;
import com.example.pathshala_plus_backend.repository.ShiftRepository;
import com.example.pathshala_plus_backend.service.ShiftService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class ShiftServiceImpl implements ShiftService {

    ShiftRepository shiftRepository;
    ShiftMapper shiftMapper;

    @Override
    public List<ShiftResponse> getAllShiftByUnitAndExamSession(Long unitId, Long examSessionId) {
        List<Shift> shifts = shiftRepository.findByUnitIdAndExamSessionIdOrderByShiftNumberAsc(unitId, examSessionId);
        return shifts.stream()
                .map(shiftMapper::toShiftResponse)
                .toList();
    }
}