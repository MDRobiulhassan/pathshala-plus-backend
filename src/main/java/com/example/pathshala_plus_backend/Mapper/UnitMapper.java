package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.response.unit.UnitResponse;
import com.example.pathshala_plus_backend.entity.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UnitMapper {
    @Mapping(target = "instituteId", source = "institute.id")
    UnitResponse toUnitResponse(Unit unit);
}