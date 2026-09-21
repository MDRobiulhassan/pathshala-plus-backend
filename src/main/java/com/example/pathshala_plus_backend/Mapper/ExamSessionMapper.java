package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.response.examsession.ExamSessionResponse;
import com.example.pathshala_plus_backend.entity.ExamSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExamSessionMapper {
    @Mapping(target = "instituteId", source = "examSession.institute.id")
    @Mapping(target = "unitId", source = "examSession.unit.id")
    @Mapping(target = "hasShift", source = "hasShift")
    ExamSessionResponse toExamSessionResponse(ExamSession examSession, boolean hasShift);
}