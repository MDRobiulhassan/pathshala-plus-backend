package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.dto.response.question.*;
import com.example.pathshala_plus_backend.entity.*;
import com.example.pathshala_plus_backend.exceptions.ResourceNotFoundException;
import com.example.pathshala_plus_backend.repository.ExamInfoRepository;
import com.example.pathshala_plus_backend.repository.QuestionRepository;
import com.example.pathshala_plus_backend.service.QuestionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class QuestionServiceImpl implements QuestionService {

    QuestionRepository questionRepository;
    ExamInfoRepository examInfoRepository;

    @Override
    public ExamQuestionsResponse getQuestionsByExamInfo(Long examInfoId) {
        ExamInfo examInfo = examInfoRepository.findById(examInfoId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam Info Not Found"));

        List<Question> questions = questionRepository.findByExamInfoWithDetails(examInfoId);

        if (questions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for this exam");
        }

        return new ExamQuestionsResponse(
                toExamInfoResponse(examInfo),
                questions.get(0).getLanguage().getCode(),
                toSubjectResponses(questions)
        );
    }

    private List<SubjectQuestionsResponse> toSubjectResponses(List<Question> questions) {
        return groupBySubject(questions).entrySet().stream()
                .map(e -> new SubjectQuestionsResponse(
                        e.getKey().getId(),
                        e.getKey().getName(),
                        toChapterResponses(e.getValue())
                ))
                .toList();
    }

    private List<ChapterQuestionsResponse> toChapterResponses(Map<Chapter, List<Question>> byChapter) {
        return byChapter.entrySet().stream()
                .map(e -> {
                    Chapter chapter = e.getKey();
                    boolean noChapter = chapter == Chapter.NONE;
                    return new ChapterQuestionsResponse(
                            noChapter ? null : chapter.getId(),
                            noChapter ? null : chapter.getName(),
                            toPassageResponses(e.getValue())
                    );
                })
                .toList();
    }

    private Map<Subject, Map<Chapter, List<Question>>> groupBySubject(List<Question> questions) {
        return questions.stream()
                .collect(Collectors.groupingBy(
                        Question::getSubject,
                        LinkedHashMap::new,
                        Collectors.groupingBy(
                                question -> question.getChapter() != null ? question.getChapter() : Chapter.NONE,
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                ));
    }

    private QuestionExamInfoResponse toExamInfoResponse(ExamInfo examInfo) {
        return new QuestionExamInfoResponse(
                examInfo.getId(),
                examInfo.getExamType(),
                examInfo.getTotalMarks(),
                examInfo.getDurationInMinutes()
        );
    }

    private QuestionResponse toQuestionResponse(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getQuestionText(),
                question.getImageUrl(),
                question.getExplanation(),
                question.getDifficulty(),
                question.getIsApproved(),
                toOptionResponses(question)
        );
    }

    private List<PassageQuestionsResponse> toPassageResponses(List<Question> questions) {
        return questions.stream()
                .collect(Collectors.groupingBy(
                        question -> question.getPassage() != null ? question.getPassage().getId() : -1L,
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values().stream()
                .map(questionList -> {
                    Question first = questionList.get(0);
                    Passage passage = first.getPassage();
                    return new PassageQuestionsResponse(
                            passage != null ? passage.getId() : null,
                            passage != null ? passage.getPassageText() : null,
                            passage != null ? passage.getImageUrl() : null,
                            questionList.stream().map(this::toQuestionResponse).toList()
                    );
                })
                .toList();
    }

    private List<QuestionOptionResponse> toOptionResponses(Question question) {
        return question.getOptions().stream()
                .map(option -> new QuestionOptionResponse(
                        option.getId(),
                        option.getOptionLabel(),
                        option.getOptionText(),
                        option.getIsCorrect(),
                        option.getImageUrl()
                ))
                .toList();
    }
}
