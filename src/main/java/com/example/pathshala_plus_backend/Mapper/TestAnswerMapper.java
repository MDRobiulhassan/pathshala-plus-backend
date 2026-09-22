package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.entity.Question;
import com.example.pathshala_plus_backend.entity.TestAnswer;
import com.example.pathshala_plus_backend.entity.TestAttempt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TestAnswerMapper {

    @Mapping(target = "testAttempt", source = "attempt")
    TestAnswer toTestAnswer(TestAttempt attempt, Question question);
}