package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.dto.response.question.examinfoquestion.*;
import com.example.pathshala_plus_backend.dto.response.question.subjectquestion.PassageWithTagResponse;
import com.example.pathshala_plus_backend.dto.response.question.subjectquestion.QuestionWithTagResponse;
import com.example.pathshala_plus_backend.dto.response.question.subjectquestion.SubjectBrowseResponse;
import com.example.pathshala_plus_backend.entity.*;
import com.example.pathshala_plus_backend.exceptions.ResourceNotFoundException;
import com.example.pathshala_plus_backend.repository.ExamInfoRepository;
import com.example.pathshala_plus_backend.repository.QuestionRepository;
import com.example.pathshala_plus_backend.repository.SubjectRepository;
import com.example.pathshala_plus_backend.service.QuestionService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    SubjectRepository subjectRepository;

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

    @Override
    public SubjectBrowseResponse getQuestionsBySubject(Long subjectId, int page, int size) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<@NonNull Long> idPage = questionRepository.findIdsBySubjectId(subjectId, pageable);

        List<PassageWithTagResponse> passages = List.of();
        if (!idPage.isEmpty()) {
            List<Question> questions = questionRepository.findByIdsWithDetails(idPage.getContent());

            passages = groupByPassage(questions);
        }

        return new SubjectBrowseResponse(
                subject.getId(),
                subject.getName(),
                idPage.getNumber(),
                idPage.getSize(),
                idPage.getTotalElements(),
                idPage.getTotalPages(),
                passages
        );
    }

    /* ----------------------------------------
     * Private Helper Methods
     * ---------------------------------------- */

    /* ----------------------------------------
     * Question Mappers
     * ---------------------------------------- */

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


    /* ----------------------------------------
     * Subject Browse Mappers
     * ---------------------------------------- */

    private QuestionWithTagResponse toQuestionWithTagResponse(Question question) {
        ExamInfo examInfo = question.getExamInfo();
        ExamSession examSession = examInfo.getExamSession();

        List<QuestionOptionResponse> options = question.getOptions().stream()
                .map(o -> new QuestionOptionResponse(
                        o.getId(),
                        o.getOptionLabel(),
                        o.getOptionText(),
                        o.getIsCorrect(),
                        o.getImageUrl()
                ))
                .toList();

        return new QuestionWithTagResponse(
                question.getId(),
                question.getQuestionText(),
                question.getImageUrl(),
                question.getExplanation(),
                question.getDifficulty(),
                question.getIsApproved(),
                examSession.getInstitute().getName(),
                examInfo.getUnit() != null ? examInfo.getUnit().getName() : null,
                examSession.getSessionLabel(),
                options
        );
    }

    private List<PassageWithTagResponse> groupByPassage(List<Question> questions) {
        Map<Long, List<Question>> grouped = new LinkedHashMap<>();

        for (Question q : questions) {
            Long key = q.getPassage() != null ? q.getPassage().getId() : -1L;
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(q);
        }

        return grouped.values().stream()
                .map(group -> {
                    Question first = group.get(0);
                    Passage passage = first.getPassage();

                    List<QuestionWithTagResponse> questionResponses = group.stream()
                            .map(this::toQuestionWithTagResponse)
                            .toList();

                    return new PassageWithTagResponse(
                            passage != null ? passage.getId() : null,
                            passage != null ? passage.getPassageText() : null,
                            passage != null ? passage.getImageUrl() : null,
                            questionResponses
                    );
                })
                .toList();
    }
}
