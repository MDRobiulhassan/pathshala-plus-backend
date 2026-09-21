package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.response.question.examinfoquestion.ChapterQuestionsResponse;
import com.example.pathshala_plus_backend.dto.response.question.examinfoquestion.PassageQuestionsResponse;
import com.example.pathshala_plus_backend.dto.response.question.examinfoquestion.QuestionOptionResponse;
import com.example.pathshala_plus_backend.dto.response.question.examinfoquestion.QuestionResponse;
import com.example.pathshala_plus_backend.dto.response.question.examinfoquestion.SubjectQuestionsResponse;
import com.example.pathshala_plus_backend.dto.response.question.subjectquestion.PassageWithTagResponse;
import com.example.pathshala_plus_backend.dto.response.question.subjectquestion.QuestionWithTagResponse;
import com.example.pathshala_plus_backend.dto.response.question.subjectquestion.SubjectBrowseResponse;
import com.example.pathshala_plus_backend.entity.Chapter;
import com.example.pathshala_plus_backend.entity.Passage;
import com.example.pathshala_plus_backend.entity.Question;
import com.example.pathshala_plus_backend.entity.QuestionOption;
import com.example.pathshala_plus_backend.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "optionId", source = "id")
    @Mapping(target = "optionImageUrl", source = "imageUrl")
    QuestionOptionResponse toOptionResponse(QuestionOption option);

    List<QuestionOptionResponse> toOptionResponses(List<QuestionOption> options);

    @Mapping(target = "questionId", source = "id")
    @Mapping(target = "questionImageUrl", source = "imageUrl")
    QuestionResponse toQuestionResponse(Question question);

    @Mapping(target = "questionId", source = "id")
    @Mapping(target = "questionImageUrl", source = "imageUrl")
    @Mapping(target = "instituteName", source = "examInfo.examSession.institute.name")
    @Mapping(target = "unitName", source = "examInfo.unit.name")
    @Mapping(target = "sessionLabel", source = "examInfo.examSession.sessionLabel")
    QuestionWithTagResponse toQuestionWithTagResponse(Question question);

    @Mapping(target = "subjectId", source = "subject.id")
    @Mapping(target = "subjectName", source = "subject.name")
    @Mapping(target = "chapters", source = "chapters")
    SubjectQuestionsResponse toSubjectQuestionsResponse(Subject subject, List<ChapterQuestionsResponse> chapters);

    @Mapping(target = "chapterId", source = "chapter.id")
    @Mapping(target = "chapterName", source = "chapter.name")
    @Mapping(target = "passages", source = "passages")
    ChapterQuestionsResponse toChapterQuestionsResponse(Chapter chapter, List<PassageQuestionsResponse> passages);

    @Mapping(target = "passageId", source = "passage.id")
    @Mapping(target = "passageText", source = "passage.passageText")
    @Mapping(target = "passageImageUrl", source = "passage.imageUrl")
    @Mapping(target = "questions", source = "questions")
    PassageQuestionsResponse toPassageQuestionsResponse(Passage passage, List<QuestionResponse> questions);

    @Mapping(target = "passageId", source = "passage.id")
    @Mapping(target = "passageText", source = "passage.passageText")
    @Mapping(target = "passageImageUrl", source = "passage.imageUrl")
    @Mapping(target = "questions", source = "questions")
    PassageWithTagResponse toPassageWithTagResponse(Passage passage, List<QuestionWithTagResponse> questions);

    @Mapping(target = "subjectId", source = "subject.id")
    @Mapping(target = "subjectName", source = "subject.name")
    @Mapping(target = "page", source = "idPage.number")
    @Mapping(target = "size", source = "idPage.size")
    @Mapping(target = "totalElements", source = "idPage.totalElements")
    @Mapping(target = "totalPages", source = "idPage.totalPages")
    @Mapping(target = "passages", source = "passages")
    SubjectBrowseResponse toSubjectBrowseResponse(Subject subject, Page<Long> idPage, List<PassageWithTagResponse> passages);
}