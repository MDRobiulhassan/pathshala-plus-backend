package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.response.examinfo.ExamInfoResponse;
import com.example.pathshala_plus_backend.dto.response.question.examinfoquestion.QuestionExamInfoResponse;
import com.example.pathshala_plus_backend.entity.ExamInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExamInfoMapper {
    @Mapping(target = "testId", source = "test.id")
    ExamInfoResponse toExamInfoResponse(ExamInfo examInfo);

    @Mapping(target = "examInfoId", source = "id")
    QuestionExamInfoResponse toQuestionExamInfoResponse(ExamInfo examInfo);
}