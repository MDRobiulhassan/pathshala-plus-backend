package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.response.shift.ShiftResponse;
import com.example.pathshala_plus_backend.entity.Shift;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShiftMapper {
    ShiftResponse toShiftResponse(Shift shift);
}