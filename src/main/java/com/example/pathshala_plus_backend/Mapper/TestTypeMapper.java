package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.response.test.TestTypeResponse;
import com.example.pathshala_plus_backend.entity.TestType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TestTypeMapper {
    @Mapping(target = "name", source = "testTypeName")
    TestTypeResponse toTestTypeResponse(TestType testType);
}