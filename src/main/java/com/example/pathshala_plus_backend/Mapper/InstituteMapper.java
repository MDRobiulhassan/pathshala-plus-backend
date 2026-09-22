package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.response.institute.InstituteResponse;
import com.example.pathshala_plus_backend.entity.Institute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstituteMapper {
    @Mapping(target = "hasUnits", source = "hasUnits")
    InstituteResponse toInstituteResponse(Institute institute, boolean hasUnits);
}