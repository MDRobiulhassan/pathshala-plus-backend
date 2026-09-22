package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.response.test.testquestion.ChapterQuestionsResponse;
import com.example.pathshala_plus_backend.dto.response.test.testquestion.PassageQuestionsResponse;
import com.example.pathshala_plus_backend.dto.response.test.testquestion.QuestionOptionResponse;
import com.example.pathshala_plus_backend.dto.response.test.testquestion.QuestionResponse;
import com.example.pathshala_plus_backend.dto.response.test.testquestion.SubjectQuestionsResponse;
import com.example.pathshala_plus_backend.entity.Chapter;
import com.example.pathshala_plus_backend.entity.Passage;
import com.example.pathshala_plus_backend.entity.Question;
import com.example.pathshala_plus_backend.entity.QuestionOption;
import com.example.pathshala_plus_backend.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TestQuestionMapper {

    @Mapping(target = "optionId", source = "option.id")
    @Mapping(target = "optionLabel", source = "option.optionLabel")
    @Mapping(target = "optionText", source = "option.optionText")
    @Mapping(target = "optionImageUrl", source = "option.imageUrl")
    @Mapping(target = "selected", source = "selected")
    QuestionOptionResponse toOptionResponse(QuestionOption option, boolean selected);

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "questionText", source = "question.questionText")
    @Mapping(target = "questionImageUrl", source = "question.imageUrl")
    @Mapping(target = "difficulty", source = "question.difficulty")
    @Mapping(target = "options", source = "options")
    QuestionResponse toQuestionResponse(Question question, List<QuestionOptionResponse> options);

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
}