package com.example.pathshala_plus_backend.serviceimpl;

import com.example.pathshala_plus_backend.Mapper.ExamInfoMapper;
import com.example.pathshala_plus_backend.Mapper.QuestionMapper;
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
    QuestionMapper questionMapper;
    ExamInfoMapper examInfoMapper;

    @Override
    public ExamQuestionsResponse getQuestionsByExamInfo(Long examInfoId) {
        ExamInfo examInfo = examInfoRepository.findById(examInfoId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam Info Not Found"));

        List<Question> questions = questionRepository.findByExamInfoWithDetails(examInfoId);

        if (questions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for this exam");
        }

        return new ExamQuestionsResponse(
                examInfoMapper.toQuestionExamInfoResponse(examInfo),
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

        return questionMapper.toSubjectBrowseResponse(subject, idPage, passages);
    }

    /* ---------------------------------------- */

    private List<SubjectQuestionsResponse> toSubjectResponses(List<Question> questions) {
        return groupBySubject(questions).entrySet().stream()
                .map(e -> questionMapper.toSubjectQuestionsResponse(
                        e.getKey(),
                        toChapterResponses(e.getValue())))
                .toList();
    }

    private List<ChapterQuestionsResponse> toChapterResponses(Map<Chapter, List<Question>> byChapter) {
        return byChapter.entrySet().stream()
                .map(e -> questionMapper.toChapterQuestionsResponse(
                        e.getKey(),
                        toPassageResponses(e.getValue())))
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

    private List<PassageQuestionsResponse> toPassageResponses(List<Question> questions) {
        return questions.stream()
                .collect(Collectors.groupingBy(
                        question -> question.getPassage() != null ? question.getPassage().getId() : -1L,
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .values().stream()
                .map(questionList -> questionMapper.toPassageQuestionsResponse(
                        questionList.get(0).getPassage(),
                        questionList.stream().map(questionMapper::toQuestionResponse).toList()))
                .toList();
    }

    private List<PassageWithTagResponse> groupByPassage(List<Question> questions) {
        Map<Long, List<Question>> grouped = new LinkedHashMap<>();

        for (Question q : questions) {
            Long key = q.getPassage() != null ? q.getPassage().getId() : -1L;
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(q);
        }

        return grouped.values().stream()
                .map(group -> questionMapper.toPassageWithTagResponse(
                        group.get(0).getPassage(),
                        group.stream().map(questionMapper::toQuestionWithTagResponse).toList()))
                .toList();
    }
}