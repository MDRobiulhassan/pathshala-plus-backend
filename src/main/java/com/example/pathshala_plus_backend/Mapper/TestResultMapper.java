package com.example.pathshala_plus_backend.Mapper;

import com.example.pathshala_plus_backend.dto.response.test.testresult.ChapterResultResponse;
import com.example.pathshala_plus_backend.dto.response.test.testresult.PassageResultResponse;
import com.example.pathshala_plus_backend.dto.response.test.testresult.QuestionOptionResultResponse;
import com.example.pathshala_plus_backend.dto.response.test.testresult.QuestionResultResponse;
import com.example.pathshala_plus_backend.dto.response.test.testresult.SubjectResultResponse;
import com.example.pathshala_plus_backend.dto.response.test.testresult.TestResultSummaryResponse;
import com.example.pathshala_plus_backend.entity.Chapter;
import com.example.pathshala_plus_backend.entity.Passage;
import com.example.pathshala_plus_backend.entity.Question;
import com.example.pathshala_plus_backend.entity.QuestionOption;
import com.example.pathshala_plus_backend.entity.Subject;
import com.example.pathshala_plus_backend.entity.Test;
import com.example.pathshala_plus_backend.entity.TestAttempt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TestResultMapper {

    @Mapping(target = "optionId", source = "option.id")
    @Mapping(target = "optionLabel", source = "option.optionLabel")
    @Mapping(target = "optionText", source = "option.optionText")
    @Mapping(target = "optionImageUrl", source = "option.imageUrl")
    @Mapping(target = "selected", source = "selected")
    @Mapping(target = "correctOption", source = "option.isCorrect")
    QuestionOptionResultResponse toOptionResultResponse(QuestionOption option, boolean selected);

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "questionText", source = "question.questionText")
    @Mapping(target = "questionImageUrl", source = "question.imageUrl")
    @Mapping(target = "difficulty", source = "question.difficulty")
    @Mapping(target = "explanation", source = "question.explanation")
    @Mapping(target = "isCorrect", source = "isCorrect")
    @Mapping(target = "marksAwarded", source = "marksAwarded")
    @Mapping(target = "options", source = "options")
    QuestionResultResponse toQuestionResultResponse(Question question, Boolean isCorrect, Float marksAwarded, List<QuestionOptionResultResponse> options);

    @Mapping(target = "attemptId", source = "attempt.id")
    @Mapping(target = "testId", source = "test.id")
    @Mapping(target = "testName", source = "test.name")
    @Mapping(target = "score", source = "attempt.score")
    @Mapping(target = "totalMarks", source = "test.examInfo.totalMarks")
    @Mapping(target = "passMarks", source = "test.passMarks")
    @Mapping(target = "passed", source = "passed")
    @Mapping(target = "totalQuestions", source = "test.totalQuestions")
    @Mapping(target = "correctAnswers", source = "correctAnswers")
    @Mapping(target = "incorrectAnswers", source = "incorrectAnswers")
    @Mapping(target = "unansweredQuestions", source = "unansweredQuestions")
    @Mapping(target = "startedAt", source = "attempt.startedAt")
    @Mapping(target = "finishedAt", source = "attempt.finishedAt")
    TestResultSummaryResponse toSummaryResponse(TestAttempt attempt, Test test, boolean passed,
                                                int correctAnswers, int incorrectAnswers, int unansweredQuestions);

    @Mapping(target = "subjectId", source = "subject.id")
    @Mapping(target = "subjectName", source = "subject.name")
    @Mapping(target = "chapters", source = "chapters")
    SubjectResultResponse toSubjectResultResponse(Subject subject, List<ChapterResultResponse> chapters);

    @Mapping(target = "chapterId", source = "chapter.id")
    @Mapping(target = "chapterName", source = "chapter.name")
    @Mapping(target = "passages", source = "passages")
    ChapterResultResponse toChapterResultResponse(Chapter chapter, List<PassageResultResponse> passages);

    @Mapping(target = "passageId", source = "passage.id")
    @Mapping(target = "passageText", source = "passage.passageText")
    @Mapping(target = "passageImageUrl", source = "passage.imageUrl")
    @Mapping(target = "questions", source = "questions")
    PassageResultResponse toPassageResultResponse(Passage passage, List<QuestionResultResponse> questions);
}