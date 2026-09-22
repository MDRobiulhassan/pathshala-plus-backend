package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.response.subject.SubjectResponse;
import com.example.pathshala_plus_backend.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    @Mapping(target = "subjectId", source = "id")
    @Mapping(target = "subjectName", source = "name")
    @Mapping(target = "educationLevelId", source = "educationLevel.id")
    SubjectResponse toSubjectResponse(Subject subject);
}